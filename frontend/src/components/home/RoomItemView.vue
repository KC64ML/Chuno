<template>
  <div class="room">
    <div class="room_title">
      <img src="@/assets/private.svg" alt="private room">
      <!-- <img src="@/assets/public.svg" alt="public room"> -->
      <div>방제목</div>
    </div>
    <div class="buttons">
      <!-- 예약 시간 -->
      <div class="room_button">
        <img src="@/assets/reservation.svg" alt="reservation">
        <span>2023년 01월 17일 20:00</span>
      </div>
      <!-- 알림 신청 -->
      <div class="room_button" @click="onPushModal">
        <img src="@/assets/notification.svg" alt="notification">
      </div>
      <!-- 입장 -->
      <div class="room_button" @click="enter">
        <img src="@/assets/door.svg" alt="door" style="width:22px; height:22px">
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RoomItemView',
  methods: {
    // 알림 신청
    onPushModal() {
      this.$store.state.pushModal = !this.$store.state.pushModal
      // 알림 신청하는 코드 추가
    },

    // 입장
    enter() {
      // if (true) {
      this.$store.state.passwordModal = !this.$store.state.passwordModal
      // } else {
        // this.$router.push({name: WaitingRoom, params: { room_num :1 }})
      // }
    }
  }
}
</script>

<style scoped>
.room {
  display: flex;
  flex-direction: column;
  background-color:#F5F5F5;
  border-radius: 5%;
  padding: 3%;
  margin: 5%;
}
.room_title{
  display: flex;
  flex-direction: row;
  justify-content: left;
  padding: 3%;
}
.buttons{
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding: 3%;
}
.room_button{
  background-color: #1D182C;
  border-radius: 10%;
  padding: 1%;
  color: #F5F5F5;
}
.room_button .img, .room_button .span {
  vertical-align: middle;
}
</style>