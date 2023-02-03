<template>
  <EditProfileModal v-if="modal"/>

  <div id="myProfile">  
    <div class="container-col">

      <div class="container-row">
        <img src="@/assets/profile_default.svg" alt="profile pic" class="uploadedImg">
        <div>
          <p>My Profile</p>
          <p>nickname</p>
        </div>
      </div>
      
      <!-- <div class="container-row"> -->
        <div class="button" @click="onModal">
          <p>프로필 편집</p>
        </div>
        <div class="button" @click="this.$router.push({name: 'friends', params: {uid: 1}})">
          <p>친구 관리</p>
        </div>
      <!-- </div> -->
      <!-- <div class="container-row" @click="addFriend"> -->
        <div class="button" v-if="!friend" @click="addFriend">
          <p>친구 추가</p>
        </div>
        <div class="button" v-if="friend" @click="addFriend">
          <p>친구 끊기</p>
        </div>
      <!-- </div> -->

    </div>
  </div>

</template>
<script>
import EditProfileModal from '@/components/profile/EditProfileModal.vue'

export default {
  name: 'MyProfile',
  components: {
    EditProfileModal,
  },
  data() {
    return {
      modal: false,
      friend: true,
    }
  },
  methods: {
    onModal(){
      this.modal = true
      console.log(this.modal)
    },
    addFriend() {
      this.friend = !this.friend
    }
  },
}
</script>

<style>
#myProfile {
  display: flex;
  flex-direction: row;
  border-radius: 10%;
  background-color: #F5F5F5;
}
.button {
  background-color: #1D182C;
  color: #F5F5F5;
  border-radius: 10%;
}
.button p{
  text-align: center;
  margin: 3%;
}
.container-row {
  display: flex;
  flex-direction: row;
  /* border-radius: 10%; */
  /* background-color: #F5F5F5; */
  justify-content: space-around;
}

.container-col{
  display: flex;
  flex-direction: column;
  /* align-items: center; */
  /* border-radius: 10%; */
}
.container-row #button{
  width: max-content;
  text-align: center;
}
</style>