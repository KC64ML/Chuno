<template>
  <PasswordModal
    v-if="pass_show"
    @pass_close="closePass()"
    :roomInfo="roomInfo"
    @isEnter="setIsEnter"
  ></PasswordModal>
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
  <div v-if="!connect_try">
    <div class="flex_center">
      <div class="lds-ripple"><div></div><div></div></div>
    </div>
    <div class="yura_yura" style="text-align: center; margin-top: 40px; font-size: 20px; width: 100vw; overflow: hidden;">저잣거리를 둘러 보고 있어요.</div>
  </div>
  <div v-else style="height: 80%;">
    <div v-if="roomList.length == 0" style="position: absolute; left: 50%; top: 50%; transform: translate(-50%, -50%)">
      <div class="sad_face" style="text-align: center; font-size: 40px;">
        ㅠ.ㅠ
      </div>
      <div style="text-align: center;">
        참가 가능한 방이 없어요
      </div>
    </div>
    <div id="room_box" style="height: 100%; overflow: scroll" v-else>
      <RoomCard
        v-for="(room, idx) in roomList"
      :key="idx"
      :room_info="room"
      @click="play(this.door)"
      @info_show="showRoomInfo"
      @room_info="setRoomInfo"
      @info_close="closeRoomInfo"
      @show_pass="showPassword"
      :isEnter="isEnter"
      ></RoomCard>
    </div>
  </div>
  <div id="plus_button" @click="createRoom">+</div>
</template>

<script>
import NewCreateRoomModal from "@/components/home/NewCreateRoomModal.vue";
import HeaderVue from "@/components/HeaderVue.vue";
import RoomCard from "@/components/RoomCard.vue";
import RoomInfoModal from "@/components/home/RoomInfoModal.vue";
import PasswordModal from "@/components/home/PasswordModal.vue";

import door from "@/assets/audio/wood_door.mp3";
import tak from "@/assets/audio/tak.mp3";

export default {
  components: {
    NewCreateRoomModal,
    HeaderVue,
    RoomCard,
    RoomInfoModal,
    PasswordModal,
  },
  data() {
    return {
      connect_try: false,
      try_error: false,

      modal_show: false,
      info_show: false,
      pass_show: false,
      lat: 0,
      lng: 0,
      roomList: [],
      roomInfo: {},
      door,
      isEnter: false,
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
      // console.log(this.lat, this.lng);
      this.enrollEvent();
    },
    enrollEvent() {
      new Promise((resolve) => {
        this.conn.onmessage = async (e) => {
          var content = JSON.parse(e.data);
          // console.log("소켓에서 온 메세지", content);
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
            this.connect_try = true;
            // console.log("지금 있는 모든 방을 조회해요");
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
            this.connect_try = true;
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
      this.sendData({
          event: "getAllRoom", // rooms와 연결되어있음
        }
      );
    },
    createRoom() {
      this.modal_show = true;
      this.play(tak);
    },
    modalOff() {
      this.modal_show = false;
    },
    showRoomInfo() {
      // console.log("showRoomInfo");
      this.info_show = true;
    },
    closeRoomInfo() {
      // console.log("closeRoomInfo");
      this.info_show = false;
    },
    setRoomInfo(data) {
      // console.log("data", data);
      this.roomInfo = data;
      // console.log("this.roomInfo", this.roomInfo);
    },
    play(audiofile) {
      var audio = {
        file: new Audio(audiofile),
      };
      audio.file.play();
    },
    closePass() {
      // console.log("closePass");
      this.pass_show = false;
    },
    showPassword() {
      // console.log("showPass");
      this.pass_show = true;
    },
    setIsEnter() {
      this.isEnter = true;
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
  z-index: 2;
}
@keyframes plus_button {
  0% {
    transform: scale(1.1, 0.9);
  }
  50% {
    transform: translateY(-50%) scale(0.95, 1.1)
  }
  100% {
    transform: scale(1.1, 0.9);
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

.lds-ripple {
  display: inline-block;
  position: relative;
  width: 80px;
  height: 80px;
}
.lds-ripple div {
  position: absolute;
  border: 4px solid rgb(0, 0, 0);
  opacity: 1;
  border-radius: 50%;
  animation: lds-ripple 1s cubic-bezier(0, 0.2, 0.8, 1) infinite;
}
.lds-ripple div:nth-child(2) {
  animation-delay: -0.5s;
}
@keyframes lds-ripple {
  0% {
    top: 36px;
    left: 36px;
    width: 0;
    height: 0;
    opacity: 0;
  }
  4.9% {
    top: 36px;
    left: 36px;
    width: 0;
    height: 0;
    opacity: 0;
  }
  5% {
    top: 36px;
    left: 36px;
    width: 0;
    height: 0;
    opacity: 1;
  }
  100% {
    top: 0px;
    left: 0px;
    width: 72px;
    height: 72px;
    opacity: 0;
  }
}
.sad_face {
  animation-name: sad_face;
  animation-duration: 1.2s;
  animation-iteration-count: infinite;
}
@keyframes sad_face {
  0% {
    transform: scale(1);
  }
  50% {
    transform: translateY(-10px) scale(1.1, 0.9);
  }
  75% {
    transform: scale(0.9, 1.1);
  }
  85% {
    transform: translateY(-8px) scale(1.05,0.95);
  }
  95% {
    transform: scale(0.9, 1.1);
  }
  100% {
    transform: scale(1);
  }

}
</style>