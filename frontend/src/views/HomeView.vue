<template>
  <div class="home">
    home
    <RoomListView
      v-for="room in rooms"
      :key="room.id"
      :room="room"
    />
  </div>
</template>

<script>
import RoomListView from '@/components/home/RoomListView.vue'
export default {
  name: 'HomeView',
  components: {
    RoomListView,
  },
  props: {
    rooms: {
      type: Array,
    }
  },
  
}
</script>