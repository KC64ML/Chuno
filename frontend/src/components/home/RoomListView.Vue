<template>
  <div>
    <p>내가 만든 방</p>
    <RoomItemView/>
    <p>전체 방</p>
  </div>
</template>

<script>
import RoomItemView from '@/components/home/RoomItemView.vue'

export default {
  name: 'RoomListView',
  components: {
    RoomItemView,
  },
  props: {
    room: Object,
  }
}
</script>

<style>

</style>