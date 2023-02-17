<template>
  <div id="header">
      <img src="@/assets/main_logo2.png" @click="goHome" style="height: 50px; position: absolute; left: 20px;">
      <div style="font-size: 30px;">
          {{ this.title }}
      </div>
  </div>
</template>

<script>
import tak from "@/assets/audio/tak.mp3";

  export default {
      props: {
          title: undefined
      },
      data() {
          return {
              here: undefined,
              audio: {
                id: "music-home",
                name: "MusicHome",
                file: new Audio(tak),
            },
          }
      },
      created() {
          // console.log(this.title)
      },  
      methods: {
          goHome() {
              this.$router.push("/home");
              this.play(this.audio);
          },
          play(audio) {
            audio.file.play();
    },
      }
  }
</script>

<style lang="scss" scoped>
@import "@/assets/scss/variable";

  #header {
      position: absolute;
      top: $header_margin;
      height: $header_height;
      width: 100vw;
      display: flex;
      align-items: center;
      justify-content: center;
  }
</style>