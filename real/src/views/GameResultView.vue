<template>
    <HeaderVue :title="'결과'"></HeaderVue>
    <div id="container">
        {{ user_role }}<br>
        {{ user_win }}<br>{{ user_exp }}<br>{{ user_money }}<br>
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
            user_role: this.$route.params.role,
            user_win: this.$route.params.win,
            user_exp: this.$route.params.exp,
            user_money: this.$route.params.money
        }
    },
    async created() {
        this.user = await this.axios.get(process.env.VUE_APP_SPRING + 'user', {headers: { Authorization: sessionStorage.token }}).then(res => res.data.result);
        console.log(this.user.exp);
        // this.level_exp_mapping = await this.axios.get(process.env.VUE_APP_SPRING + 'level').then(res => res.data);
        this.level_mapper = [
			{"level": '1', "exp": "100"},
			{"level": '2', 'exp': '200'},
			{"level": '3', 'exp': '300'}
		]
        
    }
}
</script>

<style lang="scss" scoped>

</style>