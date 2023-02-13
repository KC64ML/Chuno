<template>
    <div id="modal_back">
    <div id="make_room_modal">
        <div id="close_button" @click="offing">x</div>
        <div id="modal_title" style="font-size: 24px">방 만들기</div>
        <div v-if="page1">
        <table style="width: 300px; margin: 0 auto">
            <colgroup>
            <col style="width: 90px" />
            <col style="width: 200px" />
            </colgroup>
            <tr>
            <td>방 제목</td>
            <td>
                <input v-model="title" style="padding: 0 20px" />
            </td>
            </tr>
            <tr>
            <td>최대인원</td>
            <td>
                <div>
                <div @click="minus" class="plma_button">-</div>
                <div style="margin: 0 30px">{{ max_player }}</div>
                <div @click="plus" class="plma_button">+</div>
                </div>
            </td>
            </tr>
            <tr>
            <td>공개설정</td>
            <td>
                <div>
                <div
                    @click="publicGame"
                    class="option_button option_left"
                    :class="is_public ? 'selected' : 'unselected'"
                >
                    공개
                </div>
                <div
                    @click="secretGame"
                    class="option_button option_right"
                    :class="!is_public ? 'selected' : 'unselected'"
                >
                    비밀
                </div>
                </div>
            </td>
            </tr>
            <tr v-if="!is_public">
            <td>비밀번호</td>
            <td>
                <input
                    v-model="password"
                    style="padding: 0 20px"
                    type="password"
                />
            </td>
            </tr>
            <tr>
            <td>예약</td>
            <td>
                <div>
                <div
                    @click="today"
                    class="option_button option_left"
                    :class="is_today ? 'selected' : 'unselected'"
                >
                    오늘
                </div>
                <div
                    @click="tomorrow"
                    class="option_button option_right"
                    :class="!is_today ? 'selected' : 'unselected'"
                >
                    내일
                </div>
                </div>
            </td>
            </tr>
            <tr>
            <td></td>
            <td>
                <div>
                <div class="hour_type" v-if="is_am" @click="amChange">오전</div>
                <div class="hour_type" v-else @click="amChange">오후</div>
                <div style="display: flex; align-items: center">
                    <input
                    class="time_input"
                    v-model="hour"
                    type="number"
                    max="23"
                    min="0"
                    />
                    <div style="margin-right: 15px">시</div>
                </div>
                <div style="display: flex; align-items: center">
                    <input
                    class="time_input"
                    v-model="minute"
                    type="number"
                    max="59"
                    min="0"
                    />
                    <div>분</div>
                </div>
                </div>
            </td>
            </tr>
        </table>
        <div class="flex_center hover_pointer" @click="nextButton1">
            <img
            src="@/assets/main_button1.png"
            id="button1"
            style="width: 140px"
            />
            <div class="image_text">다음</div>
        </div>
        </div>
        <div v-if="page2">
        <div style="text-align: center">반경 {{ radius }}m</div>
        <div>
            <GMapMap
            :center="player"
            :zoom="16 - (radius - 250) * 0.0026"
            :options="{
                zoomControl: false,
                mapTypeControl: false,
                streetViewControl: false,
                fullscreenControl: false,
                minZoom: 11,
                maxZoom: 18,
            }"
            style="width: 95%; height: 400px; margin: 0 auto 30px"
            >
            <div>
                <GMapMarker :animation="4" :position="player" />
                <GMapCircle
                :radius="radius"
                :center="player"
                :options="circleOptions"
                />
            </div>
            </GMapMap>
        </div>
        <div>
            <Slider
            v-model="radius"
            :min="300"
            :max="1000"
            :step="50"
            style="width: 90%; margin: 0 auto"
            ></Slider>
        </div>
        <div style="display: flex; justify-content: center; margin-top: 40px">
            <div class="flex_center hover_pointer" @click="previous">
            <img
                src="@/assets/main_button1.png"
                id="button1"
                style="width: 140px"
            />
            <div class="image_text">이전</div>
            </div>
            <div class="flex_center hover_pointer" @click="offing">
            <img
                src="@/assets/main_button1.png"
                id="button1"
                style="width: 140px"
            />
            <div class="image_text">확인</div>
            </div>
        </div>
        </div>
    </div>
    </div>
</template>

<script>
import Slider from "@vueform/slider";

