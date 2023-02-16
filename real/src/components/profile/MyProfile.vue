<template>
  <div>
    <div class="container-col" id="myProfile">
      <div class="container-row">
        <!-- <img src="@/assets/profile_default.svg" alt="profile pic" class="uploadedImg"> -->
        <img :src="userInfo.profile && userInfo.profile.path ? URL + 'resources/images?path=' + userInfo.profile.path : defaultProfile"
          alt="profile pic" class="uploadedImg">
        <div>
          <p style="font-size: 20pt;">{{ userInfo.nickname }}</p>
          <p>Lv. {{ userInfo.level }} | {{ userInfo.money }} 냥</p>
        </div>
      </div>

      <div class="container-row" v-if="me">
        <div class="button" @click="onModal">
          <p>프로필 편집</p>
        </div>
        <div class="button" @click="this.$router.push({ name: 'friends', params: { uid: userInfo.id } })">
          <p>친구 관리</p>
        </div>
      </div>
      <div v-if="!me">
        <div class="button" v-if="!friend" @click="addFriend(this.$route.params.uid)">
          <p>친구 추가</p>
        </div>
        <div class="button" v-if="friend" @click="deleteFriend(this.$route.params.uid)">
          <p>친구 끊기</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import defaultProfile from '@/assets/profile_default.svg'
// import EditProfileModal from '@/components/profile/EditProfileModal.vue'
export default {
  name: 'MyProfile',
  components: {
    // EditProfileModal,
  },
  props: {
    me: Boolean,
    userInfo: Object,
    friend: Boolean,
  },
  data() {
    return {
      modal: false,
      defaultProfile: defaultProfile,
      URL: process.env.VUE_APP_SPRING,
      // editProfileModal: false,
    }
  },

  methods: {
    onModal() {
      // console.log("테스트 실행")
      // console.log(this.modal)
      // const token = sessionStorage.token
      // this.axios.get(process.env.VUE_APP_SPRING + 'user', { headers: { Authorization: token } })
      // .then((res) =>{
      //   console.log("userImgTest : " + res.data.result.profile.path);
      // })
      this.$emit('openEditModal');
    },
    // 친구 추가
    addFriend(yourUid) {
      // console.log(this.$route.params.uid)
      const token = sessionStorage.token
      // 내 정보 가져오기
      let myUid
      this.axios.get(process.env.VUE_APP_SPRING + 'user', { headers: { Authorization: token } })
        .then((res) => {
          myUid = res.data.result.userId
        })
        .catch((e) => {
          console.log(e)
        })

      // 넘길 데이터 만들기
      const data = { toUserId: yourUid, fromUserId: myUid }

      // 친구 추가
      this.axios.post(process.env.VUE_APP_SPRING + 'user/friend', data, { headers: { Authorization: token } })
        .then((res) => {
          const code = res.data.code
          if (code) {
            this.$emit('get-user')
            // console.log('친구 추가 성공')
            // console.log(res.data)
          } else {
            console.log('code error')
          }
        })
        .catch((e) => {
          console.log(e)
        })
    },

    deleteFriend(yourUid) {
      const token = sessionStorage.token
      this.axios.delete(process.env.VUE_APP_SPRING + 'user/friend/' + yourUid, { headers: { Authorization: token } })
        .then((res) => {
          const code = res.data.code
          if (code) {
            this.$emit('get-user')
            // console.log('친구 끊었다..')
          } else {
            console.log('code error')
          }
        })
        .catch((e) => {
          console.log(e)
        })
    },
  },
}
</script>

<style scoped>
#myProfile {
  /* display: flex; */
  /* flex-direction: row; */
  border-radius: 10%;
  background-color: #F5F5F5;
  justify-content: space-between;
  padding: 5% 0;
  margin: 5% 0;
}

.button {
  background-color: #1D182C;
  color: #F5F5F5;
  border-radius: 10%;
  text-align: center;
  margin: 3%;
  padding: 3% 5%;
}

.uploadedImg {
  width: 70px;
  height: 70px;
  border-radius: 50%;
}


.container-row {
  display: flex;
  flex-direction: row;
  /* border-radius: 10%; */
  /* background-color: #F5F5F5; */
  /* justify-content: left; */
  justify-content: space-around;
}

.container-col {
  display: flex;
  flex-direction: column;
  /* align-items: center; */
  /* border-radius: 10%; */
}

.profile {
  margin: 0 10%;
}
</style>