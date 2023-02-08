<template>
  <!-- <div> -->
  <HeaderVue
    :title="'상점'"
  ></HeaderVue>
  <div class="container-col" style="height:75%">
    <div class="container" id="money">
      <img src="@/assets/nyang.svg" alt="nyang">
      {{ userInfo.money }}
    </div>
    <SelectedItemView
      :item="selected"
      :money="money"
    />
    <div class="container-row ">
      <div class="box item" style="margin-right: 3%">
        <div id="item-title">
          <p>노비용</p>
        </div>
        <ItemView
          @click="onSelect(item)"
          v-for="item in items.filter((i) => i.forRunner == 1)"
          :key="item.id"
          :item="item"
         
          :itemCnt="runnerItemCnt"
        />
      </div>
      <div class="box item" style="margin-left: 3%">
        <div id="item-title">
          <p>추노꾼용</p>
        </div>
        <ItemView
          @click="onSelect(item)"
          v-for="item in items.filter((i) => i.forRunner == 0)"
          :key="item.id"
          :item="item"
          :itemCnt="chaserItemCnt"

        />
      </div>
    </div>
  </div>
<!-- </div> -->
</template>

<script>
  import HeaderVue from '@/components/HeaderVue.vue';
  import SelectedItemView from '@/components/shop/SelectedItemView.vue';
  import ItemView from '@/components/shop/ItemView.vue';

  export default {
    name: 'ShopView',
    components: {
      HeaderVue,
      SelectedItemView,
      ItemView,
    },
    data() {
      return {
        selected: {
          name: ' ',
          price: ' ',
          description: ' ',
          imgPath: ' ',
          forRunner: ' ',
        },
        items:[],
        userInfo: [],
      }
    },
    methods: {
      getUser(){
        const token = sessionStorage.token
        this.axios.get(process.env.VUE_APP_SPRING +'user', { headers: { Authorization: token } })
          .then((res) => {
            const code = res.data.code
            if(code) {
              this.userInfo = res.data.result
            } else {
              console.log('code err')
            }
          })
      },
      onSelect(res) {
        this.selected = res
        console.log(this.selected)
      },
      getItems(){
        this.axios.get(
          process.env.VUE_APP_SPRING + "item",
        )
          .then((res) => {
            const items = res.data.result
            const code = res.data.code
            if (code) {
              this.items = items
              console.log("여기 아이템");
              console.log(this.items)
            } else {
              console.log('error')
            }
          })
          .catch((e)=>{
            console.log(e)
          })
      },
    },
    created() {
      this.getItems()
      this.getUser()
    },
    computed: {
      runnerItemCnt(){
        let res = []
        const items = this.userInfo.items
      
        for (const i in items) {
          if(i < 4){
            res.append(items[i])
          }
        }
        return res
      },
      chaserItemCnt(){
        let res = []
        const items = this.userInfo.items
      
        for (const i in items) {
          if(i > 3){
            res.append(items[i])
          }
        }
        return res
      },
    },
  }
</script>

<style scoped>
#item-title{
  color: white;
  background-color: black;
  border-radius: 10%;
  padding: 10%;
  margin: 10%;
  text-align: center;
}
#money{
  width: 5rem;
  border-radius: 30%;
  background-color: #F5F5F5;
  padding: 0 5%;
}
.item{
  width: 50%;
}
</style>