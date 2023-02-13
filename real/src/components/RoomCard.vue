<template>
  <div id="container" @click="goWaitingRoom">
    <div style="display: flex; align-items: center">
      <img src="@/assets/Lock.svg" v-if="room.password" />
      <img src="@/assets/Unlock.svg" v-else />
      <div style="margin-left: 10px"></div>
      {{ room.title }}({{ room_info.playercnt }})
    </div>
    <div style="margin-top: 10px"></div>
    <!-- stretch는 위아래로 모든 div의 높이를 같게 해줘요 -->
    <div style="display: flex; align-items: stretch">
      <!-- flex: 1 은 남은공간을 이 div가 모두 가져가게 해 줘요 -->
      <div class="card_menu flex_center" style="color: white; flex: 1">
        <img src="@/assets/Clock.svg" />
        <!-- <div class="start_time" v-if="this.room.room_start_time < Date.now()">진행중</div> -->
        <div class="start_time">
          {{
            `${this.dateTime.month}월 ${this.dateTime.day}일 ${this.dateTime.hour}시 ${this.dateTime.minute}분`
          }}
        </div>
      </div>

      <div class="card_margin"></div>

      <div class="card_menu" @click="bell_icon">
        <img src="@/assets/Notification.svg" />
      </div>

      <div class="card_margin"></div>

      <div class="card_menu" @click="info_icon">
        <img src="@/assets/Info.svg" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    room_info: undefined,
  },

  data() {
    return {
      room: {},
      dateTime: {
        month: 0,
        day: 0,
        hour: 0,
        minute: 0,
      },
    };
  },
  async created() {
    this.room = await this.axios
      .get(process.env.VUE_APP_SPRING + "room/" + this.room_info.roomid)
      .then((res) => res.data.result);
    this.dateTime = this.room.dateTime;
  },
  mounted() {},
  methods: {
    async goWaitingRoom() {
      var user = await this.axios
        .get(process.env.VUE_APP_SPRING + "user", {
          headers: { Authorization: sessionStorage.getItem("token") },
        })
        .then((res) => res.data.result);
      console.log("-------------", user);
      this.conn.send(
        JSON.stringify({
          event: "enter",
          room: this.room_info.roomid,
          nickname: user.nickname,
          level: user.level,
        })
      );
      this.$router.push({ path: "/waitingRoom/" + this.room.id });
    },
    bell_icon(e) {
      alert("알람버튼이 눌렸어요");
      e.stopPropagation();
    },
    info_icon(e) {
      console.log("정보 버튼이 눌렸어요");
      console.log(this.room);
      this.$emit("info_show");
      this.$emit("room_info", this.room);
      e.stopPropagation();
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/assets/scss/_variable.scss";

#container {
  margin: $card_vertical_margin 0;
  background: #f2f2f2;
  height: 120px;
  width: 350px;
  border-radius: 15px;
  padding: 20px;
  box-shadow: 0px 10px 20px -10px rgb(0, 0, 0, 0.7);
  animation-name: room_card_appear;
  animation-duration: 0.5s;
  animation-iteration-count: 1;
}
@keyframes room_card_appear {
  0% {
    opacity: 0;
    transform: translateX(50px);
  }
}
.card_menu {
  padding: 10px;
  border-radius: 5px;
  background-color: black;
}
.card_margin {
  margin-left: $card_margin;
}
.start_time {
  width: 120px;
  text-align: center;
}
</style>