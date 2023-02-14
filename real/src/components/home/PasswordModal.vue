<template>
  <div id="modal_back" style="z-index: 5">
    <div id="pass_modal">
      <div>
        <div id="close_button" @click="offing">x</div>
        <div id="modal_title" style="font-size: 24px">비밀번호</div>
      </div>
      <div class="{'shake' : wrong}">
        <input
          v-model="password"
          style="padding: 0 20px; display: block; width: 70%; margin: 0 auto"
          type="password"
        />
      </div>

      <div v-show="wrong" class="wrong-pass">비밀번호가 틀렸습니다.</div>
      <div
        class="flex_center hover_pointer"
        @click="onConfirm"
        style="margin-top: 20px"
      >
        <img
          src="@/assets/main_button1.png"
          id="button1"
          style="width: 140px"
        />
        <div class="image_text">확인</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "PasswordModal",
  props: {
    roomInfo: Object,
  },
  data() {
    return {
      password: "",
      wrong: false,
    };
  },
  methods: {
    // 비밀번호 일치 확인 코드 바꾸기
    onConfirm() {
      if (this.roomInfo) console.log("room_info", this.roomInfo);
      this.axios
        .post(
          process.env.VUE_APP_SPRING + "room/join/" + this.roomInfo.id,
          {
            password: this.password,
          },
          {
            headers: { Authorization: sessionStorage.token },
          }
        )
        .then((res) => {
          const code = res.data.code;
          if (code == 1) {
            this.wrong = false;
            this.offing();
            this.$router.push(`/waitingRoom/${this.roomInfo.id}`);
          } else if (code == 3) {
            this.wrong = true;
          }
          console.log("code", code);
        });
    },
    offing() {
      this.$emit("pass_close");
    },
    shakeWrongAnswer() {
      this.wrong = true;
      setTimeout(() => {
        this.wrong = false;
      }, 1000);
    },
  },
};
</script>

<style lang="scss" scoped>
$input_height: 30px;
$plma_size: 30px;

#modal_back {
  position: absolute;
  width: 100vw;
  height: 100%;
  background: rgb(0, 0, 0, 0.6);
  z-index: 5;
}

#pass_modal {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 85%;
  padding: 30px 0;
  background-color: #f2f2f2;
  transform: translate(-50%, -50%);
  font-size: 18px;
  border-radius: 10px;
  animation-name: modal_appearance;
  animation-iteration-count: 1;
  animation-duration: 0.8s;
}
#close_button {
  position: absolute;
  top: 10px;
  right: 20px;
  font-size: 20px;
}

#modal_title {
  text-align: center;
  margin-bottom: 30px;
}

input {
  text-align: center;
  height: $input_height;
  background-color: rgb(67, 64, 57, 0.3);
  border-radius: $plma_size / 2;
}
.wrong-pass {
  color: red;
  font-size: 14px;
  text-align: center;
  margin-top: 10px;
  margin-bottom: 10px;
}
.shake {
  animation: shake 0.82s cubic-bezier(0.36, 0.07, 0.19, 0.97) both;
  transform: translate3d(0, 0, 0);
}
@keyframes shake {
  10%,
  90% {
    transform: translate3d(-1px, 0, 0);
  }
  20%,
  80% {
    transform: translate3d(2px, 0, 0);
  }
  30%,
  50%,
  70% {
    transform: translate3d(-4px, 0, 0);
  }
  40%,
  60% {
    transform: translate3d(4px, 0, 0);
  }
}
</style>