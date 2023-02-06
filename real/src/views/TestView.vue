<template>
    <div>
        {{ this.$route.params }}
    </div>
</template>

<script>
    export default {
        created() {
            console.log(this.$route.params)
        }
    }
</script>

<style lang="scss" scoped>

</style>