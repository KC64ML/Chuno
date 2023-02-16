<template>
    <div id="container" class="sliding_down_appearance">
        <img src="@/assets/main_logo2.png" class="block_center" style="width: 120px">
        <div style="margin-top: 60px"></div>
        <div>계정과 비밀번호 입력 없이</div>
        <div>카카오 톡으로 로그인 해 보세요</div>
        <div style="margin-top: 60px"></div>
        <img src="@/assets/kakao_login_button.png" class="block_center hover_pointer" style="width: 300px" @click="loginGo">
    </div>
</template>

<script>
    export default {
        methods: {
            loginGo() {
                var REST_API_KEY = "9733352823239497d6928853e1e59954";
                var REDIRECT_URI = process.env.VUE_APP_HERE + "oauth";
                window.location.href = `https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code`;
            },
        }
    }
</script>

<style lang="scss" scoped>
    #container {
        text-align: center;
    }
</style>