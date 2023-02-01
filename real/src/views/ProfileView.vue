<template>
  <!-- 모달 -->
  <LogoutModal v-if="logoutModal" @on-logout="onLogout"/>
  <DeleteAccountModal v-if="deleteAccountModal" @on-delete="onDelete"/>
  <EditProfileModal v-if="editProfileModal"/>
  
  <HeaderVue></HeaderVue>

  <div style="height: 75%;">
    <MyProfileView @on-edit="onEdit"/>
    <div class="container">
      <PlayTimeView/>
      <RecordView/>
    </div>
    <span @click="onDelete">회원탈퇴</span> |
    <span @click="onLogout">로그아웃</span>
    <br>
    <br>
  </div>

</template>

<script>
// import axios from 'axios'
import HeaderVue from '@/components/HeaderVue.vue'
import MyProfileView from '../components/profile/MyProfile.vue'
import PlayTimeView from '@/components/profile/PlayTimeView.vue'
import RecordView from '@/components/profile/RecordView.vue'

import LogoutModal from '@/components/profile/LogoutModal.vue'
import DeleteAccountModal from '@/components/profile/DeleteAccountModal.vue'
import EditProfileModal from '@/components/profile/EditProfileModal.vue'

export default {
  name: 'ProfileView',
  components: {
    HeaderVue,
    MyProfileView,
    PlayTimeView,
    RecordView,
    LogoutModal,
    DeleteAccountModal,
    EditProfileModal,
  },
  data() {
    return {
      logoutModal: false,
      deleteAccountModal: false,
      editProfileModal: false,
    }
  },
  methods: {
    onLogout() {
      this.logoutModal = !this.logoutModal
    },
    onDelete() {
      this.deleteAccountModal = !this.deleteAccountModal
    },
    onEdit() {
      this.editProfileModal != this.editProfileModal
      console.log(this.editProfileModal)
    },
  }
}
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}
</style>