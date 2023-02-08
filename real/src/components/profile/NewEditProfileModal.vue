<template>
    <div id="modal_back">
        <div id="make_room_modal">
            <div id="close_button" @click="offing">x</div>
            <div id="modal_title" style="font-size: 24px;">프로필 편집</div>
            <div v-if="page1">
                <table style="width: 300px; margin: 0 auto;">
                    <colgroup>
                        <col style="width: 90px">
                        <col style="width: 200px">
                    </colgroup>
                    <tr id="profile_image">
                        <div v-if="this.img_url">
                        <div id="profile_background">
                            <img id="preview_img" :src="this.img_url"/>
                        </div>
                        <div>
                            <button @click="clearImage" >다시 선택할래요</button>
                        </div>
                        </div>
                        <div v-else>
                        <img id="blank_img" src="@/assets/profile_default_with_cam.svg" alt="" @click="profile_click">
                        </div>
                        <input ref="file_input" type="file" @change="oneFileSelect" style="display:none"/>
                    </tr>
                    <tr>
                        <td>닉네임</td>
                        <td>
                            <input v-model="title" style="padding: 0 20px">
                        </td>
                    </tr>
                    <tr>
                        <td>전화번호</td>
                        <td>
                            <input v-model="phone" style="padding: 0 20px" maxlength="12">
                        </td>
                        <td class="notice grey">전화번호는 '-' 없이 숫자만 입력해주세요.</td>
                    </tr>
                </table>
                <div class="flex_center hover_pointer" @click="save">
                    <img src="@/assets/main_button1.png" id="button1" style="width: 140px">
                    <div class="image_text">저장</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import Slider from '@vueform/slider'

export default {
    props: {
    },
    data() {
        return {
            name: '',
            phone: '',

        }
    },
    methods: {
        offing() {
            this.$emit("modal_off")
        },
        
        
    },
        
}
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

td:nth-child(2)>div {
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
.grey {
  color: gray;
}
</style>