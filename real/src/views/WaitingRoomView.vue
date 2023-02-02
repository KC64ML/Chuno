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
        <div v-for="(sub, idx) in subscribers" :key="idx">
            <NicknameCardVue :sub="sub"></NicknameCardVue>
        </div>
    </div>
    <div id="ready_button" @click="ready_button">
        준비하고 시작하기
    </div>
</template>

<script>
import HeaderVue from '@/components/HeaderVue.vue';
import NicknameCardVue from '@/components/waitingRoom/NicknameCardVue.vue'
// import { OpenVidu } from "openvidu-browser";

    export default {
        components: {
            HeaderVue,
            NicknameCardVue
        },
        data() {
            return {
                menu_modal: false,
                roomInfo: undefined,
                hostNickName: undefined,
                subscribers: [],
                is_notified: undefined,
            }
        },
        created() {
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
            // 이걸로 방장의 아이디키를 아니 방장 닉네임을 가져올게요
            console.log("방장아이디:" + this.roomInfo.host_id);
            // 방장 닉네임을 받아왔다 칠게요
            this.hostNickName = "asdf";

            // 오픈비두에서 subscribers들을 받아오는 소스로 부터 subscribers를 추출해요
            if (this.$store.state.nickname == undefined) {
                this.$store.state.nickname = "werwer";
            }
            var myNickname = this.$store.state.nickname
            var hostNickName = this.hostNickName
            this.subscribers = [
                {"level": 15, "nickname": myNickname, "isReady": false},
                {"level": 1, "nickname": hostNickName, "isReady": false},
                {"level": 5, "nickname": "conn3", "isReady": false},
                {"level": 77, "nickname": "conn4", "isReady": true},
                {"level": 43, "nickname": "conn5", "isReady": false}
            ]

            // 아이디 키와, 방 키를 보내서 방 알람 설정을 했는지 아닌지 알아봐요 
            console.log("방키:" + this.$route.params.roomId)
            this.is_notified = true;

            /*
            //여기 부터 오픈비두 소스로 사용할 거같아요
                // 세션 연결하고 자기 이름을 메세지로 모내서 누군지 그이름을 subscribers에 저장하면 될듯

            */
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
                this.$router.push("/game/" + this.$route.params.roomId);
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
    #ready_button {
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