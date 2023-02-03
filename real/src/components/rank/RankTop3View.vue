<template>
  <div class="container-row">
    <div
      v-for="user in users.filter((u) => u.rank < 4)"
      :key="user.nickname"
    >
      <img src="@/assets/crown_gold.png" alt="" v-if="user.rank == 1" id="crown">
      <img src="@/assets/crown_silver.png" alt="" v-if="user.rank == 2" id="crown">
      <img src="@/assets/crown_bronze.png" alt="" v-if="user.rank == 3" id="crown">
      <img :src="user.path" alt="profile" id="profile">
      <p>Lv{{ user.level }}</p>
      <p>{{ user.nickname }}</p>
      <p>{{ user.runner_win_count }} | {{ user.chaser_win_count }} | 0.7</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RankItemView',
  data() {
    return {
      users: [],
    }
  },
  methods: {
    getUsers() {
      this.users = [
        {
          rank: 1 ,
          nickname: '이채은',
          level: 3,
          runner_win_count: 12,
          chaser_win_count: 132,
          path: require('@/assets/profile_default.svg')
          // odds: 
        },
        {
          rank: 2,
          nickname: '이아름스스',
          level: 12,
          runner_win_count: 3,
          chaser_win_count: 5435,
          path: require('@/assets/profile_default.svg')
          // odds: 
        },
        {
          rank: 3,
          nickname: '이경창',
          level: 1,
          runner_win_count: 9,
          chaser_win_count: 76,
          path: require('@/assets/profile_default.svg')
          // odds: 
        },
        {
          rank: 4,
          nickname: '이주찬',
          level: 75,
          runner_win_count: 66,
          chaser_win_count: 72,
          path: require('@/assets/profile_default.svg')
          // odds: 
        },
        {
          rank: 5,
          nickname: '이동하',
          level: 65,
          runner_win_count: 21,
          chaser_win_count: 34,
          path: require('@/assets/profile_default.svg')
          // odds: 
        },
        {
          rank: 6,
          nickname: '이석훈',
          level: 90,
          runner_win_count: 2,
          chaser_win_count: 3,
          path: require('@/assets/profile_default.svg')
          // odds: 
        },
        {
          rank: 7,
          nickname: '투썸',
          level: 35,
          runner_win_count: 51,
          chaser_win_count: 3,
          path: require('@/assets/profile_default.svg')
          // odds: 
        },
        {
          rank: 8,
          nickname: '플레이스',
          level: 5,
          runner_win_count: 1,
          chaser_win_count: 43,
          path: require('@/assets/profile_default.svg')
          // odds: 
        },
      ]
    },
  },
  mounted() {
    this.getUsers()
  }
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