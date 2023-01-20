<template>
  <div id="login">
    <div>
      <p>계정과 비밀번호 입력없이</p>
      <p>카카오톡으로 로그인 해 보세요.</p>
    </div>
    <br>
    <img src="@/assets/kakao_login.png" alt="kakao login" @click="login">

    <div>
      <span>로그인 여부</span>
      <input id="islogin" />
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'LoginView',
  data() {
    return {
      user_email: '',
      user_nickname: '',
    }
  },
  methods: {
    login() {
      alert("로그인")
      const REST_API_KEY = "9733352823239497d6928853e1e59954"
      const REDIRECT_URI = "http://localhost:8080/oauth"
      window.location.href=`https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code`
    },
    
    async start() {
      const token = sessionStorage.getItem("token");
      if (token != null) {
          var { data } = await axios.post("http://3.34.138.191:9997/kakao/tokenConfirm", token, {headers: {'Content-Type': 'text/plain'}});
          if (data.validation == "not_valid") {
              document.getElementById("islogin").value = "로그인X";
          } else {
              document.getElementById("islogin").value = data.nickname + "님 로그인중";
          }
      }
    },
  },
  created() {
    this.start()
  }
}
</script>

<style scoped>
#login {
  display: flex;
  flex-direction: column;
  align-items: center;
  /* width: 100vw;
  height: 100vh; */
}
p {
  /* font-weight: 700; */
}
img {
  padding-left: 10%;
  padding-right: 10%;
  max-width: 250px;
}
</style>