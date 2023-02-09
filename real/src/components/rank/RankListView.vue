<template>
  <div>
  <table
    style="overflow: scroll; width: 90vw;"
    class="scroll"
  >
    <colgroup>
      <col width="11%">
      <col width="35%">
      <col width="11%">
      <col width="14.5%">
      <col width="14.5%">
      <col width="11%">
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
    <!-- <div
      style="overflow: scroll;"
      class="scroll"
    > -->
  </table>

    <tbody>
      <tr
        v-for="(user, i) in users"
        :key="i"
      >
        <td v-if="i>=3"><span>{{ user.rank }}</span></td>
        <td 
          v-if="i>=3"
          @click="this.$router.push({ name: 'Profile', params: { uid: user.id } })"
        ><span>{{ user.nickname }}</span></td>
        <td v-if="i>=3"><span>{{ user.level }}</span></td>
        <td v-if="i>=3"><span>{{ user.runnerWinCount }}</span></td>
        <td v-if="i>=3"><span>{{ user.chaserWinCount }}</span></td>
        <td v-if="i>=3"><span>{{ user.userCountAvg }}</span></td>
      </tr>
      <tr
        v-for="(user, i) in users"
        :key="i"
      >
        <td v-if="i>=3"><span>{{ user.rank }}</span></td>
        <td 
          v-if="i>=3"
          @click="this.$router.push({ name: 'Profile', params: { uid: user.id } })"
        ><span>{{ user.nickname }}</span></td>
        <td v-if="i>=3"><span>{{ user.level }}</span></td>
        <td v-if="i>=3"><span>{{ user.runnerWinCount }}</span></td>
        <td v-if="i>=3"><span>{{ user.chaserWinCount }}</span></td>
        <td v-if="i>=3"><span>{{ user.userCountAvg }}</span></td>
      </tr>
    </tbody>
</div>

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