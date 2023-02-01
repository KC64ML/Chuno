<template>
    <div class="main_back flex_center">
        <router-view></router-view>
    </div>
    <FooterVue class="footer_fix"
        v-if="this.$route.fullPath != '/' && this.$route.fullPath != '/login' && this.$route.path != '/oauth'">
    </FooterVue>
    <button @click="testBtn">테스트버튼</button>
</template>

<script>
import FooterVue from "@/components/FooterVue.vue"
export default {
    components: {
        FooterVue
    },
    methods: {
        async testBtn() {
            this.axios.get("http://3.36.87.75:8000/user", {headers:{ "Authorization": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhY2Nlc3Nfa2V5IiwiaWF0IjoxNjc1MjM2NjMyLCJleHAiOjE2NzUzMjMwMzIsInVzZXJfaWQiOjF9.St63ziKC0rmrzPbNsDaN4hAEoUhgRethHvW0A6Yx29O0rhPC4RXn6xB9wd2zs-YzxKQ6KljVPxROGrymkXZnDg" }}).then((response => console.log(response)));
        }
    }
}
</script>

<style lang="scss">
@font-face {
    font-family: 'WandohopeR';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-10@1.0/WandohopeR.woff') format('woff');
    font-weight: bold;
    font-style: normal;
}

* {
    font-family: 'WandohopeR';
    margin: 0;
    box-sizing: border-box;
}

html,
body,
#app {
    height: 100%;
}

.button_style_1 {
    background-image: url("@/assets/main_button1.png");
    background-size: cover;
    width: 120px;
    height: 50px;
}

.block_center {
    display: block;
    margin: 0 auto;
}

.flex_center {
    display: flex;
    align-items: center;
    justify-content: center;
}

.relative {
    position: relative;
}

.image_text {
    color: white;
    position: absolute
}

.hover_pointer:hover {
    cursor: pointer;
}

.footer_fix {
    position: absolute;
    bottom: 0;
}

textarea:focus,
input:focus {
    outline: none;
}
</style>

<style lang="scss" scoped>
.main_back {
    background-image: url("@/assets/main_back.png");
    //cover는 다 덮도록 확대축소(1번 만나옴) contain 이미지가 전부 나오도록 확대축소 (2번나올수 있음)
    background-size: cover;
    width: 100vw;
    height: 100%;
    overflow: hidden;
}
</style>