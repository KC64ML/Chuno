<template>
    <div id="modal_container" @click="modal_close" v-if="menu_modal">
        <div id="waiting_room_modal">
            <div style="display: flex" class="menu_box" @click="info_on">
                <img class="modal_menu_image" src="@/assets/info_yanggak.svg" /> 방 정보
            </div>
            <div style="display: flex" class="menu_box" @click="notting" v-if="is_notified">
                <img class="modal_menu_image" src="@/assets/note_on.svg" />
                <div>알림끄기</div>
            </div>
            <div style="display: flex" class="menu_box" @click="notting" v-else>
                <img class="modal_menu_image" src="@/assets/note_off.svg" />
                <div>알림켜기</div>
            </div>
            <div style="display: flex" class="menu_box" @click="exiting_room">
                <img class="modal_menu_image" src="@/assets/exit_room.svg" />
                <div>나가기</div>
            </div>
        </div>
    </div>
    <HeaderVue :title="room_info.title"></HeaderVue>
    <div id="dot_menu" @click="dot_menu">
        <img src="@/assets/dot_menu.svg">
    </div>
    <div class="subscribers-container">
        <NicknameCardVue :sub="this.publisher_show"></NicknameCardVue>
        <div v-for="(sub, idx) in subscribers_show" :key="idx">
            <NicknameCardVue :sub="sub"></NicknameCardVue>
        </div>
    </div>
    {{ subscribers.length }}
    <div id="chat_log">
        <div v-for="(c, idx) in chat_log" :key="idx">
            {{ c.nickname }} : {{ c.msg }}
        </div>
    </div>
    <div id="waiting_room_footer">
        <div id="waiting_inner_footer">
            <div class="waiting_footer_input" style="height: 90%; flex: 1">
                <input id="input_el" v-model="chat_data">
            </div>
            <div style="display: flex; align-items: center" @click="transform_chat">
                <img src="@/assets/paper_plane.svg" alt="">
            </div>
            <div v-if="is_host" id="start_button" class="ready_start" @click="start_button">
                시작!
            </div>
            <div v-else id="ready_button" class="ready_start" @click="ready_button">
                준비!
            </div>
        </div>
    </div>
</template>

<script>
import HeaderVue from '@/components/HeaderVue.vue';
import NicknameCardVue from '@/components/waitingRoom/NicknameCardVue.vue'
import { OpenVidu } from "openvidu-browser";

const APPLICATION_SERVER_URL = process.env.VUE_APP_RTC;

