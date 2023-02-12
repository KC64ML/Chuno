<template>
    <div id="header">
        <img class="header_menu" src="@/assets/Search.svg">
        <div style="margin-left: 20px"></div>
        <input class="header_menu header_input" id="room_search" v-model="roomSearch" placeholder="방을 검색해 보세요">
    </div>
    <div style="height: 80%; overflow: scroll;" class="scroll">
        <!-- <div style="text-align: center">저잣거리</div> -->
        <!-- <room-card v-for="(room, idx) in roomList" :key="idx" v-bind:room="room"></room-card> -->
    </div>
</template>

<script>
// import RoomCard from "@/components/RoomCard.vue"

export default {
    components: {
        // RoomCard
    },
    data() {
        return {
            roomSearch: "",
            roomList: [
                {
                    room_id: 1,
                    title: "방이름1",
                    is_public: true,
                    password: null,
                    lat: 36.119485,
                    lng: 128.3445734,
                    radius: 50,
                    host_id: "gogo",
                    room_start_time: new Date(2023, 1, 1, 13, 20, 0)
                },
                {
                    room_id: 2,
                    title: "방이름2",
                    is_public: false,
                    password: "abc111",
                    lat: 36.119485,
                    lng: 127.3445734,
                    radius: 50,
                    host_id: "toto",
                    // month는 하나가 적어야 한다
                    room_start_time: new Date(2023, 0, 30, 12, 20, 0)
                },
            ]
        }
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