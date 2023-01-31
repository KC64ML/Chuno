<template>
    <div id="header">
        <img src="@/assets/main_logo2.png" @click="goHome" style="height: 50px; position: absolute; left: 20px;">
        <div style="font-size: 30px;">
            {{ this.here }}
        </div>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                here: undefined
            }
        },
        created() {
            var here = this.$route.path;
            if (here == '/home') this.here = '저잣거리';
            else if (here == '/rank') this.here = '랭킹';
            else if (here == '/profile') this.here = '프로필';
        },
        methods: {
            goHome() {
                alert('ffff');
            }
        }
    }
</script>

<style lang="scss" scoped>
    #header {
        position: absolute;
        top: 0;
        height: 60px;
        width: 100vw;
        display: flex;
        align-items: center;
        justify-content: center;
    }
</style>