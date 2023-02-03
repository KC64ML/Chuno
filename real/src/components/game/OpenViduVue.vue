<template>
    <div id="main_vedio_container" style="z-index: 1000">
        <!-- <user-video :stream-manager="mainStreamManager" style="border: dashed;"/> -->
        <user-video :stream-manager="myStreamManager" style="border: dashed;"/>
        <video autoplay ref="video" style="border:solid"></video>
    </div>
</template>

<script>
import { OpenVidu } from "openvidu-browser";
import UserVideo from "@/components/game/UserVideo.vue";

// const APPLICATION_SERVER_URL = "https://demos.openvidu.io/";
const APPLICATION_SERVER_URL = "http://localhost:5000/";
    export default {
        components: {
            UserVideo,
        },
        props: {
            my_cam_modal: undefined
        },
        data() {
            return {
                // OpenVidu objects
                OV: undefined,
                session: undefined,
                mainStreamManager: undefined,
                myStreamManager: undefined,
                publisher: undefined,
                subscribers: [],

                // Join form
                mySessionId: this.$route.params.roomId,
                myUserName: "dddd",
            };
        },
        methods: {
            async init() {
                this.OV = new OpenVidu();
                this.session = this.OV.initSession();
                this.session.on("streamCreated", ({ stream }) => {
                    console.log("스트림을 발견했어요!")
                    const subscriber = this.session.subscribe(stream);
                    this.subscribers.push(subscriber);
                });
                this.session.on("streamDestroyed", ({ stream }) => {
                    console.log("누군가가 스트림을 종료했어요!")
                    const index = this.subscribers.indexOf(stream.streamManager, 0);
                    if (index >= 0) {
                        this.subscribers.splice(index, 1);
                    }
                });
                this.session.on("exception", ({ exception }) => {
                    console.warn("오류ㅠㅠ" + exception);
                });
                await this.getToken(this.mySessionId).then(async (token) => {
                    console.log("토큰을생성해요");
                    await this.session.connect(token, { clientData: this.myUserName }).then(() => {
                        let publisher = this.OV.initPublisher(undefined, {
                            audioSource: undefined, // The source of audio. If undefined default microphone
                            videoSource: undefined, // The source of video. If undefined default webcam
                            publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
                            publishVideo: true, // Whether you want to start publishing with your video enabled or not
                            resolution: "640x480", // The resolution of your video
                            frameRate: 30, // The frame rate of your video
                            insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
                            mirror: false, // Whether to mirror your local video or not
                        });
                        this.myStreamManager = publisher;
                        this.publisher = publisher;
                        this.session.publish(this.publisher);
                        this.myStreamManager.addVideoElement(this.$refs.video);
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
            },
            async getToken(mySessionId) {
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
                const response = await this.axios.post(APPLICATION_SERVER_URL + 'api/sessions/' + sessionId + '/connections', {}, {
                    headers: { 'Content-Type': 'application/json', },
                });
                return response.data; // The token
            },
        },
        async created() {
            await this.init();
            console.log("--------------ffff--------------");
            console.log(this.subscribers);
            for (var sub of this.subscribers) {
                console.log("---", sub);
            }
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
    width: 100vw
}
</style>