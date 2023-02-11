<template>
    <div id="container" class="white" :class="[{ yellow: sub.ready, green: sub.host }]">
        {{ sub.level }} {{ sub.nickname }}
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
    animation-name: nicknmae_card;
    animation-iteration-count: 1;
    animation-duration: 1s;
}

@keyframes nicknmae_card {
    0% {
        opacity: 0;
        transform: translateX(30px);
    }
    100% {
        opacity: 100;
        transform: translateX(0);
    }
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