<template>
    <div id="modal_back">
      <div id="make_room_modal" v-if="page1">
        <div class="flex_center">
            <img
              src="@/assets/TruePaper.png"
              style="width: 140px"
            />
        </div>
        <div id="modal_title" style="font-size: 24px">노비 문서를 찾았습니다!</div>
  
        <div class="flex_center">
          <div class="flex_center hover_pointer" data-v-7961e374="">
            <img
              src="@/assets/main_button1.png"
              id="button1"
              data-v-7961e374=""
              style="width: 140px"
            />
            <div class="image_text" data-v-7961e374="" @click="verify">확인하기</div>
          </div>
      </div>
      <div id="make_room_modal" v-if="!page1">
        <div class="flex_center">
            <img
              src="@/assets/TruePaper.png"
              style="width: 140px"
            />
        </div>
        <div v-if="ripTarget.real">
          <div id="modal_title" style="font-size: 24px">이 문서를는 진짜입니다.</div>
          <div class="flex_center hover_pointer" data-v-7961e374="">
            <img
              src="@/assets/main_utton1.png"
              id="button1"
              data-v-7961e374=""
              style="width: 140px"
            />
            <div class="image_text" data-v-7961e374="" @click="onTruePaper">찢어버리기</div>
          </div>
        </div>

        <div v-if="!ripTarget.real">
          <div id="modal_title" style="font-size: 24px">이 문서를는 가짜입니다.</div>
          <div class="flex_center">
            <div class="flex_center hover_pointer" data-v-7961e374="">
              <img
                src="@/assets/main_button1.png"
                id="button1"
                data-v-7961e374=""
                style="width: 140px"
              />
              <div class="image_text" data-v-7961e374="" @click="onFalsePaper">확인</div>
          </div>
        </div>
  
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: "DeleteAccountModal",
    props: {
      ripTarget: Object,
    },
    data(){
        return {
            page1: true,
        }
    },
    methods: {
      verify() {
        console.log("확인하기?");
        this.page1 = false
      },
      onTruePaper(){
        console.log('-----------------진짜 문서----------------')
        console.log(this.ripTarget + '을 찢었다!!!!!!')
        this.$emit('on-rip', this.ripTarget)
      },
      onFalsePaper(){
        console.log('-----------------가짜 문서----------------')
        console.log(this.ripTarget + '가 가짜문서라니ㅠㅠㅠ')
        this.$emit('on-rip', this.ripTarget)
      }
    },
  };
  </script>
  
  <style scoped>
  #modal {
    /* width: 150px; */
    background-color: #f5f5f5;
    border-radius: 10%;
  }
  img {
    width: 150px;
  }
  
  .container p {
    /* position: absolute; */
    /* top: 0; */
    /* left: 50%; */
    /* transform: translate(-50%, -50%); */
    color: #f5f5f5;
    font-size: 18pt;
    margin: 0;
  }
  /* .container img{
    height: 80px;
  } */
  
  #modal_back {
    position: absolute;
    width: 100vw;
    height: 100%;
    background: rgb(0, 0, 0, 0.6);
    z-index: 1;
  }
  
  #make_room_modal {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 85%;
    padding: 30px 0;
    background-color: #f2f2f2;
    transform: translate(-50%, -50%);
    font-size: 18px;
    border-radius: 10px;
  }
  
  #close_button {
    position: absolute;
    top: 10px;
    right: 20px;
    font-size: 20px;
  }
  
  #modal_title {
    text-align: center;
    margin-bottom: 20px;
  }
  </style>