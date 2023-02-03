<template>
  <div>
    <div class="container-row">
      <div class="container-row">
        <img src="@/assets/profile_default.svg" alt="profile" style="width:50px;">
        <span id="nickname">{{ friend.nickname }}</span>
      </div>
      <img src="@/assets/deleteFriend.svg" alt="delete" v-if="edit">
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