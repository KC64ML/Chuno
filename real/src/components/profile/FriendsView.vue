<template>
  <div>
  <HeaderVue
    :title="'친구 관리'"
  />
  <div id="edit-btn" @click="onEdit">
    <p v-if="!edit">관리</p>
    <p v-if="edit">취소</p>
  </div>

  <div style="height: 80%; width:100vw; overflow: scroll;" class="scroll">
    <div class="container-row" id="friend-search" style="position:">
      <img class="header_menu" src="@/assets/Search_black.png">
      <input class="header_menu header_input" id="room_search" v-model="friendSearch" placeholder="친구 검색">
    </div>
    <div v-if="this.friendSearch">
      <div v-if="friends.length>0">${this.friends}</div>
    </div>
    <div v-if="!search">
      <FriendsItemView 
      @get-friends="getFriends"
      :edit="edit"
      v-for="friend in friends"
      :key="friend.friendId"
      :friend="friend"
      />
    </div>
    <div v-if="search">
      <div v-if="friends.length">
        <FriendsItemView 
          @get-friends="getFriends"
          :edit="edit"
          v-for="friend in friends"
          :key="friend.friendId"
          :friend="friend"
        />
      </div>
      <div v-else>
        "{{ friendSearch }}" 검색 결과 없음
      </div>
    </div>
  </div>
</div>
</template>

<script>
import HeaderVue from '../HeaderVue.vue';
import FriendsItemView from './FriendsItemView.vue';

export default {
  name: 'FriendsView',
  components: {
    HeaderVue,
    FriendsItemView,
  },
  data() {
    return {
      friends: [],
      friendSearch: '',
      edit: false,
      serach: false,
    }
  },
  methods: {
    getFriends(){
      const token = sessionStorage.token
      this.axios.get(process.env.VUE_APP_SPRING + 'user/friend', { headers: { Authorization: token } })
        .then((res) => {
          const code = res.data.code
          if(code) {
            console.log('친구 겟또')
            this.friends = res.data.result
            console.log(this.friends)

          } else {
            console.log('실패')
          }
        })
    },
    search(){
      this.friends = true
      const token = sessionStorage.token
      console.log('----')
      this.axios.get(process.env.VUE_APP_SPRING + 'user/friend/search/' + this.friendSearch, { headers: { Authorization: token }})
      .then((res) => {
        const code = res.data.code
        if(code) {
          console.log('친구 검색함')
          this.friends = res.data.result
          this.search = true
        } else{
          console.log('검색 결과 없음')
          this.search = false
        }
      })
    },
    onEdit() {
      this.edit = !this.edit
    }
  },
  created() {
    this.getFriends()
  },
  watch: {
    'friendSearch': 'serch'
  }
}
</script>

<style>
  #edit-btn{
    position: absolute;
    top: 2%;
    right: 10%;
    border: solid 1px black;
    padding: 1.5% 3%;
    background-color: #F5F5F5;
    border-radius: 5%;
  }
  #friend-search{
    border: solid 1px black;
    border-radius: 5%;
    background-color: #F5F5F5;
    padding: 3% 5%;
    margin: 0 20%;
    justify-content: center;
  }
  #room_search{
    border: none;
    background-color: #F5F5F5;
    padding-left: 7%;
    width: 100%;
  }
</style>