<template>
  <div class="container-row">
    <img :src="URL + 'resources/images?path=' + item.imgPath" alt="item" style="height:50px;">
    <div v-for="i in 8"
      :key="i"  
    >
      <div v-if="i == item.id">
        {{ userInfo.items[i-1] }}
      </div>
    </div>
    <div>{{ item.name }}</div>
  </div>
</template>

<script>
export default {
  name: 'ChaserItemView',
  props: {
    item: Object,
    userInfo: Object
    // runnerItemCnt: Array,
    // chaserItemCnt: Array,
  },
  data(){
    return {
      URL: process.env.VUE_APP_SPRING,
    }
  },
  created(){
    // console.log(this.runnerItemCnt)
    // console.log(this.chaserItemCnt)
  }
}
</script>

<style scoped>
</style>