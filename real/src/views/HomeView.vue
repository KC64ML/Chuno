<template>
  <CreateRoomModal v-if="this.$store.state.CreateRoomModal"/>
    <HeaderVue :title="'홈'"></HeaderVue>
    <div id="room_box" style="height: 75%; overflow: scroll;">
        <room-card v-for="(room, idx) in roomList" :key="idx" v-bind:room="room"></room-card>
    </div>
    <div id="plus_button" @click="createRoom">+</div>
</template>

<script>
import HeaderVue from "@/components/HeaderVue.vue"
import RoomCard from "@/components/RoomCard.vue"
import CreateRoomModal from '@/components/home/CreateRoomModal.vue'
// import variables from "@/assets/scss/_variable.scss"

export default {
    components: {
        HeaderVue,
        RoomCard,
        CreateRoomModal,
    },
    data() {
        return {
            lat: undefined,
            lng: undefined,
            room_title: "",
            roomSearch: "",
            roomList: [],
        }
    },
    async created() {
        navigator.geolocation.getCurrentPosition(this.getPositionValue);
    },
    mounted() {
    },
    methods: {
        async getPositionValue (val) {
            this.lat = val.coords.latitude;
            this.lng = val.coords.longitude;
            this.roomList = await this.axios.get(process.env.VUE_APP_SPRING + "room?lat=" + this.lat + "&lng=" + this.lng).then(res => res.data.result);
        },
        createRoom() {
            // alert("눌렸어요")
            this.$store.state.CreateRoomModal = true
        },
        fff() {
            alert('fffff');
        }
    }
}
</script>

<style lang="scss" scoped>
@import "@/assets/scss/_variable.scss";
    #plus_button {
        position: absolute;
        background-color: #181816;
        color: white;
        box-shadow: 0 5px 5px rgb(0, 0, 0, 0.5) ;
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
        top : 50%;
        transform: translate(-50%, -50%);
        background-color: #F2F2F2;
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