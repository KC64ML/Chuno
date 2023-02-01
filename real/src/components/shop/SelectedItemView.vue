<template>
  <div class="box container-row" style="width:80vw; height: 20vh; margin:5% 0;">
    <div>
      <img :src="item.img_path" alt="selected" style="width: 70px;">
    </div>
    <div>
      <p>{{ item.name }}</p>
      <p>{{ item.description }}</p>
      <div class="container-row">
        <p>가격</p>
        <p>{{ item.price }} 냥</p>
        <p class="button">구매</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SelectedItemView',
  props: {
    item: Object
  },

}
</script>

<style>
.box {
  background-color: #F5F5F5;
  border-radius: 10%;
  padding: 5%;
}
</style>