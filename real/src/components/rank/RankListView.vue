<template>
  <div class="rank-result-list">
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
          <th style="-webkit-text-stroke: medium">순위</th>
          <th style="-webkit-text-stroke: medium">닉네임</th>
          <th style="-webkit-text-stroke: medium">레벨</th>
          <th style="-webkit-text-stroke: medium">노비승</th>
          <th style="-webkit-text-stroke: medium">추노승</th>
          <th style="-webkit-text-stroke: medium">승률</th>
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

.rank-result-list{
  font-family: "WandohopeR";
  margin-top: 300px;
  position: relative;
  z-index: 0;
  overflow-wrap: initial;
}

</style>