<template>
  <TeamView/>

</template>

<script>
import TeamView from '@/components/waitingroom/TeamView.vue'
import PlayerView from '@/components/waitingroom/PlayerView.vue'
import ChattingView from '@/components/waitingroom/ChattingView.vue'

export default {
  name: 'WaitingRoomView',
  components: {
    TeamView,
    PlayerView,
    ChattingView,
  }
}
</script>

<style>

</style>