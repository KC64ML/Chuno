<template>
  <div>
  <HeaderVue
    :title="'상점'"
  ></HeaderVue>
  <div style="height:80%;  overflow: scroll;" class="scroll">
    <!-- <div class="container-col"> -->
      <div class="container-row" id="money">
        <img src="@/assets/nyang.svg" alt="nyang" style="padding: 0 10px 0 0;">
        {{ userInfo.money }}
      </div>
      <SelectedItemView
        :item="selected"
        @get-user="getUser"
      />
        <div class="container-row ">
          <div class="box item" style="margin-right: 3%">
            <div id="item-title">
              <p>노비용</p>
            </div>
            <ItemView
              :userInfo="userInfo"
              @click="onSelect(item)"
              v-for="item in items.filter((i) => i.forRunner == 1)"
              :key="item.id"
              :item="item"
            />
          </div>
          <div class="box item" style="margin-left: 3%">
            <div id="item-title">
              <p>추노꾼용</p>
            </div>
            <ItemView
              :userInfo="userInfo"
              @click="onSelect(item)"
              v-for="item in items.filter((i) => i.forRunner == 0)"
              :key="item.id"
              :item="item"
            />
          </div>
        </div>
      </div>
    <!-- </div> -->
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
              console.log(this.userInfo)
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
              console.log("아이템 가져오기");
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
  width: fit-content;
  border-radius: 30%;
  background-color: #F5F5F5;
  padding: 3% 5%;
}
.item{
  width: 50%;
}
</style>