<template>
    <div id="chat_container" style="padding: 20px 0;">
        <div id="chat_header"
            style="display: flex; align-items:center; justify-content: space-between; margin-bottom: 10px;">
            <div>
                <img src="@/assets/main_logo2.png" alt="" style="width: 50px; height: 50px; margin-left: 20px;">
            </div>
            <div style="font-size: 30px">
                전언{{ signal }}
            </div>
            <div style="font-size: 25px; margin-right: 20px;">X</div>
        </div>
        sdfs
        <div v-for="(e, idx) in chat_log" :key="idx">
            {{ e }}
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            latest_chat: "",
            chat_log: [],
        }
    },
    props: {
        signal: Number,
        chat_data: String,
    },
    watch: {
        signal: function(value, oldvalue) {
            alert(this.chat_data, value, oldvalue);

            // this.conn.send(JSON.stringify({
            //     "event": "chat",
            //     "room": this.room_info.id,
            //     "nickname": this.user.nickname,
            //     "level": this.user.level,
            //     "msg": this.chat_data
            // }))

            this.$emit("clearChatData")
        }
    },
    methods: {
    }
}
</script>

<style lang="scss" scoped>
@import "@/assets/scss/variable.scss";

#chat_container {
    z-index: 1010;
    position: absolute;
    bottom: $footer-height;
    background-image: url("@/assets/main_back_horizon.png");
    background-size: cover;
    width: 100vw;
    max-height: 40%;
    min-height: 200px;
    overflow-y: scroll;
}
</style>