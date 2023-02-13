<template>
    <div id="header">
        <img class="header_menu" src="@/assets/Search.svg">
        <div style="margin-left: 20px"></div>
        <input class="header_menu header_input" id="room_search" v-model="roomSearch" placeholder="방을 검색해 보세요">
    </div>
    <div style="height: 80%; overflow: scroll;" class="scroll">
        {{ roomList }}
        <room-card v-for="(room, idx) in roomList" :key="idx" v-bind:room_info="room" @info_show="showRoomInfo"
            @room_info="setRoomInfo" @info_close="closeRoomInfo">
        </room-card>
    </div>
</template>

<script>
import RoomCard from "@/components/RoomCard.vue"

export default {
    components: {
        RoomCard
    },
    data() {
        return {
            roomSearch: "",
            roomList: [
            ],
            location: {
                lat: 0,
                lng: 0
            },
            roomInfo: {},
            rooms: {}
        }
    },
    created() {
        // (1) onmessage선언 type=rooms
        this.conn.onmessage = async (e) => {
            var content = JSON.parse(e.data);
            if (content.type == "rooms") {
                this.rooms = content.roomInfo;
                console.log("content.roomInfo : " + content.roomInfo);
            }
        };
    },
    methods: {
        async getRoomCard() {
            // (2)
            // rooms를 돌면서 제목을 검색한다.
            // 제목이 검색어에 부합하는가 display  변수에 넣을 것
            for(const room in this.rooms){
                console.log("room : " + room.roomInfo);
            }

            // axios를 통 해모든 데이터를 가져오는데
            // this.rooms에 있는 정보라면 display(display : roomList에 넣어라)

            //     this.conn.send()
            //         소켓으로 부터 관리되고 있는 방목록을 받거든


            //         (2) this.conn.send(JSON.stringify( {
            //     event: getAllRoom
            // }

            // ex) getAllRoom = [{roomId 3, player_len: 4}, {roomId: 4, player_len: 3}];
            // getAllRoom == roomList 룸아이디가 일치하는게 룸리스트에 없으면 그건 소켓에서 관리되고 있는 방이 아니니까roomList에서 제거 해도 되겠지??

            // 현재 위치 정보 받기
            await this.$getLocation({ enableHighAccuracy: true })
                .then((coordinates) => {
                    this.location.lat = coordinates.lat
                    this.location.lng = coordinates.lng
                }
                )

            console.log("위치 정보 받기")

            // 입력되는 데이터
            const data = {
                "loc": {
                    "lat": this.location.lat,
                    "lng": this.location.lng
                },
                "keyword": this.roomSearch
            }

            await this.axios.post(process.env.VUE_APP_SPRING + 'room/search', data)
                .then((res) => {
                    const code = res.data.result;
                    // console.log("search 실행")
                    var resList = []
                    if (res.data.code) {
                        // console.log("code : "  + code);
                        code.forEach((item) => {
                            resList.push(item);
                            // console.log(item + " 저장 ");
                        });

                        // 결과 저장
                        this.roomList = resList;
                    } else {
                        console.log('code err')
                    }
                }
                )
            // console.log("결과 : " + this.roomList);
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
        'roomSearch': 'getRoomCard'
    }
}
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