<template>
  <div id="header">
    <img class="header_menu" src="@/assets/Search.svg" />
    <div style="margin-left: 20px"></div>
    <input
      class="header_menu header_input"
      id="room_search"
      v-model="roomSearch"
      placeholder="방을 검색해 보세요"
    />
  </div>
  <div style="height: 80%; overflow: scroll" class="scroll">
    {{ roomList }}
    <room-card
      v-for="(room, idx) in roomList"
      :key="idx"
      v-bind:room_info="room"
      @info_show="showRoomInfo"
      @room_info="setRoomInfo"
      @info_close="closeRoomInfo"
    >
    </room-card>
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
      roomInfo: {},
      rooms: {},
    };
  },
  created() {
    // (1) onmessage선언 type=rooms
    // socket, 이를 사용하기 위해서는
    // this.conn.send(JSON.stringify({
    //        "event": "getAllRoom",
    //    }))
    // 을 사용해야 한다.
    this.conn.onmessage = async (e) => {
      console.log("------------onmessage", e);
      var content = JSON.parse(e.data);
      console.log(e);
      if (content.type == "rooms") {
        this.rooms = content.roomInfo;
        console.log("content.roomInfo : " + content.roomInfo);
      }
    };
  },
  methods: {
    async getRoomCard() {
      console.log("getRoomCard ");

      // (2)
      // rooms를 돌면서 제목을 검색한다.
        
      this.conn.send(
        JSON.stringify({
          event: "getAllRoom",
        })
      );

    //   console.log("rooms : " + this.rooms);
      // 현재 들어와 있는 room
      const curRoomSocket = []
      console.log("시작");
      console.log("결과 : " + this.rooms.data)
      for (var room in this.rooms) {
        console.log("room 결과 : " + room.roomid);
        curRoomSocket.push(room)
      }
      
      console.log("반복문 시작");
      for(var r in curRoomSocket){
        console.log(r);
      }
      console.log("반복문 종료");

      // axios를 통해 모든 데이터를 가져오는데
      // this.rooms에 있는 정보라면 display(display : roomList에 넣어라)

      // ex) getAllRoom = [{roomId 3, player_len: 4}, {roomId: 4, player_len: 3}];
      // getAllRoom == roomList 룸아이디가 일치하는게 룸리스트에 없으면 그건 소켓에서 관리되고 있는 방이 아니니까roomList에서 제거 해도 되겠지??

      // 현재 위치 정보 받기
      await this.$getLocation({ enableHighAccuracy: true }).then(
        (coordinates) => {
          this.location.lat = coordinates.lat;
          this.location.lng = coordinates.lng;
        }
      );

      console.log("위치 정보 받기");

      // 입력되는 데이터
      const data = {
        loc: {
          lat: this.location.lat,
          lng: this.location.lng,
        },
        keyword: this.roomSearch,
      };
      console.log(data);

      await this.axios
        .post(process.env.VUE_APP_SPRING + "room/search", data)
        .then((res) => {
          const code = res.data.result;
          // console.log("search 실행")
          var resList = [];
          if (res.data.code) {

            // item 안에 해당 방에 대한 정보
            // includes(방Id)가 true라면 list에 저장
            code.forEach((item) => {
                // 현재 db를 통해 나온 방 정보가 소켓에 저장되어 있는 경우
                // 화면에 출력
                console.log("item.id : " + item.id);
                if(curRoomSocket.includes(item.id)){
                    resList.push(item);
                    console.log("item : "+ item)
                }
              // console.log(item + " 저장 ");
            });

            // 결과 저장
            this.roomList = resList;
          } else {
            console.log("code err");
          }
        });
      console.log("결과 : " + this.roomList);
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

<style lang="scss" scoped>
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