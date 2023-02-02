<template>
  <OpenViduVue style="z-index: 100;"></OpenViduVue>
  <MapView/>
</template>

<script>
import OpenViduVue from '@/components/game/OpenViduVue.vue'
import MapView from '@/components/game/MapView.vue'

export default {
  name: 'GameView',
  components: {
    MapView,
    OpenViduVue
  },
}

</script>

<style lang="scss" scoped>
  
</style>