<template> 
  <!-- <div> -->
  <MenuView 
    v-if="menu" 
    @use-item="useItem"
    style="position:absolute; z-index: 100; bottom: 60px;"
  />
  <ItemModal 
    v-if="itemModal" 
    :usedItem="usedItem" 
    @item-yes="itemYes"
    @item-no="itemNo" 
    @on-modal="OnModal"
    style="position:absolute; z-index: 100; bottom: 60px;"
   />
  <OpenViduVue
    :my_cam_modal="my_cam_modal"
    :user="user"></OpenViduVue>
   
  <div id="chat_container" style="padding: 20px 0;" v-if="chat_modal">
        <div id="chat_header"
            style="display: flex; align-items:center; justify-content: space-between; margin-bottom: 10px;">
            <div>
                <img src="@/assets/main_logo2.png" alt="" style="width: 50px; height: 50px; margin-left: 20px;">
            </div>
            <div style="font-size: 30px">
                전언
            </div>
            <div style="font-size: 25px; margin-right: 20px;" @click="close_chat_modal">X</div>
        </div>
        <div v-for="(e, idx) in chat_log" :key="idx">
            <ChatCardVue :chat_dto="e" :my_nickname="user.nickname"></ChatCardVue>
        </div>
    </div>
  
  <MapView :user="user" :roomInfo="roomInfo" @on-caught="onCaught"/>
  <div style="position: absolute; bottom: 0; left: 0;">

    <!-- 아이템 사용 -->
    <!-- <div v-if="this.$store.state.itemModal"> -->
    <!-- </div> -->
    
    <div id="footer_container">
      <div class="menu_box flex_center" @click="open_chat_modal">
        <img class="menu" src="@/assets/game_chat.png">
      </div>
      <div>
        <input class="map_search" type="text" placeholder="채팅을 입력해 주세요" v-model="chat_data">
      </div>
      <div class="menu_box" @click="transmit_chat">
          <img src="@/assets/paper_plane.svg" alt="">
      </div>
      <div class="menu_box" @click="myCam">
        <img class="menu" src="@/assets/game_myCam.png">
      </div>
      <div class="menu_box" @click="onMenu">
        <img class="menu" src="@/assets/game_menu.png">
      </div>
    </div>
  </div>
  <SpiningModalVue @spinningEnd="spinningEnd" v-if="spinningModal"></SpiningModalVue>
  <RoleModalVue v-if="roleModal" @modalAllClose="modalAllClose"></RoleModalVue>

</template>

<script>
import OpenViduVue from '@/components/game/OpenViduVue.vue'
import MapView from '@/components/game/MapView.vue' // huh
import MenuView from '@/components/game/MenuView.vue'
import ItemModal from '@/components/game/ItemModal.vue'
import RoleModalVue from '@/components/game/RoleModalVue.vue'
import ChatCardVue from '@/components/game/ChatCardVue.vue';
const APPLICATION_SERVER_URL = process.env.VUE_APP_RTC;

import SpiningModalVue from '@/components/game/SpiningModalVue.vue'

export default {

  name: 'GameView',
  components: {
    MapView,
    OpenViduVue,
    MenuView,
    ItemModal,
    SpiningModalVue,
    RoleModalVue,
    ChatCardVue,
  },
  async created() {
    this.conn.addEventListener('message', (e)  => {
      var content = JSON.parse(e.data);
      if (content.type == 'chat') {
        console.log({ nickname: content.nickname, msg: content.message })
        this.chat_log.push({ nickname: content.nickname, msg: content.message })
      }
    })
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
          console.log("GameView room info loaded");
        }
      })
    const info = JSON.parse(sessionStorage.info);
    this.user.role = this.getMyRole(info.teamslave, info.teamchuno, this.user.nickname);
    this.user.caught = false;
    console.log("GameView created complete");
    console.log("-----------------------")
    console.log(this.user);
    console.log(this.roomInfo);
  },
  data() {
    return {
      my_cam_modal: { active: true },
      menu: false,
      itemModal: false,
      user: undefined,
      usedItem: [],
      spinningModal: true,
      roleModal: false,
      roomInfo: undefined,

      chat_modal: false,
      chat_data: "",
      chat_log:[],
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
      console.log('게임뷰임. 사용한 아이템표시')
      console.log(item)
      this.usedItem = item
      this.itemModal = true
      console.log(this.itemModal)
      console.log('아이템 사용')
    },
    itemYes(item) {
      this.itemModal = false
      //아이템 사용
      if(item.id == 1){
        // 천리안: 가장 가까운 추노꾼 위치 표시
        console.log(item)
      } else if (item.id == 2){
        // 위장: 추노꾼의 catch 범위 축소
        console.log(item)
      } else if (item.id == 3) {
        // 확실한 정보통: 진짜 노비 문서 위치 표시
        console.log(item)
        this.visibility = true
        setTimeout(this.visibility = false, 30000)
      } else if (item.id == 4) {
        // 먹물탄
        console.log(item)
        this.conn.send(JSON.stringify(
          {
            event: "useItem",
            level: 4,
            nickname:this.user.nickname,
            room: this.roomInfo.id,
            startData: {
              "isStart" : 1,
            }
          }
        ));
        // 30초 후에 제거
        setTimeout(
          this.conn.send(JSON.stringify(
            {
              event: "useItem",
              level: 4,
              nickname: this.user.nickname,
              room: this.roomInfo.id,
              startData: {
                "isStart" : 0,
              }
            }
          )
        ), 30000);
      } else if (item.id == 5){
        // 조명탄: 30초간 노비 위치 표시
        console.log(item)
      } else if (item.id == 6){
        // 긴 오랏줄: 노비 catch 범위 확대
        console.log(item)
      } else if (item.is == 7) {
        // 거짓 정보통
        console.log(item)
      } else {
        // 연막탄
        console.log(item);
      }
    },
    itemNo() {
      this.itemModal = false
    },
    onCaught(){
      this.user.caught = true
    },
    spinningEnd() {
      this.spinningModal = false;
      this.roleModal = true;
    },
    modalAllClose() {
      this.roleModal = false;
    },

    open_chat_modal() {
      this.chat_modal = !this.chat_modal;
    },
    close_chat_modal() {
      this.chat_modal = false;
    },
    transmit_chat() {
      this.conn.send(JSON.stringify({
        "event": "chat",
        "room": this.roomInfo.id,
        "nickname": this.user.nickname,
        "level": this.user.level,
        "msg": this.chat_data,
      }))
      this.chat_data = "";
    },
    clearChatData() {
      this.chat_data = "";
    }
  }
}

</script>

<style lang="scss" scoped>
@import "@/assets/scss/variable.scss";
$button_width: 50px;

#footer_container {
  position: absolute;
  bottom: 0;
  background-color: black;
  height: $footer_height;
  width: 100vw;
  display: grid;
  grid-template-columns: $button_width 1fr $button_width $button_width $button_width;
  justify-content: space-around;
  align-items: center;
}

#footer_container>div {
  width: 100%;
}

.menu-window {
  float: right;
  position: absolute;
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

#chat_container {
    z-index: 1010;
    position: absolute;
    bottom: $footer-height;
    background-image: url("@/assets/main_back_horizon.png");
    background-size: cover;
    width: 100vw;
    max-height: 60%;
    min-height: 200px;
    overflow-y: scroll;
}
</style>