<template>
  <div class="myProfile">
    <div>
      <img src="@/assets/account_default.svg" alt="profile pic" class="uploadedImg">
    </div>
    <div>
      <p>My Profile</p>
      <p>nickname</p>
    </div>
    <div @click="$emit('onEdit')">
      <img src="@/assets/EditButton.svg" alt="edit">
    </div>

  </div>
</template>

<script>
export default {
  name: 'MyProfile',
  components: {
  }
}
</script>

<style>
.myProfile {
  display: flex;
  flex-direction: row;
  background-color: #F5F5F5;
  border-radius: 10%;
}
</style>