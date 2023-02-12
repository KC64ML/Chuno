<template>
  <div id="record">
    <div id="container">
      <img src="@/assets/Fist.svg" alt="record" id="iconBg" />
      <div class="container-col">
        <div>전적</div>
      </div>
    </div>
    <br />
    <div>
      <div class="record">
        <div>총 승리</div>
        <div id="cnt">{{ totalWin }}회</div>
      </div>
      <br />
      <div>
        <div>추노꾼 승리</div>
        <div id="cnt">{{ userInfo.chaserWinCount }}회</div>
      </div>
      <br />
      <div>
        <div>잡은 노비</div>
        <div id="cnt">{{ userInfo.catchCount }}명</div>
      </div>
      <br />
      <div>
        <div>노비 승리</div>
        <div id="cnt">{{ userInfo.runnerWinCount }}회</div>
      </div>
      <br />
      <div>
        <div>발견한 노비 문서</div>
        <div id="cnt">{{ userInfo.paperCount }}장</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "RecordView",
  props: {
    userInfo: Object,
  },
  data() {
    // record: null,
  },
  computed: {
    totalWin() {
      return this.userInfo.chaserWinCount + this.userInfo.runnerWinCount;
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
  /* justify-content: space-around; */
  vertical-align: middle;
  align-items: center;
}
#container div {
  padding: 5%;
  margin: 6%;
}
#container img {
  width: 50px;
  padding: 5px;
}
.container-col div {
  width: 50px;
}
#cnt {
  font-size: 22px;
}
</style>