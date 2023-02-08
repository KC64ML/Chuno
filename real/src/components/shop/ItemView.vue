<template>
  <div class="container-row">
    <img :src="URL + 'resources/images?path=' + item.imgPath" alt="item" style="height:50px;">

    <div>{{ item.name }}</div>
  </div>
</template>

<script>
export default {
  name: 'ChaserItemView',
  props: {
    item: Object,
    runnerItemCnt: Array,
    chaserItemCnt: Array,
  },
  data(){
    return {
      URL: process.env.VUE_APP_SPRING,
    }
  },
}
</script>

<style scoped>
</style>