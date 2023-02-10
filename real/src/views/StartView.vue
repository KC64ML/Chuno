<template>
    <div id="container">
        <img src="@/assets/main_logo1.png" class="block_center img_animation" style="width:150px">
        <div style="margin-top: 30px"></div>
        <div class="flex_center hover_pointer button_animation" @click="start">
            <img src="@/assets/main_button1.png" id="button1">
            <div class="image_text">시작해요</div>
        </div>
        <br>
        <div @click="play()">audioPlayer</div>
    </div>
</template>

<script>
import bgm from '@/assets/bgm.mp3'
  export default {
    methods: {
      start() {
          this.$router.push("/login")
      },
      play() {
          var audio = new Audio(this.bgm);
          audio.play();
        }
    },

    data(){
      return {
        bgm,
      }
    },
    

  }
</script>

<style lang="scss" scoped>
    #button1 {
        height: 70px;
    }
    .button_animation {
      animation-name:btn_scale;
      animation-iteration-count: infinite;
      animation-timing-function: linear;
      animation-duration: 2s;
    }

    @keyframes btn_scale {
      50% {
        transform: scale(1.3);
      }
      100% {
        transform: scale(1);
      }
    }

    .img_animation {
      animation-name: img_ani;
      animation-iteration-count: 1;
      animation-duration: 1s;
    }

    @keyframes img_ani {
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