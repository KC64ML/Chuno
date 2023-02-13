<template>
  <RoomInfoModal
    v-if="info_show"
    :roomInfo="roomInfo"
    @info_close="closeRoomInfo()"
  ></RoomInfoModal>
  <NewCreateRoomModal
    v-if="modal_show"
    @modal_off="modalOff()"
    :player="{ lat: lat, lng: lng }"
  />
  <HeaderVue :title="'저잣거리'"></HeaderVue>
  <div id="room_box" style="height: 80%; overflow: scroll">
    <room-card
      v-for="(room, idx) in roomList"
      :key="idx"
      v-bind:room_info="room"
      @info_show="showRoomInfo"
      @room_info="setRoomInfo"
      @info_close="closeRoomInfo"
    ></room-card>
  </div>
  <div id="plus_button" @click="createRoom">+</div>
</template>

<script>
import NewCreateRoomModal from "@/components/home/NewCreateRoomModal.vue";
import HeaderVue from "@/components/HeaderVue.vue";
import RoomCard from "@/components/RoomCard.vue";
import RoomInfoModal from "@/components/home/RoomInfoModal.vue";

export default {
  components: {
    NewCreateRoomModal,
    HeaderVue,
    RoomCard,
    RoomInfoModal,
  },
  data() {
    return {
      modal_show: false,
      info_show: false,
      lat: 0,
      lng: 0,
      roomList: [],
      roomInfo: {},
    };
  },
  async created() {
    navigator.geolocation.getCurrentPosition(this.getPositionValue);
  },
  async mounted() {},
  methods: {
    async getPositionValue(val) {
      this.lat = val.coords.latitude;
      this.lng = val.coords.longitude;
      //룸 아이디가 담긴 리스트를 드리면 내 위치를 기준으로 해서 정렬하여 주세요
      console.log(this.lat, this.lng);
      this.enrollEvent();
    },
    enrollEvent() {
      new Promise((resolve) => {
        this.conn.onmessage = async (e) => {
          var content = JSON.parse(e.data);
          console.log("소켓에서 온 메세지", content);
          if (content.type == "all_room") {
            var temp_room = await this.axios
              .get(
                process.env.VUE_APP_SPRING +
                  "room?lat=" +
                  this.lat +
                  "&lng=" +
                  this.lng
              )
              .then((res) => res.data.result);
            this.roomList = temp_room;
            console.log("지금 있는 모든 방을 조회해요");
            var rooms = content.roomInfo;

            // rooms = rooms.map((f) => {return f.roomid})
            // console.log(rooms)
            // 룸 아이디를 담은 배열과 위도, 경도를 주면 현재 위도 경도를 기준으로 배열에 담긴 방넘버를 가까운 순으로 정렬해 주세요
            // rooms = await this.axios.post(process.env.VUE_APP_SPRING + "sortRoomList", {
            //     "roomList": rooms,
            //     lat: this.lat,
            //     lng: this.lng,
            // })
            this.roomList = rooms;
          } else if (content.type == "rooms") {
            rooms = content.roomInfo;
            //비동기로 느려지는거 실험
            // var tt = await this.axios.post("https://i8d208.p.ssafy.io/api/room/startRoom", {
            //     "roomId": 3,
            //     "userIdList": [
            //         1, 2, 3, 4, 5, 6
            //     ]
            // })
            // console.log(tt);
            // rooms = rooms.map((f) => {return f.roomid})
            // console.log(rooms)
            // 룸 아이디를 담은 배열과 위도, 경도를 주면 현재 위도 경도를 기준으로 배열에 담긴 방넘버를 가까운 순으로 정렬해 주세요
            // rooms = await this.axios.post(process.env.VUE_APP_SPRING + "sortRoomList", {
            //     "roomList": rooms,
            //     lat: this.lat,
            //     lng: this.lng,
            // })
            this.roomList = rooms;
          } else if (content.type == "error") {
            console.log(content.msg);
          }
        };
        resolve();
      }).then(() => {
        this.init();
      });
    },
    init() {
      this.conn.send(
        JSON.stringify({
          event: "getAllRoom", // rooms와 연결되어있음
        })
      );
    },
    createRoom() {
      this.modal_show = true;
    },
    modalOff() {
      this.modal_show = false;
    },
    showRoomInfo() {
      console.log("showRoomInfo");
      this.info_show = true;
    },
    closeRoomInfo() {
      console.log("closeRoomInfo");
      this.info_show = false;
    },
    setRoomInfo(data) {
      console.log("data", data);
      this.roomInfo = data;
      console.log("this.roomInfo", this.roomInfo);
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/assets/scss/_variable.scss";

#plus_button {
  position: absolute;
  background-color: #181816;
  color: white;
  box-shadow: 0 5px 5px rgb(0, 0, 0, 0.5);
  bottom: calc($footer_height + 20px);
  right: 20px;
  width: $plus_button_size;
  height: $plus_button_size;
  border-radius: calc($plus_button_size / 2);
  line-height: $plus_button_size;
  text-align: center;
  font-weight: bold;
  font-size: 30px;
  animation-name: plus_button;
  animation-iteration-count: infinite;
  animation-duration: 1s;
}
@keyframes plus_button {
  50% {
    transform: translateY(-50%);
  }
}

// 모달용 css
#modal_container {
  z-index: 99;
  position: absolute;
  background-color: $modal_background;
  width: 100vw;
  height: 100%;
}

.modal {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  background-color: #f2f2f2;
  padding: 20px;
  border-radius: 10px;
}

#room_box {
  -ms-overflow-style: none;
  scrollbar-width: none;
}

#room_box::-webkit-scrollbar {
  display: none;
}
</style>