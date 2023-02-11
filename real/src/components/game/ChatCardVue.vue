<template>
    <div style="font-size: 20px;">
        <div v-if="chat_dto.nickname == 'system'">
            <div class="flex_center">
                <img src="@/assets/system_chat.png" style="height: 60px; width: 90%;">
                <div class="image_text">{{ chat_dto.msg }}</div>
            </div>
            
        </div>
        <div v-else>
            <div v-if="chat_dto.nickname == my_nickname" style="text-align: right;" class="chat_box">
                <div style="margin-bottom: 5px;">{{ chat_dto.nickname }}</div>
                <div class="chat_msg yellow">{{ chat_dto.msg }}</div>
            </div>
            <div v-else style="text-align: left;" class="chat_box">
                <div style="margin-bottom: 5px;">{{ chat_dto.nickname }}</div>
                <div class="chat_msg white">{{ chat_dto.msg }}</div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        props: {
            chat_dto: Object,
            my_nickname: String,
        }
    }
</script>

<style lang="scss" scoped>
@import "@/assets/scss/variable.scss";
$chat_msg_height: 30px;
.chat_box {
    margin: 0 20px 20px;
}
.chat_msg {
    height: $chat_msg_height;
    padding: 0 15px;
    border-radius: $chat_msg_height / 4;
    box-shadow: 0 5px 5px $shadow_color;
    line-height: $chat_msg_height;
    display: inline-block;
}

.yellow {
    background-color: rgb(228, 228, 98);
}

.white {
    background-color: rgb(210, 210, 210);
}

</style>