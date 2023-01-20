<template>
  <div>
    <MyProfileView/>
    <div>
      <PlayTimeView/>
      <RecordView/>
    </div>
    <span @click="del">회원탈퇴</span> |
    <span @click="logout">로그아웃</span>
  </div>

</template>

<script>
import axios from 'axios'
import MyProfileView from '@/components/profile/MyProfileView.vue'
import PlayTimeView from '@/components/profile/PlayTimeView.vue'
import RecordView from '@/components/profile/RecordView.vue'

export default {
  name: 'ProfileView',
  components: {
    MyProfileView,
    PlayTimeView,
    RecordView,
  },
  methods: {
    logout() {
      alert("로그아웃");
      sessionStorage.setItem("token", null)
      //예제 파일은 html파일이라 새로고침을 하지만 vue는 data가 바인딩 되어있으므로 이 명령은 필요 없을거에요
      // location.reload();
    },
    async del() {
      alert("탈퇴");
      var del_user_email = document.getElementById("del_user").value;
      await axios.post("http://3.34.138.191:9997/kakao/delUser", del_user_email, {headers: {'Content-Type': 'text/plain'}});
      console.log("탈퇴완료");
      this.logout();
    },
  }
}
</script>

<style>

</style>