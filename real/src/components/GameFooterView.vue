<template>
  <div id="container">
      <div class="menu_box" @click="this.$router.push('/home')">
          <img class="menu" src="@/assets/game_chat.png">
      </div>
      <div>
        <!-- 채팅창 수정 필요 -->
        <input type="text">
      </div>
      <div class="menu_box" @click="this.$router.push('/home')">
          <img class="menu" src="@/assets/game_myCam.png">
      </div>
      <div class="menu_box" @click="this.$router.push('/home')">
          <img class="menu" src="@/assets/game_menu.png">
      </div>
      
  </div>
</template>

<script>
  export default {

  }
</script>

<style lang="scss" scoped>
@import "@/assets/scss/variable.scss";

  #container {
      background-color: black;
      height: $footer_height;
      width: 100vw;
      display: flex;
      justify-content: space-around;
      align-items: center;
  }
  // .menu {
  //     height: 25px;
  // }
  .menu_box {
      width: 20%;
      display: flex;
      flex-direction: column;
      align-items: center;
  }
</style>