<template>
  <HeaderVue
    :title="'랭킹'"
  ></HeaderVue>
  <RankListView/>
  <p @click="onGame">
    game router test
  </p>
</template>

<script>
import HeaderVue from '@/components/HeaderVue.vue';
import RankListView from '@/components/rank/RankListView.vue';

export default {
  name: 'RankView',
  components: {
    HeaderVue,
    RankListView,
  },
  methods:{
    onGame() {
      this.$router.push({name: 'game', params: { roomId: 1 }})
    }
  },
}
</script>

<style>

</style>