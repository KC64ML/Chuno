<template>
    <div>
        열심히 로그인 중이에요...
    </div>
</template>

<script>
    import axios from "axios";

    export default {
        async created() {
            var code = new URL(window.location.href).searchParams.get('code');
            var result = await axios.post("http://43.201.137.209:9997/kakao/login", code, {headers: {'Content-Type': 'text/plain'}});
            console.log(result);
        }
    }
</script>

<style lang="scss" scoped>

</style>