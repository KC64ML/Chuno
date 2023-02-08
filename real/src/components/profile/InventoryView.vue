<template>
  <div id="inventory">
    <img :src="URL + 'resources/images?path=' + item.imgPath" alt="item">
    <div v-for="i in 8"
      :key="i"  
    >
      <div v-if="i == userInfo.id - 1" class="invenCnt">
        {{ userInfo.items[i] }}
      </div>
    </div>
    <!-- <div>{{ item.name }}</div> -->
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
  height: 60px;
  width: 60px;
  position: relative;
  background-color: #F5F5F5;
  border-radius: 10%;
  padding: 5%;
  margin: 10%;

}
#inventory img{
  height: 90%;
}
.invenCnt{
  position: absolute;
  right: 15%;
  bottom: 5%;
}
</style>