<template>
  <HeaderVue></HeaderVue>
  <RankListView/>
</template>

<script>
import HeaderVue from '@/components/HeaderVue.vue';
import RankListView from '@/components/rank/RankListView.vue';

export default {
  name: 'RankView',
  components: {
    HeaderVue,
    RankListView,
  }
}
</script>

<style>

</style>