<template>
  <div>
    <table
      style="width: 85vw;"
    >
      <colgroup>
        <col width="12%">
        <col width="30%">
        <col width="13%">
        <col width="15.5%">
        <col width="15.5%">
        <col width="12%">
      </colgroup>
      <thead>
        <tr>
          <th>순위</th>
          <th>닉네임</th>
          <th>레벨</th>
          <th>노비승</th>
          <th>추노승</th>
          <th>승률</th>
        </tr>
      </thead>
    </table>

    <div
      style="overflow: scroll; width: 85vw; height: 40vh;"
      class="scroll"
    >
    <table style="width: 85vw;">
      <colgroup>
        <col width="12%">
        <col width="30%">
        <col width="13%">
        <col width="15.5%">
        <col width="15.5%">
        <col width="12%">
      </colgroup>

      <tbody>
        <tr
          v-for="user in users"
          :key="user.id"
          :class="{ 'rank-bg' : user.id == myId}"
        >
          <td v-if="user.rank>3" style="text-align:center;"><span>{{ user.rank }}</span></td>
          <td 
            v-if="user.rank>3"
            @click="this.$router.push({ name: 'Profile', params: { uid: user.id } })"
          ><span>{{ user.nickname }}</span></td>
          <td v-if="user.rank>3" style="text-align:center;"><span>{{ user.level }}</span></td>
          <td v-if="user.rank>3" style="text-align:center;"><span>{{ user.runnerWinCount }}</span></td>
          <td v-if="user.rank>3" style="text-align:center;"><span>{{ user.chaserWinCount }}</span></td>
          <td v-if="user.rank>3" style="text-align:center;"><span>{{ user.userCountAvg }}</span></td>
        </tr>
      </tbody>
    </table>
  </div>

  </div>

</template>

<script>
export default {
  name: 'RankListView',
  props: {
    users: Object,
    myId: Number,
  }
    
}
</script>

<style scoped>
th {
  padding: 0.3rem;
}
td {
  padding: 0.3rem;
}
.rank-bg{
  background-color: #F9CF65;
}
</style>