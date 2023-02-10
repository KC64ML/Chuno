<template>
  <div>
  <MenuView 
    v-if="menu" 
    @use-item="useItem"
    style="position:absolute; bottom: 60px;"
  />
  <ItemModal 
    v-if="itemModal" 
    :usedItem="usedItem" 
    @item-yes="itemYes"
    @item-no="itemNo"
    style="position:absolute; bottom: 60px;"/>
  <div>
    
    <OpenViduVue
    :my_cam_modal="my_cam_modal"
    :user="user"></OpenViduVue>
    <MapView :user="user" :roomInfo="roomInfo" />

    <!-- 아이템 사용 -->
    <!-- <div v-if="this.$store.state.itemModal"> -->
    <!-- </div> -->
    
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
</div>
</template>

<script>
import OpenViduVue from '@/components/game/OpenViduVue.vue'
import MapView from '@/components/game/MapView.vue'
import MenuView from '@/components/game/MenuView.vue'
import ItemModal from '@/components/game/ItemModal.vue'
const APPLICATION_SERVER_URL = process.env.VUE_APP_RTC;

export default {

  name: 'GameView',
  components: {
    MapView,
    OpenViduVue,
    MenuView,
    ItemModal,
  },
  async created() {
    await this.axios.get(APPLICATION_SERVER_URL + 'user',
          {
              headers: { Authorization: sessionStorage.token }
          }).then(({ data }) => {
              if (data.code) {
                  this.user = data.result;
              }
          });
    await this.axios.get(APPLICATION_SERVER_URL + 'room/' + this.$route.params.roomId)
      .then(({ data }) => {
        if (data.code) {
          this.roomInfo = data.result;
        }
      })
    const info = JSON.parse(sessionStorage.info);
    this.user.role = this.getMyRole(info.teamslave, info.teamchuno, this.user.nickname);
  },
  data() {
    return {
      my_cam_modal: {active: false},
      menu: false,
      itemModal: false,
      user: undefined,
      usedItem: [],
      roomInfo: undefined,
    }
  },
  methods: {
    onMenu() {
      console.log('menu clicked')
      this.menu = !this.menu
      console.log(this.menu)
    },
    getMyRole(teamslave, teamchuno, nickname) {
      for (let i = 0; i < teamslave.length; i++) {
        if (teamslave[i].nickname == nickname) {
          return "runner";
        }
      }
      for (let i = 0; i < teamchuno.length; i++) {
        if (teamchuno[i].nickname == nickname) {
          return "chaser";
        }
      }
    },
    myCam() {
      this.my_cam_modal.active = !this.my_cam_modal.active
      console.log("mycam 눌림");
    },
    useItem(item){
      this.usedItem = item
      this.itemModal = true
      console.log('아이템 사용')
      console.log(item)
    },
    itemYes(item){
      this.itemModal = false
      //아이템 사용
      if(item.id == 1){
        // 천리안
        console.log(item)
      } else if (item.id == 2){
        // 위장
        console.log(item)
        
      } else if (item.id == 3) {
        // 확실한 정보통: 30초간 노비 위치 표시
        console.log(item)
        this.visibility = true
        setTimeout(this.visibility=true, 30000)
        
      } else if (item.id == 4){
        // 먹물탄
        console.log(item)
      } else if (item.id == 5){
        // 조명탄
        console.log(item)
      } else if (item.id == 6){
        // 긴 오랏줄
        console.log(item)
      } else {
        // 연막탄
        console.log(item)
      }
      

    },
    itemNo(){
      this.itemModal = false
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

.menu-window {
  float: right;
  position:absolute; 
  bottom: 60px;
}
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