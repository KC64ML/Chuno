<template>
    <HeaderVue :title="'결과'"></HeaderVue>
    <div id="container">
        <div v-if="user_win">
            승리!
            <div v-if="user_role == 'runner'" style="display: flex;">
                <div>노비 점수</div><div class="up_arrow">▲</div>
            </div>
            <div v-else style="display: flex;">
                <div>추노 점수</div><div class="up_arrow">▲</div>
            </div>
        </div>
        <div v-else>패배!</div>
        원래레벨{{ user_level_from }} 지금레벨 {{ user_level_to }}<br>
        원래경험치{{ user.exp - user_exp }} 현재경험치 {{ user.exp }}<br>
        원래돈{{ user.money - user_money }} 현재돈{{ user.money }}

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
            user_level_to: 0,
            user_role: this.$route.params.role,
            user_win: this.$route.params.win,
            user_exp: this.$route.params.exp,
            user_money: this.$route.params.money
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
</style>