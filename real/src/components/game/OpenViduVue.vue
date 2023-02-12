<template>
    <div id="main_vedio_container" style="z-index: 10000;">
        <!-- <video autoplay ref="video" class="enemy_video"></video> -->
        <user-video 
            :stream-manager="mainStreamManager" 
            :class-name="enemy_video">
        </user-video>
        <!-- <div class="camera_name">
            임시이름 {{ enemy_name }}
        </div> -->
        <div v-if="isInked" class="ink-bomb">
            <img src="@/assets/inkbomb.png">
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
                enemy_name: undefined,
                my_video: "my_video",
                enemy_video: "enemy_video",

                /* for item */
                players_state: {},
                isInked: false,
                amIInked: false,

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
            async init() {
                this.OV = new OpenVidu();
                this.session = this.OV.initSession();
                this.session.on("streamCreated", ({ stream }) => {
                    this.players_state[stream.connection.data.nickname] = {
                        isInked: false,
                    };
                    if (stream.connection.data.nickname == this.user.nickname) {
                        return;
                    }
                    const subscriber = this.session.subscribe(stream);
                    this.subscribers.push(subscriber);
                    console.log("스트림을 발견했어요!")
                    console.log("현재 사람은 " + this.subscribers.length + "명이에요")
                    this.mainStreamManager = subscriber;
                    // this.mainStreamManager.addVideoElement(this.$refs.video);
                    const { connection } = this.mainStreamManager.stream;
                    console.log("커넥션 데이터에요:", connection.data)
                    const { clientData } = JSON.parse(connection.data);
                    this.enemy_name = clientData;
                });
                this.session.on("streamDestroyed", ({ stream }) => {
                    const index = this.subscribers.indexOf(stream.streamManager, 0);
                    if (index >= 0) {
                        this.subscribers.splice(index, 1);
                    }
                    console.log("누군가가 스트림을 종료했어요!")
                    console.log("남은 사람은 " + this.subscribers.length + "명이에요")
                });
                this.session.on("exception", ({ exception }) => {
                    console.warn("오류ㅠㅠ" + exception);
                });
                await this.getToken(this.mySessionId + "game").then(async (token) => {
                    console.log("토큰을생성해요:" + token);
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
                console.log("getToken 시작")
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
                console.log("createToken 시작")
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
            },
            clientUser (streamManager)  {
                const { connection } = streamManager.stream;
                const { clientData } = JSON.parse(connection.data);
                return clientData.user;
            },
            inkedCheck() {
                if (this.players_state[this.clientUser(this.mainStreamManager).nickname].isInked) {
                    this.isInked = true;
                } else {
                    this.isInked = false;
                }
            },
            async openMediaDevices(constraints) {
                return await navigator.mediaDevices.getUserMedia(constraints);
            }
        },
    async created() {
        await this.openMediaDevices({
        video: true,
        audio: true,
        }).then((stream) => {
            console.log('created stream');
            console.log(stream);
            this.myVideoStream = stream;
        });
        
        console.log("--------------ffff--------------");
        console.log(this.subscribers.length + "명의 사람이 있어요");
        for (var sub of this.subscribers) {
            console.log("---", sub);
        }

        this.conn.addEventListener("message", (e) => {
            console.log("누군가 아이템을 사용하였습니다.");
            const content = JSON.parse(e.data);
            if (content.type == "item4") {
                const nickname = content.nickname;
                if (nickname == this.user.nickname) {
                    if (content.info.isStart == 1) {
                        this.amIInked = true;
                    } else {
                        this.amIInked = false;
                    }
                }
                if (content.info.isStart == 1) {
                    this.players_state[nickname].isInked = true;
                    if (this.clientUser(this.mainStreamManager).nickname == nickname) {
                        this.isInked = true;
                    }
                } else {
                    this.players_state[nickname].isInked = false;
                    if (this.clientUser(this.mainStreamManager).nickname == nickname) {
                        this.isInked = false;
                    }
                }
            }
        })

        this.init();
    },
    computed: {
        myUserName() {
            return this.user.nickname;
        }
    },
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
    height: $video_height;
    overflow: hidden;
    border: dashed;
    background-color: black;
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
    border: solid;
    border-color: white;
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
}
.ink-bomb {
    width: 100%;
    height: 100%;
}
.hidden_modal{
    visibility: hidden;
}
</style>