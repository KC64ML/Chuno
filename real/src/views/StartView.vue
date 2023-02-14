<template>
  <div id="container">
    <div class="video_back" style="overflow: hidden; z-index=0; position: absolute; top: 0; left: 0; width: 100vw; height: 100%; background-color: black;">
      <video src="@/assets/smoke.mp4" playbackRate="1.5" autoplay muted style="width: 250vw; position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);"></video>
    </div>
    <transition name="start_disappear">
      <img v-if="show_el" src="@/assets/main_logo1.png" class="block_center new_logo_appear"
        style="width:150px; z-index: 1000; position: relative;">
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
$video_duration: 6s;
#button1 {
  height: 70px;
}

.opacity_appear {
  opacity: 0;
  animation-name: btn_opacity;
  animation-delay: $video_duration / 4;
  animation-duration: $video_duration;
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
.new_logo_appear {
  animation-name: new_logo_appear;
  animation-duration: $video_duration;
  animation-iteration-count: 1;
}
@keyframes new_logo_appear {
  0%{
    opacity: 0;
    filter: blur(40px);
  }
  50% {
    opacity: 1;
    filter: blur(0) brightness(0) invert(1);
  }
}
.video_back {
  animation-name: video_back;
  animation-duration: $video_duration / 2;
  animation-delay: $video_duration / 2;
  animation-iteration-count: 1;
  animation-fill-mode: forwards;
}
@keyframes video_back {
  100% {
    opacity: 0;
  }
}
</style>