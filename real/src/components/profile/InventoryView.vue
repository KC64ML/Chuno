<template>
  <div class="container-row" id="inventory">
    <img :src="URL + 'resources/images?path=' + item.imgPath" alt="item" style="height:50px;">
    <div v-for="i in 8"
      :key="i"  
    >
      <div v-if="i == userInfo.id - 1">
        {{ userInfo.items[i] }}
      </div>
    </div>
    <div>{{ item.name }}</div>
  </div>
</template>

<script>
export default {
  name: 'InventoryView',
  props: {
    item: Object,
    userInfo: Object
  },
  data(){
    return {
      URL: process.env.VUE_APP_SPRING,
    }
  },
}
</script>

<style scoped>
#inventory{
  background-color: #1D182C;
  border-radius: 10%;
  color: #F5F5F5;
}
</style>