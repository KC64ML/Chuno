<template>
    <div id="main_vedio_container" :class="isLongVideo ? 'long-version' : 'short-version'" style="z-index: 10000;">
        <div id="long_video_toggle" :class="isLongVideo ? 'upside-down' : ''" @click="videoLengthToggle">
            <img src="@/assets/to_bottom.png">
        </div>
        <!-- <video autoplay ref="video" class="enemy_video"></video> -->
        <user-video 
            :stream-manager="mainStreamManager" 
            :class-name="enemy_video">
        </user-video>
        <div v-if="isInked" class="ink-bomb">
            <img src="@/assets/inkbomb.png">
        </div>
        
        <div v-if="isOut" class="ink-bomb">
            <img src="@/assets/outPlayer.png">
        </div>
        <img class="camera_arrow left_arrow" src="@/assets/camera_left.svg" alt="">
        <img class="camera_arrow right_arrow" src="@/assets/camera_right.svg" alt="">
        <div class="arrow_box right_box" @click="rightArrow"></div>  
        <div class="arrow_box left_box" @click="leftArrow"></div>
    </div>
    <div class="my_video_box" :class="{hidden_modal:!my_cam_modal.active}" style="z-index: 100000">
        <!-- <video autoplay ref="my_video" class="my_video"></video> -->
        <user-video 
            :stream-manager="myStreamManager"
            :class-name="my_video">
        </user-video>
        <div v-if="amIInked" class="ink-bomb-me">
            <img src="@/assets/inkbomb.png">
        </div>
        
        <div v-if="amIOut" class="ink-bomb-me">
            <img src="@/assets/outPlayer.png">
        </div>
    </div>
</template>

<script>
import { OpenVidu } from "openvidu-browser";
import UserVideo from "@/components/game/UserVideo.vue";

