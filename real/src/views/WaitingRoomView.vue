<template>
    <HeaderVue :title="roomInfo.title"></HeaderVue>
    <div id="dot_menu" @click="dot_menu">
        <img src="@/assets/dot_menu.svg">
    </div>
    <div>
        여기는 웨이팅 룸이에요
    </div>
</template>

<script>
import HeaderVue from '@/components/HeaderVue.vue';
// import { OpenVidu } from "openvidu-browser";

    export default {
        components: {
            HeaderVue
        },
        data() {
            return {
                roomInfo: undefined,
                hostNickName: undefined,
                subscribers: [],
            }
        },
        created() {
            // 이걸로 방 번호를 알게 되었으니까 이걸로 요청을 보내요
            console.log("방번호:" + this.$route.params.roomId);
            // 가상으로 응답을 받았다 칠게요
            this.roomInfo = {
                title: "방이름이에요",
                is_public: true,
                password: null,
                lat: 36.53534,
                lng: 126.25235,
                host_id: 3,
                radius: 500
            }
            // 이걸로 방장의 아이디키를 아니 방장 닉네임을 가져올게요
            console.log("방장아이디:" + this.roomInfo.host_id);
            // 방장 닉네임을 받아왔다 칠게요
            this.hostNickName = "asdf";

            // 오픈비두에서 subscribers들을 받아오는 소스로 부터 subscribers를 추출해요
            this.subscribers = ["conn1", "conn2", "conn3", "coon4"]

            

            /*
            //여기 부터 오픈비두 소스로 사용할 거같아요
                // 세션 연결하고 자기 이름을 메세지로 모내서 누군지 그이름을 subscribers에 저장하면 될듯

            */
        },
        methods: {
            dot_menu() {
                alert('ffff')
            }
        }
    }
</script>

<style lang="scss" scoped>
@import "@/assets/scss/variable";
    #dot_menu {
        position: absolute;
        top: $header_margin;
        right: 20px;
        height: $header_height;
        display: flex;
        align-items: center;
    }
    #dot_menu > img {
        display: block;
        height: 60%
    }

</style>