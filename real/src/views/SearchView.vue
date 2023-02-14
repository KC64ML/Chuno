<template>
  <div id="header" style="padding: 20px">
    <img class="header_menu" src="@/assets/Search.svg" />
    <div style="margin-left: 20px"></div>
    <input class="header_menu header_input" id="room_search" v-model="roomSearch" placeholder="방을 검색해 보세요" />
  </div>
  <div v-if="search = '' || (search != '' && roomSearch.length == 0)" style="height: 80%; overflow: scroll" class="scroll">
    <room-card v-for="(room, idx) in roomList" :key="idx" v-bind:room_info="room" @info_show="showRoomInfo"
      @room_info="setRoomInfo" @info_close="closeRoomInfo">
    </room-card>
  </div>
  <div v-else>
    검색 결과 없음
  </div>
</template>

<script>
import RoomCard from "@/components/RoomCard.vue";

export default {
  components: {
    RoomCard,
  },
  data() {
    return {
      roomSearch: "",
      roomList: [],
      location: {
        lat: 0,
        lng: 0,
      },
      roomInfo: [],
      rooms: {},
      search: false,
      curRoomDB: [],
    };
  },
  created() {
    // (1) onmessage선언

    this.conn.onmessage = async (e) => {
      // console.log("------------onmessage", e);
      var content = JSON.parse(e.data);
      // console.log("======" + content.roomInfo[0].roomid);

      if (content.type == "rooms") {
        var temp = [];
        this.rooms = content.roomInfo;
        // console.log("content.roomInfo : " + content.roomInfo);
        this.rooms.forEach(room => {
          // console.log(room)

          if (this.curRoomDB.includes(room.roomid) && !temp.includes(room)) {
            // console.log("저장 : " + room);
            temp.push(room);
            // this.roomList.push(room);
          }
        });
        this.roomList = temp;

      }
    };
  },
  methods: {
    async getRoomCard() {
      // console.log("roomSearch : ", this.roomSearch);
      this.roomList = []
      // 현재 위치 정보 받기
      await this.$getLocation({ enableHighAccuracy: true }).then(
        (coordinates) => {
          this.location.lat = coordinates.lat;
          this.location.lng = coordinates.lng;
        }
      );

      // console.log("위치 정보 받기");

      // 입력되는 데이터
      const data = {
        loc: {
          lat: this.location.lat,
          lng: this.location.lng,
        },
        keyword: this.roomSearch,
      };
      console.log(data);
      this.curRoomDB = []
      await this.axios
        .post(process.env.VUE_APP_SPRING + "room/search", data)
        .then((res) => {
          const code = res.data.result;

          // console.log("search 실행")
          if (res.data.code) {
            // item 안에 해당 방에 대한 정보
            code.forEach((item) => {

              if (!this.curRoomDB.includes(item.id)) {
                this.curRoomDB.push(item.id);
              }
            });
            this.search = true;
          } else {
            console.log("code err");
          }
        });

      // (2)
      // rooms를 돌면서 제목을 검색한다.
      this.conn.send(
        JSON.stringify({
          event: "getAllRoom",
        })
      );

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
      console.log("data : ", data);
      this.roomInfo = data;
      console.log("this.roomInfo", this.roomInfo);
    },
  },
  watch: {
    roomSearch: "getRoomCard",
  },
};
</script>

<style lang="scss"   scoped>
$menu_height: 30px;

#header {
  position: absolute;
  top: 0;
  height: 60px;
  width: 100vw;
  background-color: black;
  display: flex;
  align-items: center;
  justify-content: center;
}

.header_menu {
  height: $menu_height;
}

.header_input {
  width: 70%;
  border-radius: calc($menu_height / 2);
  padding-left: calc($menu_height / 2);
}
</style>