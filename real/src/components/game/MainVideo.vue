<template>
    <div style="border: dashed">
        {{ this.stream_manager }}
        <!-- <video autoplay></video> -->
    </div>
</template>

<script>
    export default {
        props: {
            stream_manager: undefined
        },
        computed: {
        }
    }
</script>

<style lang="scss" scoped>

</style>