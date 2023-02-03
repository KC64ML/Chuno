<template>
    <div id="container">
        <div id="video_box">
            <div class="camera_name">
                <!-- {{ clientData }} -->
                임시이름
            </div>
            <video ref="video" autoplay></video>
            <!-- @click.native는 하위컴포넌트가 지금 보고있는 컴포넌트의 method를 사용할 수 있게 해줌 -->
        </div>
    </div>
</template>

<script>
import { OpenVidu } from "openvidu-browser";
// const APPLICATION_SERVER_URL = "https://demos.openvidu.io/"
// const APPLICATION_SERVER_URL = process.env.VUE_APP_SPRING;
const APPLICATION_SERVER_URL = "http://localhost:5000/";

export default {
    data() {
        return {
            streamManager: Object,
            OV: undefined,
            session: undefined,
            mainStreamManager: undefined,
            publisher: undefined,
            subscribers: [],
            mySessionId: this.$route.params.roomId,
            myUserName: "dddd",
        }
    },
    mounted() {
    },
    computed: {
        clientData() {
            const { clientData } = this.getConnectionData();
            return clientData;
        }
    },
    methods: {
        init() {
            console.log("세션이름:" + this.$route.params.roomId);

            // OV 객체를 만들어요
            this.OV = new OpenVidu();

            // 세션 객체를 만들어요
            this.session = this.OV.initSession();
            this.session.on("streamCreated", ({ stream }) => {
                const subscriber = this.session.subscribe(stream);
                this.subscribers.push(subscriber);
            });
            this.session.on("streamDestroyed", ({ stream }) => {
                const index = this.subscribers.indexOf(stream.streamManager, 0);
                if (index >= 0) this.subscribers.splice(index, 1);
            });
            this.session.on("exception", ({ exception }) => {
                console.warn(exception);
            });
            console.log("와써여")
            this.getToken(this.mySessionId).then((token) => {
                console.log("토큰가지고왔어요",token, this.myUserName);
                this.session.connect(token, { clientData: this.myUserName })
                    .then(() => {
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
                        this.mainStreamManager = publisher;
                        this.publisher = publisher;

                        this.session.publish(this.publisher);
                        
                        this.streamManager = this.mainStreamManager;
                        console.log("여기까지 옴");
                        this.streamManager.addVideoElement(this.$refs.video);
                    })
                    .catch((error) => {
                        console.log("세션에 연결하는데 오류가 있어요:", error.code, error.message);
                    });
            });
            window.addEventListener("beforeunload", this.leaveSession);
        },

        getConnectionData() {
            const { connection } = this.streamManager.stream;
            return JSON.parse(connection.data);
        },

        leaveSession() {
            // --- 7) Leave the session by calling 'disconnect' method over the Session object ---
            if (this.session) this.session.disconnect();

            // Empty all properties...
            this.session = undefined;
            this.mainStreamManager = undefined;
            this.publisher = undefined;
            this.subscribers = [];
            this.OV = undefined;

            // Remove beforeunload listener
            window.removeEventListener("beforeunload", this.leaveSession);
        },
        updateMainVideoStreamManager(stream) {
            if (this.mainStreamManager === stream) return;
            this.mainStreamManager = stream;
        },
        async getToken(mySessionId) {
            console.log("토큰을 받아와 볼게요", mySessionId)
            const sessionId = await this.createSession(mySessionId);
            return await this.createToken(sessionId);
        },
        async createSession(sessionId) {
            console.log("여기?")
            const response = await this.axios.post(APPLICATION_SERVER_URL + 'api/sessions', { customSessionId: sessionId }, {
                headers: { 'Content-Type': 'application/json', },
            });
            console.log("여기능ㄴ?", response)
            return response.data; // The sessionId
        },
        async createToken(sessionId) {
            const response = await this.axios.post(APPLICATION_SERVER_URL + 'api/sessions/' + sessionId + '/connections', {}, {
                headers: { 'Content-Type': 'application/json', },
            });
            console.log("제발:", response.data)
            return response.data; // The token
        },
    },
    created() {
        console.log(process.env.VUE_APP_SPRING);
        this.init();
    },
}
</script>

<style lang="scss" scoped>
@import "@/assets/scss/_variable";

#container {
    position: absolute;
    top: 0;
    width: 100vw;
}

#video_box {
    width: 100%;
    height: 100%;
}

video {
    width: 100%;
    height: 100%;
}

.camera_name {
    position: absolute;
    color: white;
    top: 10px;
    left: 20px;
    background-color: rgb(0,0,0,0.5);
    padding: 4px;
    border-radius: 10px;
}
</style>