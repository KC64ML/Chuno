<template>
    <!-- {{ streamManager }} -->
    <video autoplay :class="className"></video>
</template>

<script>
    export default {
        name: 'OvVideo',

        props: {
            streamManager: Object,
            className: String,
        },

        mounted () {
            console.log("+*+*+*+*+*+*+*+*",this.streamManager);
            console.log(this.$el);
            this.streamManager.addVideoElement(this.$el);
    },

    watch: {
        streamManager(value) {
            value.addVideoElement(this.$el);
        }
    }
    }
</script>

<style lang="scss" scoped>
.my_video {
    width: 100%;
    border: solid blue;
}
.enemy_video{
    width: 100%;
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
}
</style>