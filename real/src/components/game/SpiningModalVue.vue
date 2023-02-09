<template>
    <div id="spining_container">
        <div style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);">
            <div style="width: 100vw; margin-bottom: 40px; font-size: 20px; text-align: center">
                열심히 정보를 가져오는 중이에요
            </div>
            <div style="display:flex; justify-content: center">
                <div class="lds-roller">
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                </div>
            </div>
            {{ user_info_list }}
        </div>
    </div>
</template>

<script>
import axios from 'axios';


export default {
    data() {
        return {
            room_id: this.$route.params.roomId,
            user_info_list: [],
            lat: 0,
            lng: 0,
        }
    },
    created() {
        navigator.geolocation.getCurrentPosition(this.getPositionValue);
    },
    methods: {
        async getPositionValue(val) {
            this.lat = val.coords.latitude;
            this.lng = val.coords.longitude;
            console.log(this.lat, this.lng)
            this.conn.onmessage = (e) => {
                console.log("---------------/----------------/---------------")
                var content = JSON.parse(e.data);
                if (content.type == 'receivelocation') {
                    console.log("위치를 받을게요");
                    console.log(e);
                }
            }

            console.log("4$$$$$111",process.env.VUE_APP_SPRING)
            var nickname = await axios.get(process.env.VUE_APP_SPRING + "user", { headers: { Authorization: sessionStorage.getItem("token") } }).then(res=>res.data.result.nickname);
            console.log("*/*/*/*/*/*//*////*/*/", nickname)
            this.conn.send(JSON.stringify({
                "event": "sendlocation",
                "nickname": nickname,
                "room": this.room_id,
                "startData": {
                    "lat": this.lat,
                    "lng": this.lng,
                }
            }))
        },
    }
}

</script>

<style lang="scss" scoped>
#spining_container {
    position: absolute;
    z-index: 10000;
    width: 100vw;
    height: 100%;
    background-image: url("@/assets/main_back.png");
    background-size: cover;
}

.lds-roller {
    display: inline-block;
    position: relative;
    width: 80px;
    height: 80px;
}

.lds-roller div {
    animation: lds-roller 1.2s cubic-bezier(0.5, 0, 0.5, 1) infinite;
    transform-origin: 40px 40px;
}

.lds-roller div:after {
    content: " ";
    display: block;
    position: absolute;
    width: 7px;
    height: 7px;
    border-radius: 50%;
    background: rgb(20, 0, 0);
    margin: -4px 0 0 -4px;
}

.lds-roller div:nth-child(1) {
    animation-delay: -0.036s;
}

.lds-roller div:nth-child(1):after {
    top: 63px;
    left: 63px;
}

.lds-roller div:nth-child(2) {
    animation-delay: -0.072s;
}

.lds-roller div:nth-child(2):after {
    top: 68px;
    left: 56px;
}

.lds-roller div:nth-child(3) {
    animation-delay: -0.108s;
}

.lds-roller div:nth-child(3):after {
    top: 71px;
    left: 48px;
}

.lds-roller div:nth-child(4) {
    animation-delay: -0.144s;
}

.lds-roller div:nth-child(4):after {
    top: 72px;
    left: 40px;
}

.lds-roller div:nth-child(5) {
    animation-delay: -0.18s;
}

.lds-roller div:nth-child(5):after {
    top: 71px;
    left: 32px;
}

.lds-roller div:nth-child(6) {
    animation-delay: -0.216s;
}

.lds-roller div:nth-child(6):after {
    top: 68px;
    left: 24px;
}

.lds-roller div:nth-child(7) {
    animation-delay: -0.252s;
}

.lds-roller div:nth-child(7):after {
    top: 63px;
    left: 17px;
}

.lds-roller div:nth-child(8) {
    animation-delay: -0.288s;
}

.lds-roller div:nth-child(8):after {
    top: 56px;
    left: 12px;
}

@keyframes lds-roller {
    0% {
        transform: rotate(0deg);
    }

    100% {
        transform: rotate(360deg);
    }
}
</style>