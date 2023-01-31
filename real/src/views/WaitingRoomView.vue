<template>
    <div>
        여기는 웨이팅 룸이에요
        {{ $route }}
    </div>
</template>

<script>
// import { OpenVidu } from "openvidu-browser";

    export default {
        data() {
            return {
                roomInfo: undefined,
                hostNickName: undefined,
            }
        },
        created() {
            // 이걸로 방 번호를 알게 되었으니까 이걸로 요청을 보내요
            console.log(this.$route.params.roomId);
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
            console.log(this.roomInfo.host_id);
            // 방장 닉네임을 받아왔다 칠게요
            this.hostNickName = "asdf";

            /*
            //여기 부터 오픈비두 소스로 사용할 거같아요


            */
        }
    }
</script>

<style lang="scss" scoped>

</style>