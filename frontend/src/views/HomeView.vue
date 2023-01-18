<template>
  <div class="home">
    <input id="islogin">
    <RoomListView
      v-for="room in rooms"
      :key="room.id"
      :room="room"
    />
  </div>
</template>

<script>
import axios from 'axios'
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
  methods: {
    async start() {
      const token = sessionStorage.getItem("token");
      if (token != null) {
          var { data } = await axios.post("http://3.34.138.191:9997/kakao/tokenConfirm", token, {headers: {'Content-Type': 'text/plain'}});
          if (data.validation == "not_valid") {
              document.getElementById("islogin").value = "로그인X";
          } else {
              document.getElementById("islogin").value = data.nickname + "님 로그인중";
          }
      }
    },
  },
  created() {
    this.start()
  },
  
}
</script>