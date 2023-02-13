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
            user_role: this.$route.params.role,
            user_win: this.$route.params.win,
            user_exp: this.$route.params.exp,
            user_money: this.$route.params.money
        }
    },
    async created() {
        this.user = await this.axios.get(process.env.VUE_APP_SPRING + 'user', {headers: { Authorization: sessionStorage.token }}).then(res => res.data.result);
    }
}
</script>

<style lang="scss" scoped>

</style>