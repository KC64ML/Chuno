<template>
  <div class="start">
    <img class="logo" alt="Vue logo" src="@/assets/logo.png">
    <br>
    <div class="container" @click="onStart">
      <p>시작하기</p>
      <img alt="Vue logo" src="@/assets/button_back1.png">
    </div>
    <hr>
  </div>
</template>

<script>
export default {
  name: 'StartView',
  // created() {
  //   this.getNav()
  // },
  // setup() {
  //   const data = reactive({
  //       nav : this.$store.state.nav
  //   });
  //   return {
  //     data
  //   },
  // },

  methods: {
    onStart() {
      this.$router.push({ name: 'Login' })
    },
    // getNav() {
    //   let navStatus = this.$store.sate.nav 
    //   this.$store.sate.nav != navStatus
    //   console.log(this.$store.sate.nav)
    // },
  }
}
</script>

<style>
.start{
  display: flex;
  flex-direction: column;
  align-items: center;
  /* justify-content: space-around; */
}
.start .logo {
  height: 30%;
  width: 30%;
  padding: 10%;
}
.container {
  position: relative;
  text-align: center;
  padding: 10;
}
.container p {
  position: absolute;
  top: 30%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  font-size: 24pt;
}
.container img{
  height: 130px;
}
</style>