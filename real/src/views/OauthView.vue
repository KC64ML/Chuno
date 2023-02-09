<template>
  <div>
    <p>열심히 로그인 중이에요!</p>
  </div>
</template>

<script>
// import axios from 'axios'

export default {
  name: "OauthView",
  methods: {
    async start() {
      const code = new URL(window.location.href).searchParams.get("code");
      // const code = this.$route.params.code
      // console.log(code)
      // 아이피 주소 고쳐주세요
      var { data } = await this.axios.post(
        // process.env.VUE_APP_SPRING + "kakao/login",
        process.env.VUE_APP_SPRING + "kakao/login",
        code,
        { headers: { "Content-Type": "text/plain" } }
      );
      // console.log(data);
      // console.log(data.result);
      if (data.code == "no_email") {
        console.log(data.result, "네 번째 시도");
        // 아직 우리 서비스에 가입하지 않았어요 회원 가입 페이지로 넘어가요 뷰에서는 패러미터를 안고 라우터뷰를 이동하면 될거에요
        this.$router.push({name:"Register", params: {
          email: data.result,
        }})
        // window.location.href="register/" + data.email;
      } else if (data.code == "member") {
        // 우리 서비스의 멤버에요 세션스토리지에 토큰을 저장하고 홈페이지로 이동해요
        sessionStorage.setItem("token", data.result);
        this.$router.push({ name: "home" });
      }
    },
  },
  created() {
    this.start();
  },
};
</script>

<style>
</style>