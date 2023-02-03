<template>
  <div id="container">
      <div class="menu_box flex_center" @click="this.$router.push('/home')">
          <img class="menu" src="@/assets/game_chat.png">
      </div>
      <div>
        <!-- 채팅창 수정 필요 -->
        <input class="map_search" type="text">
      </div>
      <div class="menu_box" @click="myCam">
          <img class="menu" src="@/assets/game_myCam.png">
      </div>
      <div class="menu_box" @click="gameMenu">
          <img class="menu" src="@/assets/game_menu.png">
      </div>
      
  </div>
</template>

<script>
  export default {
    methods: {
      myCam() {
        alert('myCam눌림')
      },
      gameMenu() {
        alert("메뉴 눌림")
      }
    }
  }
</script>

<style lang="scss" scoped>
@import "@/assets/scss/variable.scss";
$button_width: 60px;

  #container {
      background-color: black;
      height: $footer_height;
      width: 100vw;
      display: grid;
      grid-template-columns: $button_width 1fr $button_width $button_width;
      justify-content: space-around;
      align-items: center;
  }
  #container > div {
    width: 100%;
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
  .map_search {
    width: 100%;
    height: 40px;
  }
</style>