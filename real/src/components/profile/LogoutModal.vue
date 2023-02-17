<template>
  <div id="modal_back">
    <div id="make_room_modal">
      <div id="modal_title" style="font-size: 24px">로그아웃 하시겠습니까?</div>


      <div class="flex_center">
        <div class="flex_center hover_pointer" data-v-7961e374="">
          <img
            src="@/assets/main_button1.png"
            id="button1"
            data-v-7961e374=""
            style="width: 140px"
          />
          <div class="image_text" data-v-7961e374="" @click="logout" >예</div>
        </div>
 
        <div class="flex_center hover_pointer" data-v-7961e374="">
          <img
            src="@/assets/main_button1.png"
            id="button1"
            data-v-7961e374=""
            style="width: 140px"
          />
          <div class="image_text" data-v-7961e374="" @click="$emit('onLogout')">아니오</div>

          
      </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "LogoutModal",
  methods: {
    logout() {
      sessionStorage.setItem("token", null);
      this.$emit("on-logout");
      this.$router.push({ name: "start" });
    },
  },
};
</script>

<style>
#modal {
  /* width: 150px; */
  background-color: #f5f5f5;
  border-radius: 10%;
}
</style>


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

#modal_title {
  text-align: center;
  margin-bottom: 20px;
}
</style>