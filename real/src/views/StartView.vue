<template>
  <div id="container">
    <transition name="start_disappear">
      <img v-if="show_el" src="@/assets/main_logo1.png" class="block_center sliding_down_appearance"
        style="width:150px">
    </transition>
    <div style="margin-top: 30px"></div>
    <transition name="button_disappear">
      <div v-if="show_el" class="opacity_appear">
        <div class="flex_center hover_pointer button_animation" @click="start">
          <img src="@/assets/main_button1.png" id="button1">
          <div class="image_text">시작해요</div>
        </div>
      </div>
    </transition>
  </div>

</template> 
<script>
export default {
  data() {
    return {
      show_el: true,
    }
  },
  methods: {
    start() {
      this.show_el = false;
      setTimeout(() => {
        this.$router.push("/login")
      }, 1200)
      this.$emit("playMusic")
    },
  },
}
</script>

<style lang="scss" scoped>
#button1 {
  height: 70px;
}

.opacity_appear {
  opacity: 0;
  animation-name: btn_opacity;
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
  animation-name: btn_scale;
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
  transition: all 1.3s;
}

.start_disappear-leave-to {
  opacity: 0;
  transform: rotate(-40deg) skewX(50deg) scale(1.5);
  text-shadow: 0 0 20px;
  filter: blur(10px);
  ;
}
.button_disappear-leave-active {
  transition: all 1.1s;
}
.button_disappear-leave-to {
  filter: opacity(0%);
}

</style>