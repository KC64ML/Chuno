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
  </div>
</template>

<script>
export default {
  name: 'PlayTimeView',
  data() {
    // playTime: null,
  },
  methods: {
    
  },
}
</script>

<style scoped>
#record{
  display: flex;
  flex-direction: column;
  background-color: #F5F5F5;
  width: 50%;
  /* height: 100%; */
  border-radius: 10%;
  margin: 3%;
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
  justify-content: space-around;
}
#container div {
  padding: 5%;
  margin: 3%;
}
#container img {
  width: 30px;
}
</style>