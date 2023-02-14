<template>
  <transition name="start_disappear">
    <div id="container">
        <img src="@/assets/main_logo1.png" class="block_center sliding_down_appearance" style="width:150px">
        <div style="margin-top: 30px"></div>
        <div class="opacity_appear">
          <div class="flex_center hover_pointer button_animation" @click="start">
              <img src="@/assets/main_button1.png" id="button1">
              <div class="image_text">시작해요</div>
          </div>
        </div>
        <br>
    </div>
  </transition>
</template> 
<script>
  export default {
    methods: {
      start() {
          this.$router.push("/login")
          this.$emit("playMusic")
      },
    },

    data(){
      return {
      }
    },
  }
</script>

<style lang="scss" scoped>
    #button1 {
        height: 70px;
    }
    .opacity_appear {
      opacity: 0;
      animation-name:btn_opacity;
      animation-delay: .3s;
      animation-duration: 2s;
      animation-fill-mode: forwards;
      animation-iteration-count: 1;
    }
    @keyframes btn_opacity {
      0% {
        opacity: 0
      }
      100% {
        opacity: 1;
      }
    }
    .button_animation {
      animation-name:btn_scale;
      animation-iteration-count: infinite;
      animation-duration: 2s;
    }

    @keyframes btn_scale {
      50% {
        transform: scale(1.2);
      }
      100% {
        transform: scale(1);
      }
    }
    .start_disappear-leave-active {
      transition: all 3s;
    }
    .start_disappear-leave-to {
      opacity: 0;
    }
</style>