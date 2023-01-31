<template>
    <div id="container">
        <div class="menu_box" @click="this.$router.push('/home')">
            <img class="menu" src="@/assets/Home.svg">
            <div style="color: white">홈화면</div>
        </div>
        <div class="menu_box" @click="this.$router.push('/search')">
            <img class="menu" src="@/assets/Search.svg">
            <div style="color: white">방검색</div>
        </div>
        <div class="menu_box" @click="this.$router.push('/profile')">
            <img class="menu" src="@/assets/Profile_footer.svg">
            <div style="color: white">내프로필</div>
        </div>
        <div class="menu_box" @click="this.$router.push('/shop')">
            <img class="menu" src="@/assets/Shop.svg">
            <div style="color: white">상점</div>
        </div>
        <div class="menu_box" @click="this.$router.push('/rank')">
            <img class="menu" src="@/assets/Rank.svg">
            <div style="color: white">랭킹</div>
        </div>
    </div>
</template>

<script>
    export default {
        
    }
</script>

<style lang="scss" scoped>
@import "@/assets/scss/variable.scss";

    #container {
        background-color: black;
        height: $footer_height;
        width: 100vw;
        display: flex;
        justify-content: space-around;
        align-items: center;
    }
    .menu {
        height: 25px;
    }
    .menu_box {
        width: 20%;
        display: flex;
        flex-direction: column;
        align-items: center;
    }
</style>