export default {
    beforeRouteLeave(to, from, next) {
        this.leaveSession();
        next();
    },
    components: {
        HeaderVue,
        NicknameCardVue
    },
    data() {
        return {
            flag : false,

            menu_modal: false,
            token: sessionStorage.getItem("token"),
            user: undefined,
            room_id: this.$route.params.roomId,
            room_info: { title: "" },
            is_host: undefined,

            OV: undefined,
            session: undefined,
            publisher: undefined,
            subscribers: [],

            publisher_show: {
                isHost: false,
                isReady: false,
            },
            subscribers_show: [],

            is_notified: undefined,
            is_ready: false,

            chat_data: "",
            chat_log: [],
        }
    },
    async created() {
        // 내정보를 가저와요
        this.user = await this.axios.get(process.env.VUE_APP_SPRING + "user", { headers: { Authorization: this.token } }).then(res => res.data.result);
        console.log("현재 유저:", this.user);
        // 방정보도 가져와요
        this.room_info = await this.axios.get(process.env.VUE_APP_SPRING + "room/" + this.room_id).then(res => res.data.result);
        console.log("현재 방 정보:", this.room_info);
        // 내가 방장인지 확인해요
        console.log(this.user.id, this.room_info.host.id);
        if (this.user.id == this.room_info.host.id) this.is_host = true;

        this.OV = new OpenVidu();
        this.session = this.OV.initSession();
        this.session.on("streamCreated", ({ stream }) => {
            const subscriber = this.session.subscribe(stream);
            this.subscribers.push(subscriber);
            console.log("스트림을 발견했어요!")
            console.log("현재 사람은 " + this.subscribers.length + "명이에요")
            if ( this.flag == true) {
                this.session.signal({
                    data: JSON.stringify({
                        "nickname": this.user.nickname,
                        "msg": this.is_ready
                    }),
                    type: "welcome"
                })
            }
            const { connection } = subscriber.stream;
            console.log("커넥션 데이터에요:", connection.data)
            const { clientData } = JSON.parse(connection.data);
            const { clientLevel } = JSON.parse(connection.data);
            const { clientReady } = JSON.parse(connection.data);
            this.subscribers_show.push({
                "level": clientLevel,
                "nickname": clientData,
                "isHost": clientData == this.room_info.host.nickname,
                "isReady": clientReady,
            })
        });
        this.session.on("streamDestroyed", ({ stream }) => {
            const index = this.subscribers.indexOf(stream.streamManager, 0);
            if (index >= 0) {
                this.subscribers.splice(index, 1);
                this.subscribers_show.splice(index, 1);
            }
            console.log("누군가가 스트림을 종료했어요!")
            console.log("남은 사람은 " + this.subscribers.length + "명이에요");
        });
        this.session.on("exception", ({ exception }) => {
            console.warn("오류ㅠㅠ" + exception);
        });
        this.session.on("signal:welcome", (e) => {
            if (this.flag == false) {
                alert("tt")
                const fromdata = JSON.parse(e.data);
                console.log(fromdata)
                this.chat_log.push(fromdata);
                this.chat_data = "";
            }
        })
        this.session.on("signal:ready_button", (e) => {
            console.log(e.from.connectionId);
            var index;
            for (var i = 0; i < this.subscribers.length; i++) {
                if (e.from.connectionId == this.subscribers[i].stream.connection.connectionId) {
                    index = i;
                }
            }
            console.log(index)
        })
        this.session.on("signal:transform_chat", (event) => {
            const fromdata = JSON.parse(event.data);
            console.log(fromdata)
            this.chat_log.push(fromdata);
            this.chat_data = "";
        })

        await this.getToken(this.$route.params.roomId).then(async (token) => {
            await this.session.connect(token, { clientData: this.user.nickname, clientLevel: this.user.level, clientReady: this.is_ready }).then(() => {
                let publisher = this.OV.initPublisher(undefined, {
                    audioSource: undefined, // The source of audio. If undefined default microphone
                    videoSource: undefined, // The source of video. If undefined default webcam
                    publishAudio: false, // Whether you want to start publishing with your audio unmuted or not
                    publishVideo: false, // Whether you want to start publishing with your video enabled or not
                    resolution: "1x1", // The resolution of your video
                    frameRate: 5, // The frame rate of your video
                    insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
                    mirror: false, // Whether to mirror your local video or not
                });
                this.publisher = publisher;

                this.publisher_show = {
                    "level": this.user.level,
                    "nickname": this.user.nickname,
                    "isReady": false,
                    "isHost": this.user.nickname == this.room_info.host.nickname
                }
                this.session.publish(this.publisher);
            }).catch((error) => {
                console.log("퍼블리싱하는데 문제가 생겼어요ㅜ:", error.code, error.message);
            });
        });
        window.addEventListener("beforeunload", this.leaveSession);

        console.log("퍼블리싱을 완료했어요")
        this.flag = true;
        console.log(this.subscribers.length);

        // 아이디 키와, 방 키를 보내서 방 알람 설정을 했는지 아닌지 알아봐요 
        // console.log("방키:" + this.$route.params.roomId)
        // this.is_notified = true;
    },
    methods: {
        dot_menu() {
            this.menu_modal = true;
        },
        modal_close() {
            this.menu_modal = false;
        },
        info_on() {
            alert("정보");
        },
        notting() {
            // 방 아이디를 보내면 유저가 방에대해 한 알림설정의 상태를 바꿔주세요
            console.log("방키:" + this.$route.params.roomId)
            // 임시로 바꾼 정보를 받았다 칠게요
            this.is_notified = !this.is_notified;
            // 아마 카카오톡으로 메세지 보내기예약을 여기서 짤거 같아요
            /**
             * 카카오톡 메세지 보내기 코드
             */
        },
        exiting_room() {
            alert("나가기")
        },
        ready_button() {
            alert("준비 버튼");
            this.session.signal({
                data: this.user.nickname,
                type: "ready_button"
            })
            // this.$router.push("/game/" + this.$route.params.roomId);
        },
        start_button() {
            alert("스타트버튼");
        },
        leaveSession() {
            alert('페이지를 나가요');

            if (this.session) this.session.disconnect();

            this.session = undefined;
            this.mainStreamManager = undefined;
            this.publisher = undefined;
            this.subscribers = [];
            this.OV = undefined;

            window.removeEventListener("beforeunload", this.leaveSession);
        },
        async getToken(mySessionId) {
            console.log("getToken 시작")
            const sessionId = await this.createSession(mySessionId);
            return await this.createToken(sessionId);
        },
        async createSession(sessionId) {
            console.log("createSeesion 시작")
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
        transform_chat() {
            this.session.signal({
                data: JSON.stringify({
                    "nickname": this.user.nickname,
                    "msg": this.chat_data
                }),
                type: "transform_chat"
            })
        }
    }
}
</script>

<style lang="scss" scoped>
@import "@/assets/scss/variable";
$dot_right_position: 20px;
$ready_button_height: 50px;

#dot_menu {
    position: absolute;
    top: $header_margin;
    right: $dot_right_position;
    height: $header_height;
    display: flex;
    align-items: center;
}

#dot_menu>img {
    display: block;
    height: 60%
}

.subscribers-container {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    column-gap: calc(100vw * 0.02);
    position: absolute;
    top: $header_height + 20px;
}

#modal_container {
    position: absolute;
    top: 0;
    left: 0;
    background-color: rgb(0, 0, 0, 0.2);
    width: 100%;
    height: 100%;
    z-index: 2;
}

#waiting_room_modal {
    background-color: #F5F5F5;
    box-shadow: 0 5px 5px rgb(0, 0, 0, 0.4);
    padding: 10px;
    position: absolute;
    top: $header_height - 10px;
    right: $dot_right_position;
    z-index: 1;
}

.modal_menu_image {
    margin-right: 10px;
}

.menu_box {
    margin: 5px;
}

.menu_box:hover {
    background-color: #888888;
}

#waiting_room_footer {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100vw;
    height: $footer_height;
    background-color: black;
}

#waiting_inner_footer {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: calc(100vw - 20px);
    height: 80%;
    display: flex;
    align-items: center;
}

#waiting_inner_footer>div {
    margin: 0 5px
}

.ready_start {
    font-size: 25px;
    color: white;
}

#input_el {
    height: 100%;
    width: 100%;
    background-color: #848483;
    border-radius: $footer_height * 0.9 / 4;
    color: white;
    font-size: 20px;
    padding: 0 15px;
}
#chat_log {
    width: 100vw;
    position: absolute;
    bottom: $footer_height ;
    left: 0;
    max-height: 50%;
    font-size: 20px;
    overflow-y: scroll;
}
</style>