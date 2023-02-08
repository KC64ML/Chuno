<template>
  <div>
    <div class="container-row">
      <div class="container-row">
        <img :src=" friend.profile? URL + 'resources/images?path=' + friend.profile.path :  defaultProfile" alt="profile" style="width:50px;">

        <span id="nickname">{{ friend.nickname }}</span>
      </div>
      <img src="@/assets/deleteFriend.svg" alt="delete" 
        v-if="edit"
        @click="deleteFriend()"
      >
    </div>
    <hr>
  </div>
</template>

<script>
import defaultProfile from '@/assets/profile_default.svg'

export default {
  name: 'FriendsItemView',
  props: {
    friend: Object,
    edit: Boolean,
  },
  data(){
    return {
      defaultProfile: defaultProfile,
      URL: process.env.VUE_APP_SPRING,
    }
  },
  methods:{
    deleteFriend(){
      const token = sessionStorage.token
      this.axios.delete(process.env.VUE_APP_SPRING + 'user/friend/' + this.friend.id, { headers: { Authorization: token } })
        .then((res) => {
          const code = res.data.result
          if(code) {
            this.$emit('get-friends')
          } else {
            console.log('code err')
          }
        })
        .catch((e) => {
          console.log(e)
        })
    }
  }
}
</script>

<style>
.container-row {
  display:flex;
  padding-top: 0px;
  padding-bottom: 0px;
}
</style>