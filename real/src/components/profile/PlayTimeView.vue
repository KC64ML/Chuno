<template>
  <div id="record">
    <div id="container">
      <!-- <div> -->
      <img src="@/assets/WallClock.svg" alt="record" id="iconBg" />
      <!-- </div> -->
      <div class="container-col">
        <div>플레이</div>
        <div>시간</div>
      </div>
    </div>
    <br />
    <div>
      <div>
        <div>전체 횟수</div>
        <div id="cnt">{{ totalPlay }}회</div>
      </div>
      <br />
      <div>
        <div>추노꾼 횟수</div>
        <div id="cnt">{{ userInfo.chaserPlayCount }}회</div>
      </div>
      <br />
      <div>
        <div>노비 횟수</div>
        <div id="cnt">{{ userInfo.runnerPlayCount }}회</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "PlayTimeView",
  props: {
    userInfo: Object,
  },
  data() {
    // playTime: null,
  },
  computed: {
    totalPlay() {
      return this.userInfo.chaserPlayCount + this.userInfo.runnerPlayCount;
    },
  },
};
</script>

<style scoped>
#record {
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
  width: 50%;
  border-radius: 10%;
  padding: 15px;
}

#record div p {
  color: black;
}
#iconBg {
  background-color: black;
  border-radius: 10%;
}
#container {
  display: flex;
  flex-direction: row;
  /* text-align: left; */
  justify-content: space-around;
  vertical-align: middle;
}
#container div {
  padding: 5%;
  /* margin: 3%; */
  align-items: center;
}
#container img {
  width: 50px;
  padding: 5px;
}
.container-col div {
  width: 50px;
  text-align: left;
}
#cnt {
  font-size: 22px;
}
</style>