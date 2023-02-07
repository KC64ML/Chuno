<template>
  <div>
    <div id="game-menu">
      <!-- <p id="menu-title">아이템</p> -->
      <div
        id="menu-item"
        v-for="item in items"
        :key="item.id"
        @click="useItem(item)"
      >
        <img :src="item.imgPath" alt="item">
        <p>{{ item.name }}</p>
      </div>
      <!-- <p id="menu-title">메뉴</p>
      <div id="menu-item">
        <img src="@/assets/game_search.png" alt="search">
        <p>검색</p>
      </div>
      <div id="menu-item">
        <img src="@/assets/game_whisper.png" alt="whisper">
        <p>귓속말</p>
      </div> -->
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
        .catch(() => {
          console.log('error')
        })
      
    },
    // 아이템 사용
    useItem(item) {
     this.$emit('use-item', item)
    }
  },
  created(){
    // this.getItems()
    this.items = [
      {
        "id": 1,
        "name": "천리안",
        "price": 1500,
        "description": "자신의 위치를 드러내지 않고 가장 가까운 추노꾼의 위치를 확인할 수 있다.",
        "imgPath": "",
        "forRunner": 1
      },
      {
        "id": 2,
        "name": "위장",
        "price": 2000,
        "description": "추노꾼이 자신을 잡을 수 있는 범위를 축소한다.",
        "imgPath": "",
        "forRunner": 1
      },
      {
        "id": 3,
        "name": "확실한 정보통",
        "price": 1000,
        "description": "진짜 노비문서의 위치를 확인할 수 있다.",
        "imgPath": "",
        "forRunner": 1
      },
      {
        "id": 4,
        "name": "먹물탄",
        "price": 1300,
        "description": "먹물을 뿌려 내 화면을 가릴 수 있다.",
        "imgPath": "",
        "forRunner": 1
      }
    ]
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