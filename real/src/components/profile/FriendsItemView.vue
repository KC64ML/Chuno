<template>
  <div>
    <div class="container-row">
      <div class="container-row">
        <img src="@/assets/profile_default.svg" alt="profile" style="width:50px;">
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
export default {
  name: 'FriendsItemView',
  props: {
    friend: Object,
    edit: Boolean,
  },
  methods:{
    deleteFriend(){
      const token = sessionStorage.token
      this.axios.delete(process.env.VUE_APP_SPRING + 'user/friend/' + this.friend.id, { headers: { Authorization: token } })
        .then((res) => {
          console.log(res)
          console.log('성공')
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