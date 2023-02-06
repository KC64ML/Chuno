<template>
  <HeaderVue
    :title="'랭킹'"
  ></HeaderVue>
  <div>
    <RankTop3View
      :users="users"
    />
    <RankListView
      :users="users"
    />
    <hr>
    <p @click="onGame">
      game router test
    </p>
  </div>
</template>

<script>
import HeaderVue from '@/components/HeaderVue.vue';
import RankListView from '@/components/rank/RankListView.vue';
import RankTop3View from '@/components/rank/RankTop3View.vue';

export default {
  name: 'RankView',
  components: {
    HeaderVue,
    RankTop3View,
    RankListView,
  },
  data() {
    return {
      users: []
    }
  },
  methods:{
    getRank(){
      const token = sessionStorage.token
      this.axios.get(process.env + 'user/priority', { headers: { Authorization: token } })
      .then((res) => {
        const code = res.data.code
        if(code) {
          this.users = res.data.result
        } else {
          console.log('code error')
        }
      })
      .catch(() => {
        console.log('error')
        this.users = [
          {
            "nickname": "아리따움",
            "level": 1,
            "runnerWinCount": 30,
            "chaserWinCount": 15,
            "userCountAvg": 75
          },
          {
            "nickname": "추노장인",
            "level": 1,
            "runnerWinCount": 15,
            "chaserWinCount": 6,
            "userCountAvg": 52
          },
          {
            "nickname": "큰개님",
            "level": 1,
            "runnerWinCount": 4,
            "chaserWinCount": 3,
            "userCountAvg": 46
          },
          {
            "nickname": "노예킹",
            "level": 1,
            "runnerWinCount": 10,
            "chaserWinCount": 1,
            "userCountAvg": 45
          },
          {
            "nickname": "아름병아리",
            "level": 1,
            "runnerWinCount": 43,
            "chaserWinCount": 32,
            "userCountAvg": 41
          },
          {
            "nickname": "성곡초짱이채은",
            "level": 1,
            "runnerWinCount": 3,
            "chaserWinCount": 2,
            "userCountAvg": 26
          }
        ]
        console.log(this.users)
      })
    },
    onGame() {
      this.$router.push({name: 'game', params: { roomId: 1 }})
    }
  },
  created() {
    this.getRank()
  }
}
</script>

<style>

</style>