// const APPLICATION_SERVER_URL = "https://demos.openvidu.io/";
// const APPLICATION_SERVER_URL = "http://localhost:5000/";
const APPLICATION_SERVER_URL = process.env.VUE_APP_RTC;
// const APPLICATION_SERVER_URL = "http://:8000/";


    export default {
        components: {
            UserVideo,
        },
        props: {
            vidu_end: Boolean,
            my_cam_modal: Object,
            user: Object,
        },
        data() {
            return {
                // OpenVidu objects
                OV: undefined,
                session: undefined,
                myVideoStream: null,
                mainStreamManager: undefined,
                myStreamManager: undefined,
                publisher: undefined,
                subscribers: [],
                nowVideoNum: 0,
                my_video: "my_video",
                enemy_video: "enemy_video",
                isLongVideo: false,

                /* for item */
                players_state: {},
                isInked: false,
                amIInked: false,
                isOut: false,
                amIOut: false,

                // Join form
                mySessionId: this.$route.params.roomId,
            };
        },
        // computed: {
        //     clientData () {
        //         const { clientData } = this.getConnectionData();
        //         return clientData;
        //     },
        // },
        methods: {
            async initOpenvidu() {
                this.OV = new OpenVidu();
                this.session = this.OV.initSession();
                this.session.on("streamCreated", ({ stream }) => {
                    const otherData = JSON.parse(stream.connection.data).clientData;
                    console.log("otherData: ");
                    console.log(otherData);
                    console.log("this.user: ");
                    console.log(this.user);
                    if (otherData.user.nickname == this.user.nickname) {
                        return;
                    }
                    const subscriber = this.session.subscribe(stream);
                    this.subscribers.push(subscriber);
                    // console.log("스트림을 발견했어요!")
                    // console.log("현재 사람은 " + this.subscribers.length + "명이에요")
                    this.mainStreamManager = subscriber;
                    // this.mainStreamManager.addVideoElement(this.$refs.video);
                    const { connection } = this.mainStreamManager.stream;
                    // console.log("커넥션 데이터에요:", connection.data)
                    const { clientData } = JSON.parse(connection.data);
                    // console.log("clientData : " + clientData);
                    this.players_state[clientData.user.nickname] = {
                        isInked: false,
                        isOut: false,
                    };
                });
                this.session.on("streamDestroyed", ({ stream }) => {
                    const index = this.subscribers.indexOf(stream.streamManager, 0);
                    if (index >= 0) {
                        this.subscribers.splice(index, 1);
                    }
                    console.log("someone destroyed stream")
                    console.log(this.subscribers.length + " remain")
                });
                this.session.on("exception", ({ exception }) => {
                    console.warn("error:" + exception);
                });
                await this.getToken(this.mySessionId + "game").then(async (token) => {
                    // console.log("토큰을생성해요:" + token);
                    // console.log("openviduvue 하기 전에 user 상태 : ");
                    // console.log(this.user);
                    this.session.connect(token, {
                        clientData: {
                            user: this.user,
                        },
                        role: "good"
                    })
                        .then(() => {
                        let publisher = this.OV.initPublisher(undefined, {
                            audioSource: undefined, // The source of audio. If undefined default microphone
                            videoSource: this.myVideoStream, // The source of video. If undefined default webcam
                            publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
                            publishVideo: true, // Whether you want to start publishing with your video enabled or not
                            resolution: "640x480", // The resolution of your video
                            frameRate: 60, // The frame rate of your video
                            insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
                            mirror: true, // Whether to mirror your local video or not
                        });
                        this.myStreamManager = publisher;
                        this.publisher = publisher;
                        this.session.publish(this.publisher);
                        // this.myStreamManager.addVideoElement(this.$refs.my_video);
                    })
                    .catch((error) => {
                        console.log("There was an error connecting to the session:", error.code, error.message);
                    });
                });
                window.addEventListener("beforeunload", this.leaveSession);
            },
            leaveSession() {
                console.log("-----OpenVidu Session Disconnecting-----")
                if (this.session) this.session.disconnect();

                this.session = undefined;
                this.mainStreamManager = undefined;
                this.publisher = undefined;
                this.subscribers = [];
                this.OV = undefined;

                window.removeEventListener("beforeunload", this.leaveSession);
            },
            updateMainVideoStreamManager(stream) {
                if (this.mainStreamManager === stream) return;
                this.mainStreamManager = stream;
                // this.mainStreamManager.addVideoElement(this.$refs.video);
            },
            async getToken(mySessionId) {
                // console.log("getToken 시작")
                const sessionId = await this.createSession(mySessionId);
                return await this.createToken(sessionId);
            },
            async createSession(sessionId) {
                const response = await this.axios.post(APPLICATION_SERVER_URL + 'api/sessions', { customSessionId: sessionId }, {
                    headers: { 'Content-Type': 'application/json', },
                });
                return response.data; // The sessionId
            },
            async createToken(sessionId) {
                // console.log("createToken 시작")
                const response = await this.axios.post(APPLICATION_SERVER_URL + 'api/sessions/' + sessionId + '/connections', {}, {
                    headers: { 'Content-Type': 'application/json', },
                });
                return response.data; // The token
            },
            // getConnectionData () {
            //     const { connection } = this.mainStreamManager.stream;
            //     return JSON.parse(connection.data);
            // },
            leftArrow() {
                let idx = this.subscribers.indexOf(this.mainStreamManager);
                let len = this.subscribers.length;
                idx--;
                if (idx < 0) {
                    idx = len - 1;
                }
                this.updateMainVideoStreamManager(this.subscribers[idx]);
                this.inkedCheck();
                this.outCheck();
            },
            rightArrow() {
                let idx = this.subscribers.indexOf(this.mainStreamManager);
                let len = this.subscribers.length;
                idx++;
                if (idx >= len) {
                    idx = 0;
                }
                this.updateMainVideoStreamManager(this.subscribers[idx]);
                this.inkedCheck();
                this.outCheck();
            },
            clientUser (streamManager)  {
                const { connection } = streamManager.stream;
                const { clientData } = JSON.parse(connection.data);
                return clientData.user;
            },
            inkedCheck() {
                // console.log("==============inked check=============");
                // console.log(this.clientUser(this.mainStreamManager).nickname);
                // console.log(this.players_state[this.clientUser(this.mainStreamManager).nickname]);
                if (this.players_state[this.clientUser(this.mainStreamManager).nickname].isInked) {
                    this.isInked = true;
                } else {
                    this.isInked = false;
                }
            },
            outCheck() {
                if (this.players_state[this.clientUser(this.mainStreamManager).nickname].isOut) {
                    this.isOut = true;
                } else {
                    this.isOut = false;
                }
            },
            async openMediaDevices(constraints) {
                return await navigator.mediaDevices.getUserMedia(constraints);
            },
            videoLengthToggle() {
                this.isLongVideo = !this.isLongVideo;
            },
    },
    beforeRouteLeave() {
        if (this.session) {
            this.leaveSession();    
        }
    },
    beforeUnmount() {
        if (this.session) {
            this.leaveSession();    
        }
    },
    async created() {
        this.conn.addEventListener("message", (e) => {
            const content = JSON.parse(e.data);
            // console.log("받은 메세지 : ");
            // console.log(content);
            const nickname = content.nickname;
            if (content.type == "item4") {
                // console.log("누군가 먹물탄을 사용하였습니다.", content);
                if (nickname == this.user.nickname) {
                    if (content.info.isStart == 1) {
                        this.amIInked = true;
                    } else {
                        this.amIInked = false;
                    }
                }else if (content.info.isStart == 1) {
                    this.players_state[nickname].isInked = true; // undefined
                    if (this.clientUser(this.mainStreamManager).nickname == nickname) {
                        this.isInked = true;
                        // console.log("this.isInked : " + this.isInked);
                    }
                } else {
                    this.players_state[nickname].isInked = false;
                    if (this.clientUser(this.mainStreamManager).nickname == nickname) {
                        this.isInked = false;
                    }
                }
            } else if (content.type == "playerOut") {
                if (nickname == this.user.nickname) {
                    this.amIOut = true;
                }
                this.players_state[nickname].isOut = true;
                if (this.clientUser(this.mainStreamManager).nickname == nickname) {
                    this.isOut = true;
                }
            } else if (content.type == "caughtRunner") {
                if (nickname == this.user.nickname) {
                    this.amIOut = true;
                }
                this.players_state[nickname].isOut = true;
                if (this.clientUser(this.mainStreamManager).nickname == nickname) {
                    this.isOut = true;
                }
            }
        })


        await this.openMediaDevices({
        video: true,
        audio: true,
        }).then((stream) => {
            // console.log('created stream');
            // console.log(stream);
            this.myVideoStream = stream;
        });
        
        // console.log("--------------ffff--------------");
        // console.log(this.subscribers.length + "명의 사람이 있어요");
        for (var sub of this.subscribers) {
            console.log("---", sub);
        }

        
        setTimeout(() => {
            this.initOpenvidu();
        }, 500);
    },
    computed: {
        myUserName() {
            return this.user.nickname;
        }
    },
    watch: {
        'vidu_end': 'leaveSession'
    }
}
</script>

