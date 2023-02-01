<template>
  <div class="modal-bg">
    <!-- page 1 -->
    <div class="modal" v-if="page">
      <div class="container-row" id="modal-title">
        <p>방만들기</p>
        <p @click="onClose">X</p>
      </div>
      <div>
        <div class="container-row">
          <label for="title">방제목</label>
          <input type="text" id="title" v-model="title">
        </div>

        <div class="container-row">
          <label for="player">최대 인원</label>
          <p @click="onMinus" id="btn">-</p>
          <!-- <input type="number" id="player" max="10" min="4"> -->
          <p>{{ maxPlayer }}</p>
          <p @click="onPlus" id="btn">+</p>
        </div>

        <div class="container-row">
          <label for="password">비밀번호</label>
          <input type="password" id="password">
        </div>
        
        <div class="container-row">
          <label for="reservation">예약</label>
          <div class="container-col">
            <input type="date" id="reservation" v-model="date">
            <div class="container-row">
              <div :id="dateBtn ? date-btn1 : date-btn2" @click="onDate"><p>오늘</p></div>
              <div :id="dateBtn ? date-btn2 : date-btn1" @click="onDate"><p>내일</p></div>
            </div>
            <input type="time" id="reservation" v-model="time">
          </div>
        </div>
      </div>
      <div class="container" @click="onNext">
        <p>다음</p>
        <img alt="btn" src="@/assets/main_button1.png">
      </div>
    </div>
    
    <!-- page 2 -->
    <div class="modal" v-if="!page">
      <div class="container-row" id="modal-title">
        <p>방만들기</p>
        <p @click="onClose">X</p>
      </div>
      <div>
        <label for="radious">반경</label>
        <p>{{ this.radius }} m</p>
      </div>
      <MapView
      :radius="radius"
      />
      <Slider
        v-model="radius"
        :min="300"
        :max="1000"
        :step="50"
      ></Slider>
      <div class="container" @click="onNext">
        <p>이전</p>
        <img alt="btn" src="@/assets/main_button1.png">
      </div>
      <div class="container" @click="onCreate">
        <p>확인</p>
        <img alt="btn" src="@/assets/main_button1.png">
      </div>
    </div>
  </div>
</template>

<script>
import MapView from '@/components/home/MapView.vue'
import Slider from '@vueform/slider'

export default {
  name: 'CreateRoomModal',
  components: {
    MapView,
    Slider,
  },
  data() {
    return {
      page: true,
      // page 1
      title: '',
      maxPlayer: 4,
      password: null,
      dateBtn: true,
      date: null,
      time: null,
      // page2
      radius: 500,
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
    },

    // 최대인원
    onMinus() {
      if(this.maxPlayer <= 4){
        alert('플레이 최소인원은 4명입니다.')
      } else {
        this.maxPlayer -= 2
      }
    },
    onPlus() {
      if(this.maxPlayer < 10){
        this.maxPlayer += 2
      } else {
        alert('플레이 최대인원은 10명입니다.')
      } 
    },
    // Modal off
    onClose() {
      this.$store.state.CreateRoomModal = false
    },
    // 오늘 내일
    onDate() {
      this.dateBtn = !this.dateBtn
    }
  },
  computed(){
    return {
      titleLen() {
        this.title.length
      }
    }
  },
  watch: {
    date(res){
      console.log(res)
      console.log(typeof(res))
    },
    time(res){
      console.log(res)
      console.log(typeof(res))
    }
  }
}
</script>

<style src="@vueform/slider/themes/default.css"></style>
<style>
  .container-row{
    display: flex;
    flex-direction: row;
  }
  .container-col{
    display: flex;
    flex-direction: column;
  }

  .modal-bg{
    width: 100%; height:100%;
    background: rgba(0,0,0,0.5);
    position: fixed; padding: 20px;
  }

  .modal{
    position: fixed;
    top: 10%;
    background-color: #F5F5F5;
    border-radius: 10%;
    width: 70vw;
    /* height: 80%; */
  }

  #modal-title {
    font-size: 20pt;
  }
  #btn {
    color: #F5F5F5;
    background-color: black;
    padding: 2%;
    border-radius: 30%;
  }

  #date-btn1{
    color: #F5F5F5;
    background-color: black;
  }
  #date-btn2{
    color: #F5F5F5;
    background-color: #434039;
  }
</style>