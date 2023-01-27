<template>
  <div>
    <!-- 모달 -->
    <CreateRoomModal v-if="this.$store.state.createRoomModal" @createRoom="createRoom"/>
    <PasswordModal v-if="this.$store.state.passwordModal"/>
    <PushModal v-if="this.$store.state.pushModal"/>

    <!-- Home -->
    <div class="home">
      <nav id="header">
        <router-link to="/">
          <img class="logo" alt="Chuno Logo" src="@/assets/round_logo.svg">
        </router-link>
        <p>저잣거리</p>
      </nav>
      <input id="islogin">
      <p>내가 만든 방</p>
      <RoomListView/>
      <p>내 주변 방</p>

      <!-- 방 만들기 버튼 -->
      <img src="@/assets/createRoomBtn.svg" alt="createRoom" class="create" @click="createRoom">
    </div>
  </div>
</template>

<script>
import RoomListView from '@/components/home/RoomListView.vue'
import CreateRoomModal from '@/components/home/CreateRoomModal.vue'
import PasswordModal from '@/components/home/PasswordModal.vue'
import PushModal from '@/components/home/PushModal.vue'

export default {
  name: 'HomeView',
  components: {
    RoomListView,
    CreateRoomModal,
    PasswordModal,
    PushModal,
  },
  props: {
    rooms: Array,
  },
  data(){
    return {

    }
  },
  methods: {
    // 방 만들기 모달창 띄우기
    createRoom() {
      this.$store.state.createRoomModal = !this.$store.statecreateRoomModal
    },
  },
}
</script>

<style>
.create{
  position: fixed;
  bottom: 15%;
  right: 5%;
}
</style>