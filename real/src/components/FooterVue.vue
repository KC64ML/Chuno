<template>
    <div id="container">
        <div class="menu_box" @click="[this.$router.push('/home'),play(audio)]">
            <img class="menu" src="@/assets/Home.png">
            <div style="color: white">저잣거리</div>
        </div>
        <div class="menu_box" @click="[this.$router.push('/search'),play(audio)]">
            <img class="menu" src="@/assets/Search.svg">
            <div style="color: white">거리탐색</div>
        </div>
        <div class="menu_box" @click="[this.$router.push({ name: 'Profile', params: { uid: userInfo.id } }),play(audio)]">
            <img class="menu" src="@/assets/Profile_footer.png">
            <div style="color: white">호패</div>
        </div>
        <div class="menu_box" @click="[this.$router.push('/shop'),play(audio)]">
            <img class="menu" src="@/assets/Shop.png">
            <div style="color: white">보부상</div>
        </div>
        <div class="menu_box" @click="[this.$router.push('/rank'),play(audio)]">
            <img class="menu" src="@/assets/Rank.png">
            <div style="color: white">순위</div>
        </div>
    </div>
</template>

<script>
import drum from "@/assets/audio/drum.wav";
  export default {
    data(){
      return {
        userInfo: [],
        audio: {
        id: "music-tap",
        name: "MuscicTap",
        file: new Audio(drum),
      },
      }
    },
    methods: {
      getUser() {
        console.log('footer created에서 getuser')
        const token = sessionStorage.token
        this.axios.get(process.env.VUE_APP_SPRING + 'user', { headers: { Authorization: token } })
          .then((res) => {
            const code = res.data.result
            if(code) {
              this.userInfo = res.data.result
              console.log(res.data)
            } else {
              console.log('code err')
            }
          })
      },
      play(audio) {
        audio.file.play();
      },
    },
    created() {
      this.getUser()
    },
  }
</script>

<style lang="scss" scoped>
@import "@/assets/scss/variable.scss";

#container {
        background-color: black;
        height: $footer_height;
        width: 100vw;
        display: flex;
        justify-content: space-around;
        align-items: center;
    }

    .menu {
        height: 25px;
    }
    .menu_box {
        width: 20%;
        display: flex;
        flex-direction: column;
        align-items: center;
    }
</style>