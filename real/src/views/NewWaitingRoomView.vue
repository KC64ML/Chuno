<template>
<RoomInfoModal :roomInfo="room_info" v-if="infoShow" @info_close="closeInfo"></RoomInfoModal>
    <div id="modal_container" @click="modal_close" v-if="menu_modal">
        <div id="waiting_room_modal">
            <div style="display: flex" class="menu_box" @click="info_on">
                <img class="modal_menu_image" src="@/assets/info_yanggak.svg" />
                <div>방 정보</div>
            </div>
            <div style="display: flex" class="menu_box" @click="notting" v-if="isPushed">
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
        <div v-for="(sub, idx) in subscribers" :key="idx">
            <NicknameCardVue :sub="sub"></NicknameCardVue>
        </div>
    </div>
    <div id="chat_log">
        <div v-for="(c, idx) in chat_log" :key="idx">
            {{ c.nickname }} : {{ c.msg }}
        </div>
    </div>
    <div id="waiting_room_footer">
        <div id="waiting_inner_footer">
            <div class="waiting_footer_input" style="height: 90%; flex: 1">
                <input id="input_el" v-model="chat_data" @keyup.enter="transform_chat">
            </div>
            <div style="display: flex; align-items: center" @click="transform_chat">
                <img src="@/assets/arrow.svg" alt="">
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
import RoomInfoModal from '@/components/home/RoomInfoModal.vue'

