<template>
  <!-- 모달 -->
  <LogoutModal v-if="logoutModal" @on-logout="onLogout"/>
  <DeleteAccountModal v-if="deleteAccountModal" @on-delete="onDelete"/>
  <EditProfileModal 
    v-if="editProfileModal"
    @on-edit="onEdit"
    :userInfo="userInfo"
  />
  
  <HeaderVue
    v-if="me"
    :title="'프로필'"
  ></HeaderVue>
  <HeaderVue
    v-if="!me"
    :title= "userInfo.nickname + '님의 프로필'"
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
      friend: false,
      userInfo: []
    }
  },
  methods: {
    getUser(){
      const token = sessionStorage.token
      // 프로필 주인 아이디
      const uid = this.$route.params.uid
      // 내 정보 불러와서
      this.axios.get(process.env.VUE_APP_SPRING + 'user', { headers: { Authorization: token } })
        .then((res) => {
          console.log('-----------내 정보 불러오기----------')
          console.log(res)
          const code = res.data.code
          console.log(token)
          if (code) {
            if(res.data.result.id == uid){ // 내 프로필이면 
              this.userInfo = res.data.result
              this.me = true
            } else{ // 다른 사람 프로필이면
              this.axios.get(process.env.VUE_APP_SPRING + 'user/' + uid, { headers: { Authrization: token } })
              .then((res) => {
                  this.userInfo = res.data.result
                  this.me = false
                })
              // 친구인지 확인
              this.axios.get(process.env.VUE_APP_SPRING + 'user/friend/' + uid, { headers: { Authrization: token } })
              .then((res) => {
                  this.friend = res.data.result
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
      this.logoutModal = !this.logoutModal
    },
    onDelete() {
      this.deleteAccountModal = !this.deleteAccountModal
    },
    onEdit() {
      this.editProfileModal = !this.editProfileModal
      console.log(this.editProfileModal)
    },
  },
  created(){
    console.log('getUser 실행?')
    this.getUser()
    console.log('getUser 실행!')
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