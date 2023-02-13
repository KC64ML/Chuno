<template>
  <div class="main_back flex_center">
    <router-view :key="$route.fullPath"></router-view>
  </div>
  <MusicPlayVue
    class="player"
    v-show="musicControll()"
    :isPlay="isPlay"
  ></MusicPlayVue>
  <FooterVue
    class="footer_fix"
    v-if="
      this.$route.name != 'waitingRoom' &&
      this.$route.name != 'game' &&
      this.$route.fullPath != '/' &&
      this.$route.fullPath != '/login' &&
      this.$route.path != '/oauth' &&
      this.$route.name != 'Register'
    "
  >
  </FooterVue>
</template>

<script>
import FooterVue from "@/components/FooterVue.vue";
import MusicPlayVue from "@/components/musicPlayVue.vue";

export default {
  components: {
    FooterVue,
    MusicPlayVue,
  },
  methods: {
    test() {
      this.conn.send({
        event: "reset",
      });
    },
    musicControll() {
      if (this.$route.name == "friends" || this.$route.name == "search") {
        this.isPlay = true;
        return false;
      } else if (
        this.$route.name != "waitingRoom" &&
        this.$route.name != "game"
      ) {
        this.isPlay = true;
        return true;
      } else {
        this.isPlay = false;
        return false;
      }
    },
  },
  created() {
    console.log("앱시작");
  },
  data() {
    return {
      isPlay: true,
    };
  },
};
</script>

<style lang="scss">
.button_style_1 {
  background-image: url("@/assets/main_button1.png");
  background-size: cover;
  width: 120px;
  height: 50px;
}
.block_center {
  display: block;
  margin: 0 auto;
}
.flex_center {
  display: flex;
  align-items: center;
  justify-content: center;
}
.flex_left {
  display: flex;
  align-items: center;
  justify-content: left;
}
.flex_right {
  display: flex;
  align-items: center;
  justify-content: right;
}
.relative {
  position: relative;
}
.image_text {
  color: white;
  position: absolute;
}
.image_text_logout {
  color: white;
  position: absolute;
  text-align: center;
}
.hover_pointer:hover {
  cursor: pointer;
}
.footer_fix {
  position: absolute;
  bottom: 0;
}
textarea:focus,
input:focus {
  outline: none;
}
/* 모달 */
.modal-bg {
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  position: fixed;
  padding: 20px;
}

.modal {
  // position: fixed;
  background-color: #f5f5f5;
  border-radius: 10%;
  width: 70vw;
  padding: 5%;
  margin: 5%;
}

@font-face {
  font-family: "WandohopeR";
  src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-10@1.0/WandohopeR.woff")
    format("woff");
  font-weight: bold;
  font-style: normal;
}

* {
  font-family: "WandohopeR";
  margin: 0;
  box-sizing: border-box;
}

html,
body,
#app {
  height: 100%;
}
.grey {
  color: grey;
}

.button_style_1 {
  background-image: url("@/assets/main_button1.png");
  background-size: cover;
  width: 120px;
  height: 50px;
}

.block_center {
  display: block;
  margin: 0 auto;
}

.flex_center {
  display: flex;
  align-items: center;
  justify-content: center;
}

.relative {
  position: relative;
}

.image_text {
  color: white;
  position: absolute;
}

.hover_pointer:hover {
  cursor: pointer;
}

.footer_fix {
  position: absolute;
  bottom: 0;
}

textarea:focus,
input:focus {
  outline: none;
}
// leechaeeun
.art-button {
  position: relative;
}
.art-button img {
  width: 90%;
}
.art-button div {
  color: #f5f5f5;
  position: absolute;
  top: 30%;
  left: 20%;
  font-size: 20pt;
}
.scroll::-webkit-scrollbar {
  display: none; /* 크롬, 사파리, 오페라, 엣지 */
}
.sliding_down_appearance {
  animation-name: sliding_down_appearance;
  animation-iteration-count: 1;
  animation-duration: 1s;
}
@keyframes sliding_down_appearance {
  0% {
    opacity: 0;
    transform: translateY(-30px);
  }
  100% {
    opacity: 100;
    transform: translateX(0);
  }
}
</style>

<style lang="scss" scoped>
.main_back {
  background-image: url("@/assets/main_back.png");
  //cover는 다 덮도록 확대축소(1번 만나옴) contain 이미지가 전부 나오도록 확대축소 (2번나올수 있음)
  background-size: cover;
  width: 100vw;
  height: 100%;
  overflow: hidden;
}

// soundPlayer
.player {
  width: 10px;
  height: 10px;
  position: absolute;
  top: 20px;
  right: 50px;
}
</style>