export default {
    props: {
    player: undefined,
    room_info: {},
    },
    components: {
    Slider,
    },
    created() {
    this.title = this.room_info.title;
    this.max_player = this.room_info.max_player;
    this.radius = this.room_info.radius;
    this.password = this.room_info.password;
    if (this.room_info.password) {
        this.is_public = false;
    } else {
        this.is_public = true;
    }
    this.hour = this.room_info.dateTime.hour;
    this.minute = this.room_info.dateTime.minute;
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth + 1;
    var yyyy = today.getFullYear();
    if (
        yyyy == this.room_info.dateTime.year &&
        mm == this.room_info.dateTime.month &&
        dd == this.room_info.dateTime.day
    ) {
    }
    },
    data() {
    return {
        title: "",
        max_player: 4,
        is_public: true,
        password: "",
        is_today: true,
        hour: null,
        minute: null,
        page1: true,
        page2: false,
        circleOptions: {
        strokeColor: "#0000FF",
        strokeOpacity: 0.3,
        strokeWeight: 2,
        fillColor: "#0000FF",
        fillOpacity: 0.15,
        },
        is_am: true,
        radius: 750,
        room: {},
    };
    },
    methods: {
    offing() {
        this.$emit("modal_off");
    },
    nextButton1() {
        console.log(this.hour + (this.is_am ? 0 : 12), this.minute);
        if (this.title == "") {
        alert("제목을 입력해 주세요");
        return;
        }
        if (
            this.hour > 12 ||
            this.hour <= 0 ||
            this.minute < 0 ||
            this.minute >= 60
        ) {
            alert("시간을 확인해 주세요");
            return;
        }
        this.page1 = false;
        this.page2 = true;
    },
    minus() {
        if (this.max_player <= 4) return;
        this.max_player -= 2;
    },
    plus() {
        if (this.max_player >= 8) return;
        this.max_player += 2;
    },
    publicGame() {
        this.is_public = true;
    },
    secretGame() {
        this.is_public = false;
    },
    today() {
        this.is_today = true;
    },
    amChange() {
        this.is_am = !this.is_am;
    },
    tomorrow() {
        this.is_today = false;
    },
    previous() {
        this.page1 = true;
        this.page2 = false;
        },
    },
};
</script>

<style src="@vueform/slider/themes/default.css"></style>
<style lang="scss" scoped>
$input_height: 30px;
$plma_size: 30px;

#modal_back {
    position: absolute;
    width: 100vw;
    height: 100%;
    background: rgb(0, 0, 0, 0.6);
    z-index: 1;
}

#make_room_modal {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 85%;
    padding: 30px 0;
    background-color: #f2f2f2;
    transform: translate(-50%, -50%);
    font-size: 18px;
    border-radius: 10px;
    animation-name: modal_appearance;
    animation-iteration-count: 1;
    animation-duration: 0.8s;
}

#close_button {
    position: absolute;
    top: 10px;
    right: 20px;
    font-size: 20px;
}

#modal_title {
    text-align: center;
    margin-bottom: 20px;
}

input {
    height: $input_height;
    background-color: rgb(67, 64, 57, 0.3);
    border-radius: $plma_size / 2;
}

table {
    border-spacing: 0 20px;
}

td:nth-child(1) {
    text-align: center;
}

td:nth-child(2) > div {
    display: flex;
    align-items: center;
}
.hour_type {
    background-color: #141414;
    color: #f2f2f2;
    height: $plma_size;
    line-height: $plma_size;
    text-align: center;
    width: 40px;
    border-radius: $plma_size / 4;
    margin-right: 8px;
}
.time_input {
    width: 40px;
    text-align: center;
}

.plma_button {
    background-color: #141414;
    color: #f2f2f2;
    width: $plma_size;
    height: $plma_size;
    line-height: $plma_size;
    text-align: center;
    border-radius: $plma_size / 4;
}

.option_button {
    height: $plma_size;
    padding: 0 20px;
    line-height: $plma_size;
    color: white;
}

.option_left {
    border-top-left-radius: $plma_size / 4;
    border-bottom-left-radius: $plma_size / 4;
}

.option_right {
    border-top-right-radius: $plma_size / 4;
    border-bottom-right-radius: $plma_size / 4;
}

.selected {
    background-color: #141414;
}

.unselected {
    background-color: rgb(67, 64, 57, 0.3);
}

@keyframes modal_appearance {
    0% {
    transform: translate(-50%, -50%) scale(0);
    }
    70% {
    transform: translate(-50%, -50%) scale(1.2);
    }
    85% {
    transform: translate(-50%, -50%) scale(0.9);
    }
    95% {
    transform: translate(-50%, -50%) scale(1.05);
    }
}
</style>