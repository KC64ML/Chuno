<template>
    <div id="container" class="white" :class="[ {yellow : sub.isReady, green : sub.isHost} ]">
        {{ sub.level }} {{ sub.nickname }} {{ sub.isReady }}
    </div>
</template>

<script>

    export default {
        props: {
            sub: undefined
        },
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
    .white {
        background-color: #F2F2F2;
    }
    .yellow {
        background-color: #F9CF65;
    }
    .green {
        background-color: #34ff74
    }
</style>