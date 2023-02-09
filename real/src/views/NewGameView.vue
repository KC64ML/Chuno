<template>
    <NewOpenViduVue :my_cam_modal="my_cam_modal"></NewOpenViduVue>
    <div id="footer_container">
        <div class="menu_box flex_center" @click="chat_log_button();">
            <img class="menu" src="@/assets/game_chat.png">
        </div>
        <div>
            <!-- 채팅창 수정 필요 -->
            <input class="map_search" placeholder="채팅을 입력해 주세요" v-model="chat_massage">
        </div>
        <div class="menu_box" @click="myCam">
            <img class="menu" src="@/assets/game_myCam.png">
        </div>
        <div class="menu_box" @click="onMenu">
            <img class="menu" src="@/assets/game_menu.png">
        </div>
    </div>
</template>

<script>
import NewOpenViduVue from "@/components/game/NewOpenViduVue.vue";

export default {
    components: {
        NewOpenViduVue,
    },
    data() {
        return {
            my_cam_modal: false,
            chat_massage: "",
        }
    },
    methods: {
        chat_log_button() {
            alert("챗로그");
        },
        myCam() {
            this.my_cam_modal = !this.my_cam_modal;
        },
        onMenu() {
            alert("온메뉴");
        }
    }
}
</script>

<style lang="scss" scoped>
@import "@/assets/scss/variable.scss";
$button_width: 60px;

#footer_container {
    position: absolute;
    bottom: 0;
    background-color: black;
    height: $footer_height;
    width: 100vw;
    display: grid;
    grid-template-columns: $button_width 1fr $button_width $button_width;
    justify-content: space-around;
    align-items: center;
}

#footer_container>div {
    width: 100%;
}

.menu-window {
    float: right;
    position: absolute;
    bottom: 60px;
}

.menu_box {
    width: 20%;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.map_search {
    display: block;
    margin: 0 auto;
    width: 95%;
    height: 40px;
    background-color: #F1F1F180;
    color: white;
    padding: 0 10px;
    border-radius: 15px;
    font-size: 20px;
}

.map_search::placeholder {
    color: rgba(255, 255, 255, 0.56)
}
</style>