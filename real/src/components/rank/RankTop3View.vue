<template>
  <div class="container-row">
    <div
      v-for="(user, i) in users"
      :key="i"
    >
      <div v-if="i < 3">
        <img src="@/assets/crown_gold.png" alt="" v-if="i == 0" id="crown">
        <img src="@/assets/crown_silver.png" alt="" v-if="i == 1" id="crown">
        <img src="@/assets/crown_bronze.png" alt="" v-if="i == 2" id="crown">
        <img :src="user.path" alt="profile" id="profile">
        <p>Lv.{{ user.level }}</p>
        <p>{{ user.nickname }}</p>
        <p>{{ user.runnerWinCount }} | {{ user.chaserWinCount }} | {{ user.userCountAvg }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RankItemView',
  data() {
    return {
      // users: [],
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