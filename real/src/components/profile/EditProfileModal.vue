<template>
    <div id="modal_back">
        <div id="make_room_modal">
            <div id="close_button" @click="offing">x</div>
            <div id="modal_title" style="font-size: 24px;">호패 편집</div>
            <div>
                <div v-if="this.img_url">
                    <div id="profile_background">
                    <img
                        :src="this.img_url"
                        alt="profile pic"
                        class="uploadedImg"
                    />
                    </div>
                    <div>
                    <button @click="clearImage">다시 선택할래요</button>
                    </div>
                </div>
                <div v-else>
                    <img
                    id="blank_img"
                    src="@/assets/profile_default_with_cam.svg"
                    alt=""
                    @click="profile_click"
                    />
                </div>
                <input
                    ref="file_input"
                    type="file"
                    @change="oneFileSelect"
                    style="display: none"
                />
            </div>
                <table style="width: 300px; margin: 0 auto;">
                    <colgroup>
                        <col style="width: 90px">
                        <col style="width: 200px">
                    </colgroup>
                    <tr>
                        <td>닉네임</td>
                        <td>
                            <input v-model="title" style="padding: 0 20px">
                        </td>
                    </tr>
                        <td>전화번호</td>
                        <td>
                            <input v-model="title" style="padding: 0 20px">
                        </td>
                </table>
                <div class="flex_center hover_pointer" @click="nextButton1">
                    <img src="@/assets/main_button1.png" id="button1" style="width: 140px">
                    <div class="image_text">저장</div>
                </div>
            </div>
    </div>
</template>

<script>
export default {
    props: {
        player: undefined,
    },
    components: {
    },
    data() {
        return {
            title: "",
            max_player: 4,
            is_public: true,
            password: "",
            is_today: true,
            hour: null,
            minute: null,
            page1: true,
            page2: false,
            circleOptions: {
                strokeColor: "#0000FF",
                strokeOpacity: 0.3,
                strokeWeight: 2,
                fillColor: "#0000FF",
                fillOpacity: 0.15,
            },
            is_am: true,
            radius: 750
        }
    },
    methods: {
        offing() {
            this.$emit("modal_off")
        },
        nextButton1() {
            console.log(this.hour + (this.is_am ? 0 : 12), this.minute)
            if (this.title == "") {
                alert("제목을 입력해 주세요");
                return;
            }
            if (this.checkDate()) {
                alert("시간을 확인해 주세요");
                return;
            }
            this.page1 = false;
            this.page2 = true;
        },
        checkDate(){
            if (this.hour > 12 || this.hour <= 0 || this.minute < 0 || this.minute >= 60) {
                return true;
            }
            if (this.is_today) {
                let curDate = new Date();
                let h = curDate.getHours();
                let m = curDate.getMinutes();
                console.log("curDate",curDate);
                console.log("h",h);
                console.log("m",m);
                if(this.is_am){
                    if(this.hour<=h&&this.minute<m){
                        return true;
                    }
                }else{
                    if(this.hour+12<=h&&this.minute<m){
                        return true;
                    }
                }
            }
            return false;
        },
        minus() {
            if (this.max_player <= 4) return;
            this.max_player-=2;
        },
        plus() {
            if (this.max_player >= 8) return;
            this.max_player+=2;
        },
        publicGame() {
            this.is_public = true;
        },
        secretGame() {
            this.is_public = false;
        },
        today() {
            this.is_today = true;
        },
        amChange() {
            this.is_am = !this.is_am;
        },
        tomorrow() {
            this.is_today = false;
        },
        previous() {
            this.page1 = true;
            this.page2 = false;
        },
        async modalConfirm() {
            alert('게임방을 만들어요!!!')
            console.log(process.env.VUE_APP_SPRING + "room")
            console.log(sessionStorage.getItem("token"));
            // 방을 데이터 베이스에 등록해요
            console.log(process.env.VUE_APP_SPRING + "room");
            var data = await this.axios.post(process.env.VUE_APP_SPRING + "room", {
                lat: this.player.lat,
                lng: this.player.lng,
                title: this.title,
                isPublic: this.is_public,
                password: this.is_public ? null : this.password,
                radius: this.radius,
                isToday: this.is_today, 
                hour:  (this.hour + (this.is_am ? 0 : 12)) % 24,
                minute:  this.minute,
                maxPlayers: this.max_player,
            }, {
                headers: { Authorization: sessionStorage.getItem("token") }
            }).then(res => res.data.result)
            var user = await this.axios.get(process.env.VUE_APP_SPRING + "user", {headers: {Authorization: sessionStorage.getItem("token")}}).then(res => res.data.result);
            console.log(data);
            this.conn.send(JSON.stringify({
                "event": "make",
                "room": data,
                "nickname": user.nickname,
                "level": user.level,
            }));
            this.$router.push({ path: "/waitingRoom/" + data })
        }
    },
}
</script>

<style src="@vueform/slider/themes/default.css"></style>
<style lang="scss" scoped>
$input_height: 30px;
$plma_size: 30px;

#modal_back {
    position: absolute;
    width: 100vw;
    height: 100%;
    background: rgb(0, 0, 0, 0.6);
    z-index: 5;
}

#make_room_modal {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 85%;
    padding: 30px 0;
    background-color: #f2f2f2;
    transform: translate(-50%, -50%);
    font-size: 18px;
    border-radius: 10px;
    animation-name: modal_appearance;
    animation-iteration-count: 1;
    animation-duration: 0.8s;
}

#close_button {
    position: absolute;
    top: 10px;
    right: 20px;
    font-size: 20px;
}

#modal_title {
    text-align: center;
    margin-bottom: 20px;
}

input {
    height: $input_height;
    background-color: rgb(67, 64, 57, 0.3);
    border-radius: $plma_size / 2;
}

table {
    border-spacing: 0 20px;
}

td:nth-child(1) {
    text-align: center;
}

td:nth-child(2)>div {
    display: flex;
    align-items: center;
}
.hour_type {
    background-color: #141414;
    color: #f2f2f2;
    height: $plma_size;
    line-height: $plma_size;
    text-align: center;
    width: 40px;
    border-radius: $plma_size / 4;
    margin-right: 8px;
}
.time_input {
    width: 40px;
    text-align: center;
}

.plma_button {
    background-color: #141414;
    color: #f2f2f2;
    width: $plma_size;
    height: $plma_size;
    line-height: $plma_size;
    text-align: center;
    border-radius: $plma_size / 4;
}

.option_button {
    height: $plma_size;
    padding: 0 20px;
    line-height: $plma_size;
    color: white;
}

.option_left {
    border-top-left-radius: $plma_size / 4;
    border-bottom-left-radius: $plma_size / 4;
}

.option_right {
    border-top-right-radius: $plma_size / 4;
    border-bottom-right-radius: $plma_size / 4;
}

.selected {
    background-color: #141414;
}

.unselected {
    background-color: rgb(67, 64, 57, 0.3);
}

@keyframes modal_appearance {
    0% {transform: translate(-50%, -50%) scale(0);}
    70% {transform: translate(-50%, -50%) scale(1.2);}
    85% {transform: translate(-50%, -50%) scale(0.9);}
    95% {transform: translate(-50%, -50%) scale(1.05);}
}
</style>