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
    <HeaderVue :title="roomInfo.title"></HeaderVue>
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
    <div v-if="is_host" id="start_button" class="ready_start" @click="start_button">
        게임 시작하기!
    </div>
    <div v-else id="ready_button" class="ready_start" @click="ready_button">
        준비하고 시작하기
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
                OV: undefined,
                session: undefined,
                publisher: undefined,
                subscribers: [],
                user: undefined,

                is_host: undefined,
                publisher_show: {
                    isHost: false,
                    isReady: false,
                },
                subscribers_show: [],

                menu_modal: false,
                roomInfo: undefined,
                hostNickName: undefined,
                is_notified: undefined,
                is_ready: false,
            }
        },
        async created() {
            // 잠깐 닉네임 딴거 좀 쓸게요
            if (!this.$store.state.nickname) {
                this.$store.state.nickname = "ddddfff"
            }
            // 방장인지 구해요
            // this.is_host = this.$route.params.isHost
            this.is_host = false;

            // 내정보를 일단 가져와요
            // this.user = this.axios.get(process.env.VUE_APP_SPRING/user, '', {headers: {Authentication: sessionStorage.getItem("token")}}).then(res => {
            //     user = {
            //         "user_id": res.data.result.id,
            //         "level": res.data.result.level,
            //         "nickname": res.data.result.nickname,
            //     }
            // })
            this.user = {
                "user_id": 2,
                "level" : 11,
                "nickname" : this.$store.state.nickname,
            }
            // 이걸로 방 번호를 알게 되었으니까 이걸로 요청을 보내요
            console.log("방번호:" + this.$route.params.roomId);
            // 가상으로 응답을 받았다 칠게요
            this.roomInfo = {
                title: "방이름이에요",
                is_public: true,
                password: null,
                lat: 36.53534,
                lng: 126.25235,
                host_id: 3,
                radius: 500
            }
            // this.roomInfo = await this.axios.get(process.env.VUE_APP_SPRING + "room/join/" + this.$route.params.roomId).then(res => res.data.result);
            console.log("****", this.roomInfo)
            
            // 이걸로 방장의 아이디키를 아니 방장 닉네임을 가져올게요
            console.log("방장아이디:" + this.roomInfo.host_id);
            // 방장 닉네임을 받아왔다 칠게요
            this.hostNickName = "asdf";

            this.OV = new OpenVidu();
            this.session = this.OV.initSession();
            this.session.on("streamCreated", ({ stream }) => {
                const subscriber = this.session.subscribe(stream);
                this.subscribers.push(subscriber);
                console.log("스트림을 발견했어요!")
                console.log("현재 사람은 " + this.subscribers.length + "명이에요")
            
                const { connection } = subscriber.stream;
                console.log("커넥션 데이터에요:", connection.data)
                const { clientData } = JSON.parse(connection.data);
                const { clientLevel } = JSON.parse(connection.data);
                const { clientReady } = JSON.parse(connection.data);
                this.subscribers_show.push({
                    "level": clientLevel,
                    "nickname": clientData,
                    "isHost": this.is_host,
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

            await this.getToken(this.$route.params.roomId + "game").then(async (token) => {
                console.log("토큰을생성해요:" + token);
                await this.session.connect(token, { clientData: this.$store.state.nickname, clientLevel: this.user.level, clientReady: this.is_ready }).then(() => {
                    let publisher = this.OV.initPublisher(undefined, {
                        audioSource: undefined, // The source of audio. If undefined default microphone
                        videoSource: undefined, // The source of video. If undefined default webcam
                        publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
                        publishVideo: true, // Whether you want to start publishing with your video enabled or not
                        resolution: "80x60", // The resolution of your video
                        frameRate: 5, // The frame rate of your video
                        insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
                        mirror: false, // Whether to mirror your local video or not
                    });
                    this.publisher = publisher;

                    this.publisher_show = {
                        "level" : this.user.level,
                        "nickname" : this.user.nickname,
                        "isReady": false,
                        "isHost": this.is_host
                    }
                    this.session.publish(this.publisher);
                })
                .catch((error) => {
                    console.log("There was an error connecting to the session:", error.code, error.message);
                });
            });
            window.addEventListener("beforeunload", this.leaveSession);

            console.log("퍼블리싱을 완료했어요")
            console.log(this.subscribers.length);

            // 아이디 키와, 방 키를 보내서 방 알람 설정을 했는지 아닌지 알아봐요 
            console.log("방키:" + this.$route.params.roomId)
            this.is_notified = true;

            /*
            //여기 부터 오픈비두 소스로 사용할 거같아요
                // 세션 연결하고 자기 이름을 메세지로 모내서 누군지 그이름을 subscribers에 저장하면 될듯

            */
        },
        // watch: {
        //     $route(to, from) {
        //         if(to.path !== from.path) alert('ffffff');
        //     }
        // },
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
    #dot_menu > img {
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
        background-color: rgb(0,0,0,0.2);
        width: 100%;
        height: 100%;
        z-index: 2;
    }
    #waiting_room_modal {
        background-color: #F5F5F5;
        box-shadow: 0 5px 5px rgb(0,0,0,0.4);
        padding: 10px;
        position: absolute;
        top: $header_height - 10px;
        right: $dot_right_position;
        z-index:1;
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
    .ready_start {
        background-color: #f2f2f2;
        box-shadow: 0 10px 10px $shadow_color;
        height: $ready_button_height;
        line-height: $ready_button_height;
        padding: 0 30px;
        border-radius: $ready_button_height / 2;
        position: absolute;
        bottom: $footer_height + 20px;
    }
</style>