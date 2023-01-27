<template>
  <div class="modal-bg">
    <!-- page 1 -->
    <div class="modal" v-if="page">
      <div class="container-row">
        <p>방만들기</p>
        <p @click="$emit('create-room')">X</p>
      </div>
      <div>
        <label for="title">방제목</label>
        <input type="text" id="title" v-model="title">
        <p v-if="title.length" style="margin: 0;">ghghghghgh</p>
        <br v-else>
        <br>
        <label for="player">최대 인원</label>
        <input type="number" id="player" max="10" min="4">명
        <br>
        <label for="password">비밀번호</label>
        <input type="text" id="password">
        <br>
        <label for="reservation">예약</label>
        <input type="date" id="reservation">
        <input type="time" id="reservation">
      </div>
      <div class="container" @click="onNext">
        <p>다음</p>
        <img alt="btn" src="@/assets/button_back1.svg">
      </div>
    </div>
    <!-- page 2 -->
    <div class="modal" v-if="!page">
      <div class="container-row">
        <p>방만들기</p>
        <p @click="$emit('create-room')">X</p>
      </div>
      <div>
        <label for="radious">반경</label>
        <input type="number" value="500" max="1000" min="100">m
      </div>
      <div class="container" @click="onNext">
        <p>이전</p>
        <img alt="btn" src="@/assets/button_back1.svg">
      </div>
      <div class="container" @click="onCreate">
        <p>확인</p>
        <img alt="btn" src="@/assets/button_back1.svg">
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CreateRoomModal',
  data() {
    return {
      page: true,
      title: '',
      password: null,
      date: null,
      radiouos:500,
    }
  },
  methods: {
    // 페이지 전환
    onNext() {
      this.page = !this.page
    },

    // 방 생성
    // params 수정 필요
    onCreate() {
      this.$router.push({ name: 'WaitingRoom', params: { room_num: 1} })
    }
  },
  computed(){
    return {
      titleLen() {
        this.title.length
      }
    }
  }
}
</script>

<style>

</style>