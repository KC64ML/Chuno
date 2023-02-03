<template>
  <div>
    <OpenViduVue style="z-index: 10000;" :my_cam_modal="my_cam_modal"></OpenViduVue>
    <MapView />
    <MenuView v-if="menu" />

    
    
    <div id="footer_container">
      <div class="menu_box flex_center" @click="this.$router.push('/home')">
        <img class="menu" src="@/assets/game_chat.png">
      </div>
      <div>
        <!-- 채팅창 수정 필요 -->
        <input class="map_search" type="text" placeholder="채팅을 입력해 주세요">
      </div>
      <div class="menu_box" @click="myCam">
        <img class="menu" src="@/assets/game_myCam.png">
      </div>
      <div class="menu_box" @click="onMenu">
        <img class="menu" src="@/assets/game_menu.png">
      </div>
    </div>
  </div>
</template>

<script>
import OpenViduVue from '@/components/game/OpenViduVue.vue'
import MapView from '@/components/game/MapView.vue'
import MenuView from '@/components/game/MenuView.vue'

export default {

  name: 'GameView',
  components: {
    MapView,
    OpenViduVue,
    MenuView,
  },
  data() {
    return {
      menu: true,
      my_cam_modal: true,
    }
  },
  methods: {
    onMenu() {
      console.log('menu clicked')
      this.menu = !this.menu
    },
    myCam() {
      this.my_cam_modal = !this.my_cam_modal
    },
  }
}

</script>

<style lang="scss" scoped>
@import "@/assets/scss/variable.scss";
$button_width: 60px;

#footer_container {
  position: absolute;
  bottom: 0;
  background-color: black;
  height: $footer_height;
  width: 100vw;
  display: grid;
  grid-template-columns: $button_width 1fr $button_width $button_width;
  justify-content: space-around;
  align-items: center;
}

#footer_container>div {
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
  display: block;
  margin: 0 auto;
  width: 95%;
  height: 40px;
  background-color: #F1F1F180;
  color: white;
  padding: 0 10px;
  border-radius: 15px;
  font-size: 20px;
}
.map_search::placeholder {
  color: rgba(255, 255, 255, 0.56)
}

</style>