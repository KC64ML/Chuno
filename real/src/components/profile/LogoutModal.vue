<template>
  <div id="modal">
    <p>로그아웃 하시겠습니까?</p>
    <span class="container" @click="$emit('onLogout')">
      <img src="@/assets/main_button1.png" alt="button">
      <p>아니오</p>
    </span>
    <span class="container" @click="logout">
      <img src="@/assets/main_button1.png" alt="button">
      <p>예</p>
    </span>
  </div>
</template>

<script>

export default {
  name: 'LogoutModal',
  methods: {
    logout() {
      alert("로그아웃");
      sessionStorage.setItem("token", null)
      this.$emit('on-logout')
      //예제 파일은 html파일이라 새로고침을 하지만 vue는 data가 바인딩 되어있으므로 이 명령은 필요 없을거에요
      // location.reload();
      this.$router.push({ name: 'start'})
    },
  }
}
</script>

<style>
#modal {
  /* width: 150px; */
  background-color: #F5F5F5;
  border-radius: 10%;
}
</style>