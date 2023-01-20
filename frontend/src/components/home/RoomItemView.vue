<template>
  <div class="room">
    <div class="room_title">
      <img src="@/assets/private.svg" alt="private room">
      <!-- <img src="@/assets/public.svg" alt="public room"> -->
      <div>방제목</div>
    </div>
    <div class="buttons">
      <div class="room_button">
        <img src="@/assets/reservation.svg" alt="reservation">
        <span>2023년 01월 17일 20:00</span>
      </div>

      <div class="room_button">
        <img src="@/assets/notification.svg" alt="notification">
      </div>

      <div class="room_button">
        <img src="@/assets/door.svg" alt="door" style="width:22px; height:22px">
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RoomItemView',
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