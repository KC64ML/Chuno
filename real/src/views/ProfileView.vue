<template>
  <!-- 모달 -->
  <LogoutModal v-if="logoutModal" @on-logout="onLogout"/>
  <DeleteAccountModal v-if="deleteAccountModal" @on-delete="onDelete"/>
  <NewEditProfileModal v-if="profileEditModal" @on-editProfile="openEditModal" :userInfo="userInfo" />

  <HeaderVue
    v-if="me"
    :title="'프로필'"
  ></HeaderVue>
  <HeaderVue
    v-if="!me"
    :title= "userInfo.nickname + '님의 프로필'"
  ></HeaderVue>

  <div style="height: 80%; width:300px; overflow: scroll;" class="scroll">
    <MyProfileView 
      @openEditModal="openEditModal"
      @on-edit="onEdit"
      :me="me"
      :userInfo="userInfo"
      :friend="friend"
      @get-user="getUser"
    />
    <div><br></div>
    <p class="inventory-text">노비용</p>
    <div style="display: grid; grid-template-columns: 1fr 1fr 1fr 1fr; background-color: #1D182C; border-radius: 10%; justify-items: center;">
      <InventoryView
        :userInfo="userInfo"
        v-for="item in items.filter((i)=>i.id<5)"
        :key="item.id"
        :item="item"
      />
    </div>
    <div><br></div>
    <p class="inventory-text">추노꾼용</p>
    <div style="display: grid; grid-template-columns: 1fr 1fr 1fr 1fr; background-color: #1D182C; border-radius: 10%; justify-items: center;">
      <InventoryView
        :userInfo="userInfo"
        v-for="item in items.filter((i)=>i.id>4)"
        :key="item.id"
        :item="item"
      />
    </div>
    <div><br></div>
    <div class="container">
      <PlayTimeView style="margin-right:20px;"
        :userInfo="userInfo"
      />
      <RecordView
        :userInfo="userInfo"
      />
    </div>
    <div><br></div>
    <div id="checkout" v-if="me">
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
import InventoryView from '@/components/profile/InventoryView.vue'

import LogoutModal from '@/components/profile/LogoutModal.vue'
import NewEditProfileModal from '@/components/profile/NewEditProfileModal.vue'
import DeleteAccountModal from '@/components/profile/DeleteAccountModal.vue'

export default {
  name: 'ProfileView',
  components: {
    HeaderVue,
    MyProfileView,
    InventoryView,
    PlayTimeView,
    RecordView,
    LogoutModal,
    DeleteAccountModal,
    NewEditProfileModal,
  },
  data() {
    return {
      logoutModal: false,
      deleteAccountModal: false,
      profileEditModal: false,
      me: true,
      friend: false,
      userInfo: [],
      items: [],
    }
  },
  methods: {
    getUser(){
      const token = sessionStorage.token
      // 파람스에 있는 uid
      const uid = this.$route.params.uid
      //내 정보 불러와서
      this.axios.get(process.env.VUE_APP_SPRING + 'user', { headers: { Authorization: token } })
        .then((res) => {
          console.log('-----------내 정보 불러오기----------')
          console.log(res)
          const code = res.data.code
          console.log(token)
          
          if (res.data.result.profile == null || res.data.result.profile.path == '') {
            res.data.result.profile = {
              path: 'profile/default.svg'
            };
          }
          console.log("path : " + res.data.result.profile.path);
          if (code) {
            if(res.data.result.id == uid){ // 내 프로필이면 
              this.userInfo = res.data.result
              this.me = true
            } else{ // 다른 사람 프로필이면
              this.axios.get(process.env.VUE_APP_SPRING + 'user/' + uid, { headers: { Authorization: token } })
              .then((res) => {
                  this.userInfo = res.data.result
                  this.me = false
                })
              // 친구인지 확인
              this.axios.get(process.env.VUE_APP_SPRING + 'user/friend/' + uid, { headers: { Authorization: token } })
              .then((res) => {
                  console.log(res.data)
                  const code = res.data.code
                  if(code) {
                    console.log('친구임')
                    this.friend = true
                  } else{
                    console.log('친구 아님')
                    this.friend = false
                  }
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
    getItems(){
      this.axios.get(
        process.env.VUE_APP_SPRING + "item",
      )
        .then((res) => {
          const items = res.data.result
          const code = res.data.code
          if (code) {
            this.items = items
            console.log("프로필에서 아이템 가져오기");
            console.log(this.items)
          } else {
            console.log('error')
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
    openEditModal() {
      this.profileEditModal = !this.profileEditModal
    }
  },
  created(){
    this.getItems()
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
  grid-template-columns: 1fr 1fr 1fr 1fr;
}
/* #inventory{
  display: gird;
  background-color: #1D182C;
} */
.inventory-text{
  font-size: 18px;
}

</style>