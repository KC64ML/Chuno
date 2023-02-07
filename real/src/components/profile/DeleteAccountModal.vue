<template>
  <div id="modal">
    <p>탈퇴하시겠습니까?</p>
    <span class="container" @click="$emit('on-delete')">
      <img src="@/assets/main_button1.png" alt="button">
      <p>아니오</p>
    </span>
    <span class="container" @click="del">
      <img src="@/assets/main_button1.png" alt="button">
      <p>예</p>
    </span>
  </div>
</template>

<script>
export default {
  name: 'DeleteAccountModal',
  methods: {
    async del() {
      alert("탈퇴?");
      // var del_user_email = document.getElementById("del_user").value;
      const token = sessionStorage.token
      await this.axios.delete(process.env.VUE_APP_SPRING + 'user', { headers: { Authorization: token } })
      console.log("탈퇴완료");
      this.$emit('on-delete')
      // this.logout();
      this.$router.push({ name: 'start'})
    },
  }
}
</script>

<style scoped>
#modal {
  /* width: 150px; */
  background-color: #F5F5F5;
  border-radius: 10%;
}
img {
  width: 150px;
}

.container p {
  /* position: absolute; */
  /* top: 0; */
  /* left: 50%; */
  /* transform: translate(-50%, -50%); */
  color: #F5F5F5;
  font-size: 18pt;
  margin: 0;
}
/* .container img{
  height: 80px;
} */
</style>