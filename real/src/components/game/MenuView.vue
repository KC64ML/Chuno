<template>
  <div>
    <div id="game-menu">
      <p id="menu-title">아이템</p>
      <div
        id="menu-item"
        v-for="item in items"
        :key="item.id"
        @click="useItem(item)"
      >
        <img :src="item.imgPath" alt="item">
        <p>{{ item.name }}</p>
      </div>
      <p id="menu-title">메뉴</p>
      <div id="menu-item">
        <img src="@/assets/game_search.png" alt="search">
        <p>검색</p>
      </div>
      <div id="menu-item">
        <img src="@/assets/game_whisper.png" alt="whisper">
        <p>귓속말</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MenuView',
  data() {
    return {
      items: []
    }
  },
  methods: {
    // 아이템 불러오기
    getItems(){
      this.axios.get(
        process.env.VUE_APP_SPRING + "item/1",
      )
        .then((res) => {
          const items = res.data.result
          const code = res.data.code
          if (code) {
            this.items = items
          } else {
            console.log('error')
          }
        })
    },
    // 아이템 사용
    useItem(item) {
      console.log(item)
      this.$store.state.item = [item]
      this.$store.dispatch('useItem')
      this.$store.state.menu = false

    }
  },
  mounted(){
    this.getItems()
  }
}

</script>

<style>
#game-menu{
  background: rgba(67, 64, 57, 0.3);
  border-radius: 10%;
}
#menu-title{
  color: #F5F5F5;
}
#menu-item{
  display: flex;
  flex-direction: row;
}
</style>