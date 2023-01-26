<template>
  <div class="modal-bg">
    <div class="modal" v-if="page">
      <div class="container-row">
        <p>방만들기</p>
        <p>X</p>
      </div>
      <div>
        <label for="title">방제목</label>
        <input type="text" id="title">
        <div v-if="titleLen">
          <p>ghghghghgh</p>
        </div>
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
    <div class="modal" v-if="!page">
      <div class="container-row">
        <p>방만들기</p>
        <p>X</p>
      </div>
      <div>
        <label for="radious">반경</label>
        <input type="number" value="500" max="1000" min="100">m
      </div>
      <div class="container" @click="onNext">
        <p>이전</p>
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
    onNext() {
      this.page = !this.page
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