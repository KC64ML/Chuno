<template>
  <div id="login">
    <div>
      <p>계정과 비밀번호 입력없이</p>
      <p>카카오톡으로 로그인 해 보세요.</p>
    </div>
    <br>
    <img src="@/assets/kakao_login.png" alt="kakao login">
  </div>
</template>

<script>
export default {
  name: 'LoginView',
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