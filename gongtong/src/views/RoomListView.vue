<template>
    <div container>

    </div>
    <div id="header">
        <img src="@/assets/main_logo2.png" style="height: 50px; position: absolute; left: 20px;">
        <div style="font-size: 30px;">저잣거리</div>
    </div>
    <div style="height: 75%">
        <!-- <div style="text-align: center">저잣거리</div> -->
        <room-card v-for="(room, idx) in roomList" :key="idx" v-bind:room="room"></room-card>
    </div>
    <div id="plus_button" @click="makeRoom">+</div>
</template>

<script>
import RoomCard from "@/components/RoomCard.vue"

export default {
    components: {
        RoomCard
    },
    data() {
        return {
            make_room_modal: false,
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
    },
    created() {
        console.log(this.$store.state.nickname);
    },
    methods: {
        makeRoom() {
            alert('ffff');
        }
    }
}
</script>

<style lang="scss" scoped>
@import "@/assets/scss/variable.scss";
$plus_button_size: 50px;

    #header {
        position: absolute;
        top: 0;
        height: 60px;
        width: 100vw;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    #plus_button {
        position: absolute;
        background-color: #181816;
        color: white;
        box-shadow: 0 5px 5px rgb(0, 0, 0, 0.5) ;
        right: 0;
        bottom: calc($footer_height + 20px);
        right: 20px;
        width: $plus_button_size;
        height: $plus_button_size;
        border-radius: calc($plus_button_size / 2);
        line-height: $plus_button_size;
        text-align: center;
        font-weight: bold;
        font-size: 30px;
    }
</style>