<template>
  <nav id="header">
    <router-link to="/">
      <img class="logo" alt="Chuno Logo" src="@/assets/round_logo.svg">
    </router-link>
    <p>방제목</p>
  </nav>
  <TeamView/>

</template>

<script>
import TeamView from '@/components/waitingroom/TeamView.vue'
// import PlayerView from '@/components/waitingroom/PlayerView.vue'
// import ChattingView from '@/components/waitingroom/ChattingView.vue'

export default {
  name: 'WaitingRoomView',
  components: {
    TeamView,
    // PlayerView,
    // ChattingView,
  }
}
</script>

<style>

</style>