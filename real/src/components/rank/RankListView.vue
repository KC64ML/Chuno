<template>
  <table>
    <tr>
      <th>순위</th>
      <th>닉네임</th>
      <th>레벨</th>
      <th>노비승</th>
      <th>추노승</th>
      <th>승률</th>
    </tr>
    <tr
      v-for="(user, i) in users"
      :key="i"
    >
      <td v-if="i>=3"><span>{{ i + 1 }}</span></td>
      <td 
        v-if="i>=3" @click="onProfile"
        @click="this.$router.push(`/profile/${user.id}`)"
      ><span>{{ user.nickname }}</span></td>
      <td v-if="i>=3"><span>{{ user.level }}</span></td>
      <td v-if="i>=3"><span>{{ user.runnerWinCount }}</span></td>
      <td v-if="i>=3"><span>{{ user.chaserWinCount }}</span></td>
      <td v-if="i>=3"><span>{{ user.userCountAvg }}</span></td>
    </tr>
  </table>

</template>

<script>
export default {
  name: 'RankListView',
  props: {
    users: Object
  }
    
}
</script>

<style>
th {
  padding: 0.3rem;
}
td {
  padding: 0.3rem;
}

</style>