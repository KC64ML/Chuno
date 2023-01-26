<template>
  <EditProfileModal v-if="modal"/>

  <div id="myProfile">  
    <div class="container-col">

      <div class="container-row">
        <img src="@/assets/account_default.svg" alt="profile pic" class="uploadedImg">
        <div>
          <p>My Profile</p>
          <p>nickname</p>
        </div>
      </div>
      
      <div class="container-row">
        <div class="button" @click="onModal">
          프로필 편집
        </div>
        <div class="button" @click="addFriend">
          친구추가
        </div>
      </div>

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
    }
  },
  methods: {
    onModal(){
      this.modal = true
      console.log(this.modal)
    },
    addFriend() {
      console.log()
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
</style>