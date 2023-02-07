<template>
    <div id="container">
        <div class="menu_box" @click="this.$router.push('/home')">
            <img class="menu" src="@/assets/Home.svg">
            <div style="color: white">홈화면</div>
        </div>
        <div class="menu_box" @click="this.$router.push('/search')">
            <img class="menu" src="@/assets/Search.svg">
            <div style="color: white">방검색</div>
        </div>
        <div class="menu_box" @click="this.$router.push(`profile/${userInfo.id}`)">
            <img class="menu" src="@/assets/Profile_footer.svg">
            <div style="color: white">내프로필</div>
        </div>
        <div class="menu_box" @click="this.$router.push('/shop')">
            <img class="menu" src="@/assets/Shop.svg">
            <div style="color: white">상점</div>
        </div>
        <div class="menu_box" @click="this.$router.push('/rank')">
            <img class="menu" src="@/assets/Rank.svg">
            <div style="color: white">랭킹</div>
        </div>
    </div>
</template>

<script>
  export default {
    data(){
      return {
        userInfo: [],
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
      }
    },
    created() {
      this.getUser()
    }
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