<template>
  <OpenViduVue style="z-index: 100;" @test="testa"></OpenViduVue>
  <MapView></MapView>
  <GameFooterView class="footer_fix"></GameFooterView>
</template>

<script>
import OpenViduVue from '@/components/game/OpenViduVue.vue'
import MapView from '@/components/game/MapView.vue'
import GameFooterView from "@/components/GameFooterView.vue"

export default {

  name: 'GameView',
  components: {
    GameFooterView,
    MapView,
    OpenViduVue
  },
  methods: {
    testa(ttt) {
      console.log(ttt);
    }
  }
}

</script>

<style lang="scss" scoped>
@import "@/assets/scss/variable.scss";
  

</style>