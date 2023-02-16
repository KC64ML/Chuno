<template>
    <HeaderVue :title="'결과'"></HeaderVue>
    <transition name="result_disappear">
        <div v-if="page_show" id="result_container">
            <div class="display_win_appear">
                <div class="display_win" :class="{ 'win_true': user_win }">
                    <img src="@/assets/leaf_crown.png" alt="" style="height: 100px" v-if="user_win == 1">
                    <div class="winDiv" v-if="user_win == 1">승리!</div>
                    <div class="winDiv" v-else>패배ㅠㅠ</div>
                </div>
                <div style="text-align: center; margin-bottom: 50px;">
                    <div v-if="user_win == 1">신난닭! 오늘 저녁은 치킨이닭!</div>
                    <div v-else>괜찮아.. 그런날도 있는거지 뭐..</div>
                </div>
            </div>
            <div class="bar_appear" style="display: flex; align-items: center; width: 100vw; margin-bottom: 30px;">
                <div style="width: 100%;">
                    <div class="total_bar exp_bar">
                        <div class="sub_bar exp_bar" ref="sub_bar">
                            <div>{{ Math.floor(display_exp) }}</div>
                        </div>
                    </div>
                    <div style="display: flex; justify-content: space-between; width: 95%; margin: 0 auto;">
                        <div>
                            <div>Lv.{{ user_level_from }}</div>
                            <div v-if="user_level_from != level_mapper.length">({{ user_level_from_exp }})</div>
                        </div>
                        <div>
                            <div>Lv.{{ user_level_to }}</div>
                            <div v-if="user_level_to != level_mapper.length">({{ user_level_to_exp }})</div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="obtain_box">
                <div style="width: 50%; text-align: center;">
                    <div style="width: 100%; text-align:center">경험치 {{ Math.floor(display_exp) }}</div>
                    <div style="display: flex; justify-content: center;">
                        <div>(+{{ user_exp }}</div>
                        <div class="up_arrow">▲</div>
                        <div>)</div>
                    </div>
                </div>
                <div style="width: 50%; text-align: center;">
                    <div style="width: 100%; text-align:center">내 엽전 {{ Math.floor(display_money) }}</div>
                    <div style="display: flex; justify-content: center;">
                        <div>(+{{ user_money }}</div>
                        <div class="up_arrow">▲</div>
                        <div>)</div>
                    </div>
                </div>
            </div>
            <div class="flex_center confirm_button" style="margin-top: 20px;" @click="game_end_confirm">
                <img src="@/assets/main_button1.png" style="height: 80px;">
                <div class="image_text">확인</div>
            </div>
        </div>
    </transition>
    <div id="level_up_container" v-if="level_up_modal" style="background-color: rgb(0,0,0,0.5); position: absolute; top: 0; left: 0; width: 100vw; height: 100%; z-index= 10011100">
        <div id="level_up_modal" class="level_up_modal">
            <div style="text-align: center; font-size: 25px;">축하합니다 레벨업 하셨습니다</div>
            <div style="text-align: center; font-size: 20px; margin: 20px 0;">Lv.{{ user_level_from }} -> Lv.{{ user_level_to }}</div>
            <div class="flex_center hover_pointer button_animation" @click="level_up_click">
                <img src="@/assets/main_button1.png" style="height: 60px;">
                <div class="image_text">좋아요!</div>
            </div>
        </div>
    </div>
</template>

<script>
import HeaderVue from '@/components/HeaderVue.vue';
export default {
    components: {
        HeaderVue,
    },
    data() {
        return {
            page_show: true,
            level_up_modal: false,

            user: {},
            level_mapper: {},
            user_level_from: 0,
            user_level_from_exp: 0,
            user_level_to: 3,
            user_level_to_exp: 0,
            roomId: this.$route.params.roomId,
            host: this.$route.params.host,
            user_role: this.$route.params.role,
            user_win: this.$route.params.win,
            user_exp: this.$route.params.exp,
            user_money: this.$route.params.money,
            display_exp: 0,
            display_money: 0,
            interv: null,
            interv2: null
        }
    },
    async created() {
        this.user = await this.axios.get(process.env.VUE_APP_SPRING + 'user', { headers: { Authorization: sessionStorage.token } }).then(res => res.data.result);
        // console.log(this.user.exp)
        this.user.exp = 800;
        this.display_exp = this.user.exp - this.user_exp;
        this.display_money = this.user.money - this.user_money;

        this.level_mapper = await this.axios.get(process.env.VUE_APP_SPRING + 'room/levelandexp').then(res => res.data);
        
        // this.level_mapper = [
        //     { "level": '0', "exp": "0" },
        //     { "level": '1', "exp": "810" },
        //     { "level": '2', 'exp': '900' },
        //     { "level": '3', 'exp': '2400' }
        // ]

        this.user_level_from = this.user.level;
        // console.log(this.level_mapper, this.user_level_from - 1)
        this.user_level_from_exp = this.level_mapper[this.user_level_from - 1].exp;

        var flag = false;
        for (var o of this.level_mapper) {
            if (parseInt(o.exp) > this.user.exp) {
                flag = true;
                this.user_level_to = o.level;
                this.user_level_to_exp = this.level_mapper[this.user_level_to].exp;
                // console.log(this.user_level_to_exp)
                break;
            }
        }
        if (flag == false) {
            this.user_level_to = this.level_mapper.length;
        }
        if (this.user_level_from != this.user_level_to) {
            this.axios.post(process.env.VUE_APP_SPRING + "updatelevel", this.user_level_to, {headers: {Authorization: sessionStorage.getItem("token")}}).then((res) => console.log("*****",res));
        }
        if (this.user_level_to == this.level_mapper.length) {
            this.$refs.sub_bar.style.width = "100%";
        } else {
            this.$refs.sub_bar.style.width = (this.display_exp - this.user_level_from_exp) / (this.user_level_to_exp - this.user_level_from_exp) * 100 + "%";
        }
        this.startInterval();
    },
    methods: {
        ci() {
            clearInterval(this.interv);
        },
        ci2() {
            clearInterval(this.interv2);
        },
        async startInterval() {
            // console.log(this.display_exp, this.user_level_to_exp)
            var exp_speed = this.user_exp / 200
            this.interv = setTimeout(() => {
                this.interv = setInterval(() => {
                    this.display_exp += exp_speed;
                    if (this.user_level_to == this.level_mapper.length) this.$refs.sub_bar.style.width = "100%"
                    else this.$refs.sub_bar.style.width = (this.display_exp - this.user_level_from_exp) / (this.user_level_to_exp - this.user_level_from_exp) * 100 + "%";
                    if (this.display_exp >= this.user.exp) this.ci();
                }, 1)
            }, 2000);
            var money_speed = this.user_money / 300;
            this.interv2 = setTimeout(() => {
                this.interv2 = setInterval(() => {
                    this.display_money += money_speed;
                    if (this.display_money >= this.user.money) this.ci2();
                }, 10)
            }, 2000);
            var main_interv = setInterval(() => {
                if(this.display_money >= this.user.money && this.display_exp >= this.user.exp) {
                    clearInterval(main_interv);
                    if(this.user_level_from != this.user_level_to) this.level_up_modal = true;
                }
            })
        },
        level_up_click() {
            this.level_up_modal = false;
        },
        game_end_confirm() {
            this.page_show = false;
            this.sendData({
                "event": "clear",
                "room": this.roomId,
                "nickname": this.host,
                "level": "1",
            });
            setTimeout(() => {
                this.$router.push({name: "home"});
            }, 1000)
        }
    }
}
</script>

