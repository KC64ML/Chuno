<template>
  <div class="container-row">
    <img :src="item.img_path" alt="item" style="height:50px;">
    <div>{{ item.name }}</div>
  </div>
</template>

<script>
export default {
  name: 'ChaserItemView',
  props: {
    item: Object,
  }
}
</script>

<style>

</style>