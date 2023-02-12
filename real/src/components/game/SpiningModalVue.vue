<template>
    <div id="spining_container" class="flex_center">
        <div>
            {{ location_list }}
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
            <div style="text-align: center; margin-top: 20px;">
                {{ display_info }}
            </div>
            <div class="flex_center" style="margin-top: 30px">
                <GMapMap :center="roomcenter" :zoom="map_zoom" :options="{
                    zoomControl: false,
                    mapTypeControl: false,
                    streetViewControl: false,
                    fullscreenControl: false,
                    minZoom: 1,
                    maxZoom: map_zoom,
                }" class="map_size">
                    <div v-for="(mk, idx) in location_list" :key="idx">
                        <div v-if="mk.me == true && mk.role == 'chuno'">
                            11 {{ mk.position }}
                            <GMapMarker :animation=1 :position="mk.position" />
                        </div>
                        <div v-else-if="mk.me == true && mk.role == 'slave'">
                            <GMapMarker :icon="slave_me_img" :animation=1 :position="{ lat: mk.lat, lng: mk.lng }" />
                        </div>
                        <div v-else-if="mk.me == false && mk.role == 'chuno'">
                            <GMapMarker :icon="chuno_img" :animation=1 :position="{ lat: mk.lat, lng: mk.lng }" />
                        </div>
                        <div v-else-if="mk.me == false && mk.role == 'slave'">
                            <GMapMarker :icon="slave_img" :animation=1 :position="{ lat: mk.lat, lng: mk.lng }" />
                        </div>
                    </div>
                    <GMapCircle :radius="roomradius" :center="roomcenter" :options="gameCircle" />
                </GMapMap>
                <!-- <div class="map_size mak" @click="noTouch"></div> -->
            </div>
            <div :class="{ div_hidden: !count_down_start }"
                style="text-align: center; font-size: 25px; margin-top: 20px;">
                {{ count_down }}초 후에 게임이 시작되요!
            </div>
        </div>
    </div>
</template>

<script>
import chuno_me_img from '@/assets/chuno_me_img.png'
import slave_me_img from '@/assets/slave_me_img.png'
import chuno_img from '@/assets/chuno_img.png'
import slave_img from '@/assets/slave_img.png'

export default {
    data() {
        return {
            count_down_start: false,
            count_down: 20,
            room_id: this.$route.params.roomId,
            nickname: "",
            lat: 0,
            lng: 0,
            info: undefined,
            teamchuno: [],
            teamslave: [],
            my_position: undefined,
            role: "cc",
            player_len: 0,
            cnt_len: 0,
            location_list: [],
            roomcenter: undefined,
            roomradius: 0,
            display_info: "백성을 추리고 있어요",
            gameCircle: {
                strokeColor: "#0000FF",
                strokeOpacity: 0.3,
                strokeWeight: 2,
                fillColor: "#0000FF",
                fillOpacity: 0.15,
            },
            chuno_me_img: {
                url: chuno_me_img,
                scaledSize: { width: 40, height: 40 }
            },
            slave_me_img: {
                url: slave_me_img,
                scaledSize: { width: 40, height: 40 }
            },
            chuno_img: {
                url: chuno_img,
                scaledSize: { width: 40, height: 40 }
            },
            slave_img: {
                url: slave_img,
                scaledSize: { width: 40, height: 40 }
            },
        }
    },
    computed: {
        map_zoom() {
            return 16 - (this.roomradius - 250) * 0.00224;
        }
    },
    async created() {
        this.conn.addEventListener('message', (e) => {
            var content = JSON.parse(e.data);
            if (content.type == 'receivelocation') {
                this.location_list.push({ "nickname": content.nickname, "role": content.info.role, position:{"lat": content.info.lat, "lng": content.info.lat}, "me": (content.nickname == this.nickname) ? (true) : (false) });
                this.cnt_len++;
                this.display_info = this.cnt_len + " / " + this.player_len;
                if (this.cnt_len == this.player_len) {
                    this.display_info = "모든 사용자로 부터 정보를 받아왔어요!"
                    var count = setInterval(() => {
                        this.count_down_start = true;
                        this.count_down--;
                        if (this.count_down == 0) {
                            clearInterval(count);
                            this.$emit("spinningEnd");
                        }
                    }, 1000);
                    count();
                }
            }
        })
        this.info = JSON.parse(sessionStorage.getItem("info"));
        this.roomradius = this.info.radius;
        this.roomcenter = { lat: this.info.roomlat, lng: this.info.roomlng };

        this.nickname = await this.axios.get(process.env.VUE_APP_SPRING + "user", { headers: { Authorization: sessionStorage.getItem("token") } }).then(res => res.data.result.nickname);
        this.teamchuno = this.info.teamchuno.map((e) => e.nickname);
        if (this.teamchuno.includes(this.nickname)) this.role = "chuno";
        this.teamslave = this.info.teamslave.map((e) => e.nickname);
        if (this.teamslave.includes(this.nickname)) this.role = "slave";
        this.player_len = this.teamchuno.length + this.teamslave.length;
        this.display_info = "0 / " + this.player_len;
        navigator.geolocation.getCurrentPosition(this.getPositionValue);
    },
    methods: {
        async getPositionValue(val) {
            this.lat = val.coords.latitude;
            this.lng = val.coords.longitude;

            this.conn.send(JSON.stringify({
                "event": "sendlocation",
                "nickname": this.nickname,
                "room": this.room_id,
                "startData": {
                    "role": this.role,
                    "lat": this.lat,
                    "lng": this.lng,
                }
            }))
        },
        noTouch(e) {
            e.stopPropagation();
        }
    }
}

</script>

<style lang="scss" scoped>
$map_width: 100vw * 0.9;
$map_height: $map_width;

.div_hidden {
    visibility: hidden;
}

.map_size {
    height: $map_height;
    width: $map_height;
}

.mak {
    position: absolute;
    // background-color: rgb(100, 0, 0, 0.5);
}

#spining_container {
    position: absolute;
    z-index: 100001;
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