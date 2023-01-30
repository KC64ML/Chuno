<template>
    <div id="container">
        <div style="display: flex; align-items: center;">
            <img src="@/assets/Lock.svg" v-if="room.is_public">
            <img src="@/assets/Unlock.svg" v-else>
            <div style="margin-left: 10px"></div>
            {{ room.title }}
        </div>
        <div style="margin-top: 10px"></div>
        <!-- stretch는 위아래로 모든 div의 높이를 같게 해줘요 -->
        <div style="display: flex; align-items: stretch;">
            <!-- flex: 1 은 남은공간을 이 div가 모두 가져가게 해 줘요 -->
            <div class="card_menu flex_center" style="color: white; flex: 1">
                <img src="@/assets/Clock.svg">
                <div class="start_time" v-if="this.room.room_start_time < this.now">진행중</div>
                <div class="start_time" v-else>
                    {{ `${this.month}월 ${this.date}일 ${this.hour}시 ${this.minute}분` }}
                </div>
            </div>

            <div class="card_margin"></div>

            <div class="card_menu">
                <img src="@/assets/Notification.svg" />
            </div>

            <div class="card_margin"></div>

            <div class="card_menu">
                <img src="@/assets/Info.svg" />
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        props: {
            room: undefined
        },
        data() {
            return {
                // 후에 서버에서 시간 가져오는 걸로 할게요
                now: Date.now(),
                // 2월은 1월로 표시됨
                month: this.room.room_start_time.getMonth() + 1,
                date: this.room.room_start_time.getDate(),
                hour: this.room.room_start_time.getHours() % 12,
                minute: this.room.room_start_time.getMinutes()
            }
        },
        created() {
        }
    }
</script>

<style lang="scss" scoped>
    @import "@/assets/scss/_variable.scss";

    #container {
        margin: $card_vertical_margin 0;
        background: #F2F2F2;
        height: 120px;
        width: 350px;
        border-radius: 15px;
        padding: 20px;
        box-shadow: 0px 10px 20px -10px rgb(0, 0, 0, 0.7);
    }
    .card_menu {
        padding: 10px;
        border-radius: 5px;
        background-color: black;
    }
    .card_margin {
        margin-left: $card_margin;
    }
    .start_time {
        width: 120px;
        text-align: center;
    }
</style>