<style lang="scss" scoped>
@import "@/assets/scss/_variable";
$my_video_width: 150px;
$my_video_margin: 20px;

#main_vedio_container {
    position: absolute;
    top: 0;
    width: 100vw;
    overflow: hidden;
    border: dashed;
    background-color: black;
    transition: all 0.5s ease-in-out;
}
#long_video_toggle {
    position: absolute;
    top: 5px;
    right: 5px;
    width: 20px;
    height: 20px;
    transition: all 0.3s ease-in;
    z-index: 1;
}
#long_video_toggle img {
    width: 100%;
    height: 100%;
}
// @keyframes upsidedown {
//   0% {
//     left:100px;
//   }
//   100% {
//     left:300px;
//   }
// }
.upside-down {
    transform: rotate(180deg);
}
.short-version {
    height: $video_height;
}
.long-version {
    height: $video_height_long;
}

.camera_name {
    position: absolute;
    color: white;
    top: 10px;
    left: 20px;
    background-color: rgb(0,0,0,0.5);
    padding: 4px;
    border-radius: 10px;
    font-size: 15px;
}


.camera_arrow {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    height: 60px;
}

.left_arrow {
    left: -10px;
}
.right_arrow {
    right: 5px
}
.arrow_box {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    height: 100px;
    width: 50px;
}
.left_box {
    left: 0;
}
.right_box {
    right: 0;
}

.my_video_box {
    background-color: whitesmoke;
    position: absolute;
    bottom: $footer_height;
    right: 0;
    margin: 10px;
    z-index: 100;
    width: 200px;

    min-height: 100px;
    border: dashed;

    transition: all 0.5s
}
.ink-bomb {
    position: inherit;
    width: 100%;
    height: 100%;
}
.ink-bomb img {
    width: 100%;
    height: 100%;
}
.ink-bomb-me {
    position: absolute;
    top: 0;
    width: 100%;
    height: 100%;
    opacity: 0.5;
    overflow: hidden;
}
.ink-bomb-me img {
    width: 100%;
    height: 100%;
}
.hidden_modal{
    opacity: 0;
    transform: translateY(50%);
    display: none;
}
</style>