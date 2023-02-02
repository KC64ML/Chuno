<template>
  <HeaderVue
    :title="'상점'"
  ></HeaderVue>
  <div class="container-col" style="height:75%">
    <SelectedItemView
      :item="selected"
    />
    <div class="container-row">
      <div class="box">
        <div id="item-title">
          <p>노비용</p>
        </div>
        <ItemView
          @click="onSelect(item)"
          v-for="(item, i) in items.filter((i) => i.forR == 1)"
          :key="i"
          :item="item"
        />
      </div>
      <div class="box">
        <div id="item-title">
          <p>추노꾼용</p>
        </div>
        <ItemView
          @click="onSelect(item)"
          v-for="(item, i) in items.filter((i) => i.forR == 0)"
          :key="i"
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
        selected: [],
        items:[],
      }
    },
    methods: {
      onSelect(res) {
        this.selected = res
      }
    },
    mounted() {
      this.items = [
        {
          forR: 1,
          name: '천리안',
          description: '자신의 위치를 드러내지 않고 가장 가까운 추노꾼의 위치를 확인할 수 있다.',
          price: 20,
          img_path: require('@/assets/logo.png'),
        },
        {
          forR: 1,
          name: '위장',
          description: '추노꾼이 자신을 잡을 수 있는 범위를 축소한다.',
          price: 30,
          img_path: require('@/assets/logo.png'),
        },
        {
          forR: 0,
          name: '조명탄',
          description: 'n초간 노비의 위치를 지도에 표시할 수 있다.',
          price: 15,
          img_path: require('@/assets/logo.png'),
        },
        {
          forR: 0,
          name: '긴 오랏줄',
          description: '자신이 노비를 잡을 수 있는 범위를 확대할 수 있다.',
          price: 15,
          img_path: require('@/assets/logo.png'),
        },
      ]
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