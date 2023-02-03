<template>
  <div>
    <OpenViduVue style="z-index: 100;"></OpenViduVue>
    <MapView/>
    <MenuView v-if="menu"/>
  
    <GameFooterView 
      class="footer_fix"
      @on-menu="onMenu"
    />
  </div>
</template>

<script>
import OpenViduVue from '@/components/game/OpenViduVue.vue'
import MapView from '@/components/game/MapView.vue'
import GameFooterView from "@/components/GameFooterView.vue"
import MenuView from '@/components/game/MenuView.vue'

export default {

  name: 'GameView',
  components: {
    GameFooterView,
    MapView,
    OpenViduVue,
    MenuView,
  },
<<<<<<< HEAD
  data(){
    return {
      menu: true,
    }
  },
  methods: {
    onMenu(){
      console.log('menu clicked')
      this.menu = !this.menu
=======
  methods: {
    testa(ttt) {
      console.log(ttt);
>>>>>>> 4e1c161a5b72d35b7e3827b59fe4f11d906e460d
    }
  }
}

</script>

<style lang="scss" scoped>
@import "@/assets/scss/variable.scss";
  

</style>