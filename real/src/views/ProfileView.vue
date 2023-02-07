<template>
  <!-- 모달 -->
  <LogoutModal v-if="logoutModal" @on-logout="onLogout"/>
  <DeleteAccountModal v-if="deleteAccountModal" @on-delete="onDelete"/>
  <EditProfileModal v-if="editProfileModal"/>
  
  <HeaderVue
    v-if="me"
    :title="'프로필'"
  ></HeaderVue>
  <HeaderVue
    v-if="!me"
    :title= "userInfo.nickname"
  ></HeaderVue>

  <div style="height: 75%; width:300px;">
    <MyProfileView 
      @on-edit="onEdit"
      :me="me"
      :userInfo="userInfo"
      />
    <div class="container">
      <PlayTimeView
        :userInfo="userInfo"
      />
      <RecordView
        :userInfo="userInfo"
      />
    </div>
    <div id="checkout">
      <span @click="onDelete">회원탈퇴</span> |
      <span @click="onLogout">로그아웃</span>
    </div>
    <br>
    <br>
  </div>

</template>

<script>
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
      me: true,
      userInfo: {
        nickname: '바보',
        runnerPlayCount: 1,
        chaserPlayCount: 1,
        runnerWinCount: 1,
        chaserWinCount: 1,
      },
    }
  },
  methods: {
    getUser(){
      console.log('getuser')
      const token = sessionStorage.token
      const uid = this.$route.params.uid
      this.axios.get(process.env.VUE_APP_SPRING + 'user', { headers: { Authrization: token } })
        .then((res) => {
          console.log(res)
          const code = res.data.code
          if (code) {
            if(res.data.result.userId == uid){
              this.userInfo = res.data.result
              this.me = true
            } else{
              this.axios.get(process.env.VUE_APP_SPRING + 'user' + uid, { headers: { Authrization: token } })
              .then((res) => {
                  this.userInfo = res.data.result
                  this.me = false
                })
            }
          } else {
            console.log('code err')
          }
        })
        .catch((e)=>{
          console.log(e)
        })
    },
    onLogout() {
      this.$store.state.logoutModal = !this.$store.state.logoutModal
    },
    onDelete() {
      this.deleteAccountModal = !this.deleteAccountModal
    },
    onEdit() {
      this.editProfileModal != this.editProfileModal
      console.log(this.editProfileModal)
    },
  },
  created(){
    this.getUser()
  }
}
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}
#checkout{
  text-align: center;
}

</style>