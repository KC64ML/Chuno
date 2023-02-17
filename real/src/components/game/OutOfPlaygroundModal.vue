<template>
  <div id="modal_back">
    <div id="make_room_modal">
      <div id="modal_title" style="font-size: 24px">플레이 범위 밖입니다.</div>
      <div id="modal_title" style="font-size: 24px">범위 안으로 돌아가세요.</div>

      <div class="flex_center">
        <div class="flex_center hover_pointer" data-v-7961e374="">
          <img
            src="@/assets/main_button1.png"
            id="button1"
            data-v-7961e374=""
            style="width: 140px"
          />
          <div class="image_text" data-v-7961e374="" @click="onYes">확인</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "OutOfPlaygroundModal",
  methods: {
      onYes() {
      this.$emit('off-out-of-playground');
    },
  },
};
</script>

<style scoped>
#modal {
  background-color: #f5f5f5;
  border-radius: 10%;
}
img {
  width: 150px;
}

.container p {
  color: #f5f5f5;
  font-size: 18pt;
  margin: 0;
}

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

#modal_title {
  text-align: center;
  margin-bottom: 20px;
}
</style>