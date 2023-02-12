<template>
    <div id="container">
        <img src="@/assets/main_logo1.png" class="block_center sliding_down_appearance" style="width:150px">
        <div style="margin-top: 30px"></div>
        <div class="flex_center hover_pointer button_animation" @click="start">
            <img src="@/assets/main_button1.png" id="button1">
            <div class="image_text">시작해요</div>
        </div>
        <br>
    </div>
</template>

<script>
  export default {
    methods: {
      start() {
          this.$router.push("/login")
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
</style>