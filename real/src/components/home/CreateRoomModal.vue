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
            <div class="container-row">
              <div  class="date-btn1" :id="today ? 'date-btn1' : 'date-btn2'" @click="onDate"><p>오늘</p></div>
              <div  class="date-btn2" :id="today ? 'date-btn2' : 'date-btn1'" @click="onDate"><p>내일</p></div>
            </div>
            <input type="time" id="reservation" v-model="timeInput" @change="onTime">
          </div>
        </div>
      </div>
      <div class="art-button" @click="onNext">
        <div>다음</div>
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
        <span>반경</span>
        <span>{{ this.radius }} m</span>
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
      <div class="art-button" @click="onNext">
        <div>이전</div>
        <img alt="btn" src="@/assets/main_button1.png">
      </div>
      <div class="art-button" @click="onCreate">
        <div>확인</div>
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
      today: true,
      date: {
        year: null,
        month: null,
        day: null,
      },
      time: {
        hour: null,
        min: null,
      },
      timeInput: '',
      // page2
      radius: 500,
    }
  },
  methods: {
    // Modal off
    onClose() {
      this.$store.state.CreateRoomModal = false
    },

    // 페이지 전환
    onNext() {
      this.page = !this.page
    },

    // 방 생성
    // params 수정 필요
    onCreate() {
      this.axios.post(process.evn + `room`)
        .then(res=>{
          console.log(res)
        })
      this.$router.push({ name: 'game', params: { roomId: 1 } })
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
    // 오늘 내일
    onDate() {
      this.today = !this.today
      const today = new Date()
      console.log(today)
      if(this.today){
        this.date.year = today.getFullYear()
        this.date.month = today.getMonth() + 1
        this.date.day = today.getDate()
      } else{
        this.date.year = today.getFullYear()
        this.date.month = today.getMonth() + 1
        this.date.day = today.getDate() + 1
      }
      console.log(this.date.year)
      console.log(this.date.month)
      console.log(this.date.day)
    },
    // 예약 시간
    onTime() {
      const selectedTime = this.timeInput.split(':')
      console.log(selectedTime)
      this.time.hour = Number(selectedTime[0])
      this.time.min = Number(selectedTime[1])
      const today = new Date()
      if(this.today){
        console.log(this.today)
        const hrs = today.getHours()
        const min = today.getMinutes()
        if(this.time.hour < hrs) {
          console.log('wrong time')
        } else if(this.time.hour == hrs && this.time.min < min) {
          console.log('wrong time')
        }
      }
    }
  },
  computed(){
    return {
      titleLen() {
        this.title.length
      }
    }
  },
  // watch: {
  //   time(){
  //     this.onTime()
  //   }
  // }
}
</script>

<style lang="scss" src="@vueform/slider/themes/default.css"></style>
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
    background-color: black;
  }
  #date-btn2{
    background-color: rgba(67, 64, 57, 1);
  }
  .date-btn1{
    color: #F5F5F5;
    padding: 5%;
    border-radius: 20% 0 0 20%;
  }
  .date-btn2{
    color: #F5F5F5;
    padding: 5%;
    border-radius: 0 20% 20% 0;
  }
</style>