<template>
    <div id="main_vedio_container">
        <video autoplay ref="video" class="enemy_video" style="border: dashed; width: 100%; max-width: 600px;"></video>
    </div>

    <div class="my_video_box" :class="{ hidden_modal: !my_cam_modal }">
        <video autoplay ref="my_video" class="my_video" style="border: dashed; width: 100%"></video>
    </div>
</template>

<script>
import { OpenVidu } from "openvidu-browser";
const APPLICATION_SERVER_URL = process.env.VUE_APP_RTC;

export default {
    props: {
        my_cam_modal: undefined,
    },
    data() {
        return {
            OV: undefined,
            session: undefined,

            mainStreamManager: undefined,
            publisher: undefined,
            subscribers: [],

            mySessionId: this.$route.params.roomId + 1000,
            myUserName: this.$route.params.nickname,
        }
    },
    created() {
        this.OV = new OpenVidu();
        this.session = this.OV.initSession();

        this.getToken(this.mySessionId).then((token) => {
            this.session.connect(token, { clientData: this.myUserName }).then(() => {
                let publisher = this.OV.initPublisher(undefined, {
                    audioSource: undefined,
                    videoSource: undefined,
                    publishAudio: true,
                    publishVideo: true,
                    resolution: "64x48",
                    frameRate: 5 ,
                    insertMode: "APPEND",
                    mirror: false,
                });
                this.publisher = publisher;
                this.session.publish(this.publisher);
                this.publisher.addVideoElement(this.$refs.my_video);
            }).catch((e) => {
                console.log("퍼블리쉬를 하는 도중에 에러가 발생해써요ㅜㅜ", e);
            });
        });
        window.addEventListener("beforeunload", this.leaveSession);
    },
    methods: {
        leaveSession() {
            if (this.session) this.session.disconnect();
            this.session = undefined;
            this.mainStreamManager = undefined;
            this.publisher = undefined;
            this.subscribers = [];
            this.OV = undefined;

            window.removeEventListener("beforeunload", this.leaveSession);
        },
        // updateMainVideoStreamManager(stream) {
        //     if (this.mainStreamManager === stream) return;
        //     this.mainStreamManager = stream;
        // },
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
    height: $video_height;
    overflow: hidden;
    border: dashed;
    background-color: red;
}

.my_video_box {
    background-color: red;
    position: absolute;
    bottom: $footer_height;
    min-height: 10px;
    right: 0;
    margin: 10px;
    z-index: 100;
    width: 200px;
}

.hidden_modal {
    visibility: hidden;
}
</style>