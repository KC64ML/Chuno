<template>
  <HeaderVue
    :title="'상점'"
  ></HeaderVue>
  <div class="container-col" style="height:75%">
    <SelectedItemView
      :item="selected"
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
      }
    },
    methods: {
      onSelect(res) {
        this.selected = res
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
              console.log(this.items)
            } else {
              console.log('error')
            }
          })
      },
    },
    mounted() {
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
</style>