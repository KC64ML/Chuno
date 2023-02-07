<template>
  <div id="record">
    <div id="container">
      <!-- <div> -->
        <img src="@/assets/WallClock.svg" alt="record" id="iconBg">
      <!-- </div> -->
      <div class="container-col">
        <div>플레이</div>
        <div>타임</div>
      </div>
    </div>
    <div>
      <div>
        <div>플레이한 게임</div>
        <div>{{ totalPlay }}회</div>
      </div>
      <div>
        <div>추노꾼 플레이</div>
        <div>{{ userInfo.chaserPlayCount }}회</div>
      </div>
      <div>
        <div>노비 플레이</div>
        <div>{{ userInfo.runnerPlayCount }}회</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PlayTimeView',
  props: {
    userInfo: Object,
  },
  data() {
    // playTime: null,
  },
  computed:{
    totalPlay(){
      return this.userInfo.chaserPlayCount + this.userInfo.runnerPlayCount
    }
  }
}
</script>

<style scoped>
#record{
  display: flex;
  flex-direction: column;
  background-color: #F5F5F5;
  width: 50%;
  border-radius: 10%;
}

#record div p {
  color: black;
}
#iconBg {
  background-color: black;
  border-radius: 10%;
}
#container{
  display: flex;
  flex-direction: row;
  /* text-align: left; */
  justify-content: space-around;
  vertical-align: middle;
}
#container div {
  padding: 5%;
  margin: 3%;
}
#container img {
  width: 30px;
}
.container-col div{
  width: 50px;
  text-align: left;
}
</style>