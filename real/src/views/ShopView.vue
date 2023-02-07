<template>
  <HeaderVue
    :title="'상점'"
  ></HeaderVue>
  <div class="container-col" style="height:75%">
    <div class="container" id="money">
      <img src="@/assets/nyang.svg" alt="nyang">
      {{ money }}
    </div>
    <SelectedItemView
      :item="selected"
      :money="money"
    />
    <div class="container-row">
      <div class="box" style="margin-right: 3%">
        <div id="item-title">
          <p>노비용</p>
        </div>
        <ItemView
          @click="onSelect(item)"
          v-for="item in items.filter((i) => i.forRunner == 1)"
          :key="item.id"
          :item="item"
        />
      </div>
      <div class="box" style="margin-left: 3%">
        <div id="item-title">
          <p>추노꾼용</p>
        </div>
        <ItemView
          @click="onSelect(item)"
          v-for="item in items.filter((i) => i.forRunner == 0)"
          :key="item.id"
          :item="item"
        />
      </div>
    </div>
  </div>
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
        money: 0,
      }
    },
    methods: {
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
      getUser(){
        const token = sessionStorage.token
        this.axios.get(
          process.env.VUE_APP_SPRING + "item", { headers: { Authorization: token } }
          )
          .then((res) => {
            const user = res.data.result
            const code = res.data.code
            if (code) {
              this.money = user.money
              console.log(this.money)
            } else {
              console.log('code error')
            }
          })
          .catch((e)=>{
            console.log(e)
          })
      }
    },
    created() {
      this.getItems()
    }
  }
</script>

<style>
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

</style>