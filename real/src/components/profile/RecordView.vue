<template>
  <div id="record">
    <div id="container">
      <img src="@/assets/Fist.svg" alt="record" id="iconBg">
      <div class="container-col">
        <br>
        <div>전적</div>
      </div>
    </div>
    <div>
      <div>
        {{ userInfo.chaserWinCount }}
      </div>
      <div>
        {{ userInfo.runnerWinCount }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RecordView',
  props: {
    userInfo: Object,
  },
  data() {
    // record: null,
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
}
</style>