<style lang="scss" scoped>
.up_arrow {
    animation-name: up_arrow;
    animation-duration: 1s;
    animation-iteration-count: infinite;
}

@keyframes up_arrow {
    0% {
        transform: translateY(50%);
    }
    100% {
        opacity: 0;
        transform: translateY(-50%);
    }
}

.winDiv {
    position: absolute;
    font-size: 30px;
    transform: translateY(-10px)
}

.total_bar {
    width: 90%;
    height: 30px;
    background-color: purple;
    margin: 10px auto;
    box-shadow: 0 10px 10px rgb(0, 0, 0, 0.5);
}

.sub_bar {
    width: 10%;
    background-color: white;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 1s;
}

.exp_bar {
    border-radius: 10px;
}

.display_win {
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 50px 0 20px;
}

.display_win_appear {
    animation-name: display_win_appear;
    animation-duration: 1s;
    animation-iteration-count: 1;
}

@keyframes display_win_appear {
    0% {
        transform: translateY(-80px);
        opacity: 0;
    }
}

.win_true {
    animation-name: display_win;
    animation-duration: 1.2s;
    animation-iteration-count: infinite;
}

@keyframes display_win {
    0% {
        transform: scale(1.2, 0.9)
    }

    50% {
        transform: scale(1.05) translateY(-20px);
    }

    100% {
        transform: scale(1.2, 0.9)
    }
}

.bar_appear {
    opacity: 0;
    animation-name: bar_appear;
    animation-duration: 1s;
    animation-delay: 0.5s;
    animation-iteration-count: 1;
    animation-fill-mode: forwards;
}

@keyframes bar_appear {
    0% {
        transform: translateY(-30px);
    }

    100% {
        opacity: 1;
    }
}

.obtain_box {
    display: flex;
    width: 100vw;
    align-items: center;
    font-size: 20px;
    margin-bottom: 40px;
    opacity: 0;
    animation-name: obtain_box;
    animation-duration: 1s;
    animation-delay: 1s;
    animation-iteration-count: 1;
    animation-fill-mode: forwards;
}

@keyframes obtain_box {
    0% {
        transform: translateY(-30px);
    }

    100% {
        opacity: 1;
    }
}

.confirm_button {
    opacity: 0;
    animation-name: confirm_button;
    animation-duration: 1s;
    animation-delay: 1.5s;
    animation-iteration-count: 1;
    animation-fill-mode: forwards;
}

@keyframes confirm_button {
    0% {
        transform: translateY(-30px);
    }

    100% {
        opacity: 1;
    }
}
#level_up_modal {
    background-color: #f2f2f2;
    padding: 20px;
    border-radius: 20px;
    width: 80%;
    position: absolute;
    top: 50%; left: 50%;
    transform: translate(-50%, -50%);
}
.level_up_modal {
    animation-name: level_up_modal_appear;
    animation-duration: 1s;
    animation-iteration-count: 1;
}
@keyframes level_up_modal_appear {
    0% {transform: translate(-50%, -50%) scale(0);}
    70% {transform: translate(-50%, -50%) scale(1.3);}
    85% {transform: translate(-50%, -50%) scale(0.8);}
    95% {transform: translate(-50%, -50%) scale(1.1);}
    
}

.result_disappear-leave-active {
    animation: result_out 1s;
}
@keyframes result_out {
    100% {
        opacity: 0;
        transform: scale(0);
    }
}
#result_container {
    height: 75%;
    width: 100vw;
    overflow-x: hidden;
    overflow-y:scroll;
}
</style>