export default {
    beforeRouteLeave(to, from, next) {
        if (to.name != "game") this.leave_room();
        next();
    },
    components: {
        HeaderVue,
        NicknameCardVue,
        RoomInfoModal
    },
    data() {
        return {
            menu_modal: false,
            // 이건 수정해야 될 가능성 큼
            isPushed: false,
            infoShow:false,

            is_host: false,
            room_id: this.$route.params.roomId,
            token: sessionStorage.getItem("token"),

            room_info: { title: "" },
            user: undefined,
            subscribers: undefined,

            chat_data: "",
            chat_log: [],
        }
    },
    async created() {
        // 방정보를 받아올게요
        this.room_info = await this.axios.get(process.env.VUE_APP_SPRING + "room/" + this.room_id, {
                headers: { Authorization: sessionStorage.getItem("token") },
            })
      .then((res) => res.data.result);
        // console.log("현재 방 정보:", this.room_info);
        this.isPushed = this.room_info.isPushed;
        // console.log("현재 방 알림:", this.isPushed);
        // console.log("현재 방 아이디:", this.room_info.id);
        // 내정보를 가저와요
        this.user = await this.axios.get(process.env.VUE_APP_SPRING + "user", { headers: { Authorization: this.token } }).then(res => res.data.result);
        // 방장인지 알아봐요
        this.is_host = (this.room_info.host.nickname == this.user.nickname);
        // 이벤트도 등록하면서 방안에 유저정보를 가저와요
        this.enrollEvent();
    },
    methods: {
        enrollEvent() {
            new Promise((resolve) => {
                this.conn.onmessage = async (e) => {
                    var content = JSON.parse(e.data);
                    // console.log("소켓에서 온 메세지", content)
                    if (content.type == 'already') {
                        // console.log("현재있는사람", content.players);
                        this.subscribers = content.players
                    } else if (content.type == 'me') {
                        // console.log(content.present);
                        this.subscribers = content.players;
                    } else if (content.type == 'chat') {
                        // console.log(content);
                        this.chat_log.push({ nickname: content.nickname, msg: content.message });
                    } else if (content.type == 'startGame') {
                        // console.log("게임을 위한 정보 : ", content.info);
                        sessionStorage.setItem("info", JSON.stringify(content.info));
                        this.$router.push({ path: "/game/" + this.room_id})
                    } else if (content.type == 'error') {
                        // console.log(content.msg);
                    }
                }
                resolve();
            }).then(() => {
                this.init();
            })
        },
        init() {
            this.sendData({
                "event": "getAllUserInRoom", // rooms와 연결되어있음
                "room": this.room_id,
            });
        },
        dot_menu() {
            this.menu_modal = true;
        },
        modal_close() {
            this.menu_modal = false;
        },
        info_on() {
            this.infoShow = true;
            // e.stopPropagation();
        },
        closeInfo(){
            this.infoShow = false;
        },
        notting() {
            // 방 아이디를 보내면 유저가 방에대해 한 알림설정의 상태를 바꿔주세요
            // console.log("방키:" + this.$route.params.roomId)
            if (!this.isPushed) {
                this.axios
                .post(
                    process.env.VUE_APP_SPRING + "room/push/" + this.room_info.id,
                    "",
                    {
                    headers: { Authorization: sessionStorage.getItem("token") },
                    }
                )
                .then(({ data }) => {
                    if (data.code == 1) {
                    this.isPushed = true;
                    // console.log("예약 성공");
                    } else {
                    alert("예약에 실패했습니다. 오류코드 : " + data.code);
                    }
                });
            } else {
                this.axios
                .delete(
                    process.env.VUE_APP_SPRING + "room/push/" + this.room_info.id,
                    {
                    headers: { Authorization: sessionStorage.getItem("token") },
                    }
                )
                .then(({ data }) => {
                    if (data.code == 1) {
                        // console.log("예약취소 성공");
                    this.isPushed = false;
                    } else {
                    alert("예약 취소에 실패했습니다. 오류코드 : " + data.code);
                    }
                });
            }
            // e.stopPropagation();
        },
        exiting_room(e) {
            this.$router.push({ name: 'home' })
            e.stopPropagation();
        },
        leave_room() {
            this.sendData({
                "event": "leave",
                "room": this.room_id,
                "nickname": this.user.nickname,
                "level": this.user.level
            })
        },
        ready_button() {
            this.sendData({
                "event": "ready",
                "room": this.room_info.id,
                "nickname": this.user.nickname,
                "level": this.user.level,
            })

        },
        async start_button() {
            for (var s of this.subscribers) {
                if (s.host || s.ready) continue;
                alert("모두가 준비된 상태여야 합니다.");
                return;
            }
            var roomId = this.room_info.id;
            var userNickNameList = this.subscribers.map((e) => e.nickname)

            // console.log(roomId, userNickNameList);

            // 데이터를 받아오는 코드에요
            var data = await this.axios.post(process.env.VUE_APP_SPRING + "room/startRoom", {
                "roomId": roomId,
                "userNickNameList": userNickNameList,
            }).then(res => res.data)

            var user_len = data.roomDecideChunoOrSlaveList.length;
            var team_slave = data.roomDecideChunoOrSlaveList.slice(0, user_len / 2);
            var team_chuno = data.roomDecideChunoOrSlaveList.slice(user_len/2, user_len);
            // console.log(data.roomStartDto.lat)
            this.sendData({
                "event": "startGame",
                "room": roomId,
                "startData": {
                    "radius": data.roomStartDto.radius,
                    "roomlat": data.roomStartDto.lat,
                    "roomlng": data.roomStartDto.lng,
                    "slavepaper": data.roomSlaveDocumentList,
                    "teamslave": team_slave,
                    "teamchuno": team_chuno,
                }
            });
            // console.log(data.roomDecideChunoOrSlaveList.slice(0,))
            // var user_len = data.roomDecideChunoOrSlaveList.length();
            // var team_slave = data.roomDecideChunoOrSlaveList.slice(0,user_len / 2);
            // var team_chuno = data.roomDecideChunoOrSlaveList.slice(user_len / 2, user_len);
            
            // console.log(team_slave)
            // console.log(team_chuno)
            // this.conn.send(JSON.stringify({
            //     "event": "startGame",
            //     "startData": JSON.stringify(data)
            // }))
        },
        transform_chat() {
            this.sendData({
                "event": "chat",
                "room": this.room_info.id,
                "nickname": this.user.nickname,
                "level": this.user.level,
                "msg": this.chat_data
            })
            this.chat_data = "";
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
    border-radius: 10px;
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
    bottom: $footer_height + 20px;
    left: 0;
    max-height: 50%;
    font-size: 20px;
    overflow-y: scroll;
    word-break: break-all;
}
#chat_log > div {
    padding: 10px 20px;
}
</style>
