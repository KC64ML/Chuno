<template>
    <div>
        <!-- {{ streamManager }} -->
        <video autoplay></video>
    </div>
</template>

<script>
    export default {
        name: 'OvVideo',

        props: {
            streamManager: Object,
        },

        mounted () {
            console.log("+*+*+*+*+*+*+*+*",this.streamManager);
            console.log(this.$el);
            this.streamManager.addVideoElement(this.$el);
        },
    }
</script>

<style lang="scss" scoped>

</style>