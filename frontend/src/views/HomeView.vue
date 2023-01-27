<template>
  <CreateRoomModal v-if="modal" @createRoom="createRoom"/>
  <div class="home">
    <nav id="header">
      <router-link to="/">
        <img class="logo" alt="Chuno Logo" src="@/assets/round_logo.svg">
      </router-link>
      <p>저잣거리</p>
    </nav>
    <input id="islogin">
    <RoomListView/>
    
    <img src="@/assets/createRoomBtn.svg" alt="createRoom" class="create" @click="createRoom">
  </div>
</template>

<script>
import axios from 'axios'
import RoomListView from '@/components/home/RoomListView.vue'
import CreateRoomModal from '@/components/home/CreateRoomModal.vue'

export default {
  name: 'HomeView',
  components: {
    RoomListView,
    CreateRoomModal,
  },
  props: {
    rooms: {
      type: Array,
    },
  },
  data(){
    return {
      modal: false,
    }
  },
  methods: {
    async start() {
      const token = sessionStorage.getItem("token");
      if (token != null) {
          var { data } = await axios.post("http://3.34.138.191:9997/kakao/tokenConfirm", token, {headers: {'Content-Type': 'text/plain'}});
          if (data.validation == "not_valid") {
              document.getElementById("islogin").value = "로그인X";
          } else {
              document.getElementById("islogin").value = data.nickname + "님 로그인중";
          }
      }
    },
    createRoom() {
      this.modal = !this.modal
    },
  },
  created() {
    this.start()
  },
  
}
</script>
<style>
.create{
  position: fixed;
  bottom: 15%;
  /* left: 10%; */
  right: 5%;
}
</style>