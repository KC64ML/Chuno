<template>
  <div class="modal-bg">
    <div class="modal">
      <p>프로필 편집</p>
      
      <label for="file">
        <!-- <img src="@/assets/profile_frame.png" alt="profileFrame"> -->
        <!-- <img src="@/assets/camera.svg" alt="upload pic"> -->
        <img :src="img ? img : img_default" alt="profilePic" class="uploadedImg">
        <input type="file" id="file" class="inputfile" @change="upload">
      </label>
 

      <div>
        <label for="nickname_input">닉네임</label>
        <input type="text" id="nickname_input" v-model="nickname" maxlength="6">

        <div class="check" v-if="nickname.length>0">
          <div v-if="!lengthValid || !useValid">
            <p v-if="!lengthValid">닉네임은 최대 6글자까지 가능합니다.</p>
            <p v-if="!useValid">이미 사용 중인 닉네임입니다.</p>
          </div>
          <div v-else>
            <p style="color:green;">사용 가능한 닉네임입니다.</p>
          </div>
        </div>
        <div v-else>
          <br>
        </div>
        <div class="container" @click="onSave">
          <p>저장</p>
          <img alt="Vue logo" src="@/assets/main_button1.png">
        </div>

      </div>
    </div>
  </div>
</template>

<script>
// import axios from 'axios'
import img_default from '@/assets/profile_default.svg'

export default {
  name: 'EditProfileModal',
  data() {
    return {
      img_default,
      img: null,
      nickname: '',
      valid: true,
      lengthValid: true,
      useValid: true,
    }
  },

}
</script>

<style>

</style>