<template>
    <div id="container">
        <img src="@/assets/main_logo1.png" class="block_center" style="width:150px">
        <div style="margin-top: 30px"></div>
        <div class="flex_center hover_pointer" @click="start">
            <img src="@/assets/main_button1.png" id="button1">
            <div class="image_text">시작해요</div>
        </div>
    </div>
</template>

<script>
    export default {
        methods: {
            start() {
                this.$router.push("/login")
            }
        }
    }
</script>

<style lang="scss" scoped>
    #button1 {
        height: 70px;
    }
</style>