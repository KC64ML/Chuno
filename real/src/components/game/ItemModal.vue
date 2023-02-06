<template>
  <!-- 아이템을 사용했을 때 뜨는 Cofirm용 모달 -->
  <div class="modal">
    <p>{{ item.name }}을(를) 사용하시겠습니까?</p>
    <div class="container-row">
      <div class="art-button" @click="itemNo">
        <div>아니오</div>
        <img src="@/assets/main_button1.png" alt="button">
      </div>
      <div class="art-button" @click="itemYes">
        <div>예</div>
        <img src="@/assets/main_button1.png" alt="button">
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'LogoutModal',
  data() {
    return {
      
    }
  },
  props :{
    item: Object,
  },
  methods: {
    itemYes() {
      console.log('아이템 사용')
      this.$store.state.itemModal = false
      // 아이템 사용 코드 추가
    },
    itemNo() {
      console.log('아이템 미사용')
      console.log(this.$store.state.itemModal)
      this.$store.state.itemModal = false
    },
  },
  created(){
    console.log(this.item)
  },
  
}
</script>

<style>
/* #modal {
  padding: 5%;
  margin: 5%;
  background-color: #F5F5F5;
  border-radius: 10%;
}
.art-button{
  position: relative;
}
.art-button img{
  width: 90%;
}
.art-button div {
  color: #F5F5F5;
  position: absolute;
  top: 30%;
  left: 20%;
  font-size: 20pt;
} */
</style>