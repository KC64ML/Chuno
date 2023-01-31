<template>
    <nav id="header">
      <router-link to="/">
        <img class="logo" alt="Chuno Logo" src="@/assets/main_logo2.png">
      </router-link>
      <br>
      <p>상점</p>
    </nav>
    <router-view/>
    <div>shop</div>
    <SelectedItemView/>
    <ChaserItemView/>
    <RunnerItemView/>

</template>

<script>
import SelectedItemView from '@/components/shop/SelectedItemView.vue';
import ChaserItemView from '@/components/shop/ChaserItemView.vue';
import RunnerItemView from '@/components/shop/RunnerItemView.vue';

export default {
  name: 'ShopView',
  components: {
    SelectedItemView,
    ChaserItemView,
    RunnerItemView,
  },
  data() {
    return {
      selected: null,
      tab: 0,
    }
  }
}
</script>

<style>

</style>