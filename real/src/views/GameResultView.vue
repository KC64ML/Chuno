<template>
    <HeaderVue :title="'결과'"></HeaderVue>
    <div id="container">
        {{ user }} {{ user_role }}
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
        }
    },
    async created() {
        this.user = await this.axios.get(process.env.VUE_APP_SPRING + 'user', {headers: { Authorization: sessionStorage.token }}).then(res => res.data.result);
    }
}
</script>

<style lang="scss" scoped>

</style>