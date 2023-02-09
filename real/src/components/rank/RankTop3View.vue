<template>
  <div class="container-row">
    <div
      v-for="user in users"
      :key="user.id"
    >
      <div 
        v-if="user.rank < 3"
      >
        <img src="@/assets/crown_gold.png" alt="" v-if="user.rank == 0" id="crown">
        <img src="@/assets/crown_silver.png" alt="" v-if="user.rank == 1" id="crown">
        <img src="@/assets/crown_bronze.png" alt="" v-if="user.rank == 2" id="crown">
        <img 
          :src=" user.profile ? URL + 'resources/images?path=' + user.profile :  defaultProfile" 
          alt="profile" 
          id="profile"
          @click="this.$router.push({ name: 'Profile', params: { uid: user.id } })"
        >
        <p>Lv.{{ user.level }}</p>
        <p @click="this.$router.push({ name: 'Profile', params: { uid: user.id } })">{{ user.nickname }}</p>
        <p>{{ user.runnerWinCount }} | {{ user.chaserWinCount }} | {{ user.userCountAvg }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import defaultProfile from '@/assets/profile_default.svg'

export default {
  name: 'RankItemView',
  data() {
    return {
      defaultProfile: defaultProfile,
    }
  },
  props: {
    users: Object
  },
}
</script>

<style>
.container-row{
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding: 5%;
}

#crown {
  /* height: 30px; */
  width: 30px;
  position: absolute;
}
</style>