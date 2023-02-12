<template>
  <div>
    <div>
      <img
        class="music"
        :v-if="audio.isPlaying"
        @click="audio.isPlaying ? pause(audio) : play(audio)"
        v-bind:src="`${audio.isPlaying ? soundOn : soundOff}`"
      />
    </div>
    <div>
      <img
        class="music"
        :v-if="!audio.isPlaying"
        @click="audio.isPlaying ? pause(audio) : play(audio)"
        src="@/assets/sound_off.png"
      />
    </div>
  </div>
</template>

<script>
import bgm from "@/assets/audio/bgm.mp3";
import soundOn from "@/assets/sound_on.png";
import soundOff from "@/assets/sound_off.png";

export default {
  props: {
    isPlay: Boolean,
  },
  data() {
    return {
      bgm,
      soundOn,
      soundOff,
      audio: {
        id: "music-opening",
        name: "MuscicOpening",
        file: new Audio(bgm),
        loop: true,
        isPlaying: false,
      },
    };
  },

  updated() {
    if (!this.isPlay) {
      this.audio.isPlaying = false;
      this.audio.file.pause();
      this.audio.currentTime = 0;
    }
  },

  methods: {
    play(audio) {
      audio.isPlaying = true;
      audio.file.play();
    },

    pause(audio) {
      audio.isPlaying = false;
      audio.file.pause();
    },
  },
};
</script>

<style>
.music {
  position: absolute;
  width: 40px;
  height: 40px;
  cursor: pointer;
}
</style>