<template>
  <div id="card_container" :class="comp">
    {{ nickname }}
  </div>
  
</template>

<script>
export default {
  props: {
    nickname: String,
    comp:String
  },
}
</script>

<style lang="scss" scoped>
@import "@/assets/scss/variable";
$card_height: 40px;

#card_container {
  width: 100px;
  height: $card_height;
  border-radius: $card_height / 2;
  text-align: center;
  line-height: $card_height;
  box-shadow: 0 10px 10px $shadow_color;
  font-weight: 800;
}
.yellow {
  background-color: #f9cf65;
}
.white {
  background-color: white;
}
</style>