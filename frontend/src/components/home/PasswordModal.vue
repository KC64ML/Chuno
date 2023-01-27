<template>
  <div class="modal-bg">
    <div id="modal">
      <div>
        <p>비밀번호</p>
        <p>X</p>
      </div>
      <input type="text" v-model="password">
      <div v-show="wrong">비밀번호가 틀렸습니다.</div>
      <div class="container" @click="onConfirm">
        <p>확인</p>
        <img alt="btn" src="@/assets/button_back1.svg">
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PasswordModal',
  data() {
    return {
      password: '',
      wrong: false,
    }
  },
  methods: {
    // 비밀번호 일치 확인 코드 바꾸기
    onConfirm() {
      if (this.password.length == this.password ) {
        // this.wrong = false
        this.$router.push({ name: 'WaitingRoom', params: { room_num: 1 }})
      } else {
        this.wrong = true
      }
    }
  }
}
</script>

<style>

</style>