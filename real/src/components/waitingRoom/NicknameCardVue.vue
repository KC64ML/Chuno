<template>
    <div id="container" :class="[ sub.isReady? 'yellow' : 'white' ]">
        {{ sub.level }} {{ sub.nickname }}
    </div>
</template>

<script>

    export default {
        props: {
            sub: undefined
        } 
    }
</script>

<style lang="scss" scoped>
@import "@/assets/scss/variable";
$container_height: 30px;
    #container {
        margin: 10px 0;
        box-shadow: 0 10px 10px $shadow_color;
        height: $container_height;
        width: calc(100vw * 0.3);
        line-height: $container_height;
        text-align: center;
        border-radius: $container_height;
        font-size: 13px;
    }
    .yellow {
        background-color: #F9CF65;
    }
    .white {
        background-color: #F2F2F2;
    }
</style>