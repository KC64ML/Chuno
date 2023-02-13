<template>
    <HeaderVue :title="'결과'"></HeaderVue>    

    <!-- <div id="container">
        원래레벨{{ user_level_from }} 지금레벨 {{ user_level_to }}<br>
        원래경험치{{ user.exp - user_exp }} 현재경험치 {{ user.exp }}<br>
        원래돈{{ user.money - user_money }} 현재돈{{ user.money }}

    </div> -->
    <div style="height: 75%; width: 100vw; overflow-x: hidden; overflow-y:scroll; border: dashed">
        <!-- <div style="text-align: center; margin-top: 30px;">
            <img src="@/assets/main_logo2.png">
        </div> -->
        <div class="display_win_appear">
            <div class="display_win" :class="{'win_true': user_win}">
                <img src="@/assets/leaf_crown.png" alt="" style="height: 100px">
                <div class="winDiv" v-if="user_win == 1">승리!</div>
                <div class="winDiv" v-else>패배ㅠㅠ</div>
            </div>
            <div style="text-align: center; margin-bottom: 80px;">
                <div v-if="user_win">신난닭! 오늘 저녁은 치킨이닭!</div>
                <div v-else>괜찮아.. 그런날도 있는거지 뭐..</div>
            </div>
        </div>
        <div style="display: flex; align-items: center; width: 100vw; margin-bottom: 60px;">
            <div style="width: 100%;">
                <div class="total_bar exp_bar">
                    <div class="sub_bar exp_bar" ref="sub_bar">
                        <div>{{ display_exp }}</div>
                    </div>
                </div>
                <div style="display: flex; justify-content: space-between; width: 95%; margin: 0 auto;">
                    <div>
                        <div>Lv.{{ user_level_from }}</div>
                        <div v-if="user_level_from != level_mapper.length">({{ user_level_from_exp }})</div>
                    </div>
                    <div>
                        <div>Lv.{{ user_level_to }}</div>
                        <div v-if="user_level_to != level_mapper.length">({{ level_mapper[user_level_to] }})</div>
                    </div>
                </div>
            </div>
        </div>
        <div style="display: flex; justify-content: space-evenly; align-items: center; font-size: 20px;">
            <!-- 원래돈{{ user.money - user_money }} 현재돈{{ user.money }} -->
            <div style="width: 20%; text-align: center;">
                <!-- 원래경험치{{ user.exp - user_exp }} 현재경험치 {{ user.exp }}<br> -->
                
                <div>경험치 {{ display_exp }}</div>
                <div style="display: flex; justify-content: center;">
                    <div>(+{{ user_exp }}</div>
                    <div class="up_arrow">▲</div>
                    <div>)</div>
                </div>
            </div>
            <div>
                <div>내 엽전 {{ display_money }}</div>
                <div style="display: flex; justify-content: center;">
                    <div>(+{{ user_money }}</div>
                    <div class="up_arrow">▲</div>
                    <div>)</div>
                </div>
                
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
            user: {},
            level_mapper: {},
            user_level_from: 0,
            user_level_from_exp: "",
            user_level_to: 0,
            user_level_to_exp: "",
            user_role: this.$route.params.role,
            user_win: this.$route.params.win,
            user_exp: this.$route.params.exp,
            user_money: this.$route.params.money,
            display_exp: 0,
            display_money: 0,
        }
    },
    async created() {
        this.user = await this.axios.get(process.env.VUE_APP_SPRING + 'user', {headers: { Authorization: sessionStorage.token }}).then(res => res.data.result);
        // this.level_exp_mapping = await this.axios.get(process.env.VUE_APP_SPRING + 'level').then(res => res.data);
        this.level_mapper = [
            {"level": '0', "exp": "0"},
			{"level": '1', "exp": "100"},
			{"level": '2', 'exp': '200'},
			{"level": '3', 'exp': '300'}
		]
        this.user_level_from = this.user.level;
        this.user_level_to = this.user.level;
        if (this.level_mapper.length == this.user_level_from) {
            console.log("이미 만렙임");
        } else if (parseInt(this.level_mapper[this.user_level_from].exp) <= this.user.exp) {
            var flag = false;
            for(var ex = this.user_level_from + 1; ex < this.level_mapper.length; ex++) {
                if (this.level_mapper[ex].exp > this.user.exp) {
                    console.log("레벨업");
                    this.user_level_to = ex;
                    flag = true;
                    break;
                    // this.axios.post(process.env.VUE_APP_SPRING + "levelUp", {
                    //     "id": this.user.id,
                    //     "level": this.user_level_to,
                    // })
                }
            }
            if (flag == false) {
                console.log("만렙이 되신걸 축하해요")
                this.user_level_to = this.level_mapper.length;
                // this.axios.post(process.env.VUE_APP_SPRING + "levelUp", {
                //     "id": this.user.id,
                //     "level": this.user_level_to,
                // })
            }
        } else {
            console.log("레벨업 안됨")
        }
        if (this.user_level_from != this.level_mapper.length) this.user_level_from_exp = this.level_mapper[this.user_level_from - 1].exp;
        else this.user_level_from_exp = "";
        if (this.user_level_to != this.level_mapper.length) this.user_level_to_exp = this.level_mapper[this.user_level_to].exp
        else this.user_level_to_exp = "";

        this.display_exp = this.user.exp - this.user_exp;
        this.display_money = this.user.money - this.user_money;
    },
    mounted() {
        console.log(this.$refs.sub_bar.style)
        this.$refs.sub_bar.style.width = "50" + "%";
        
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
    box-shadow: 0 10px 10px rgb(0,0,0, 0.5);
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
    display:flex;
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
.win_true{
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
</style>