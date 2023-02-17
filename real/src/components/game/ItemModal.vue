<template>
  <div id="modal_back">
    <div id="make_room_modal">
      <div>
        <p>{{ usedItem.name }}을(를) 사용하시겠습니까?</p>
        <div class="container-row">
          <div class="art-button" @click="itemNo">
            <div>아니오</div>
            <img src="@/assets/main_button1.png" alt="button">
          </div>
          <div class="art-button" @click="itemYes(usedItem)">
            <div>예</div>
            <img src="@/assets/main_button1.png" alt="button">
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    usedItem: Object,
  },
  data() {
      return {
          
      }
  },
  methods: {
    itemYes(usedItem) {
      // console.log('아이템 사용')
      this.$emit('item-yes', usedItem)
    },
    itemNo() {
      // console.log('아이템 미사용')
      this.$emit('item-no')
    },

  },
}
</script>

<style src="@vueform/slider/themes/default.css"></style>
<style lang="scss" scoped>
$input_height: 30px;
$plma_size: 30px;

#modal_back {
  position: absolute;
  width: 100vw;
  height: 100%;
  background: rgb(0, 0, 0, 0.6);
  z-index: 1;
}

#make_room_modal {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 85%;
  padding: 30px 0;
  background-color: #f2f2f2;
  transform: translate(-50%, -50%);
  font-size: 18px;
  border-radius: 10px;
}

#close_button {
  position: absolute;
  top: 10px;
  right: 20px;
  font-size: 20px;
}


</style>