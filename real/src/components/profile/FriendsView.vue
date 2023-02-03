<template>
  <HeaderVue
    :title="'친구 관리'"
  />
  <div id="edit-btn" @click="onEdit">
    <p v-if="!edit">관리</p>
    <p v-if="edit">취소</p>
  </div>
  <div class="container-col" style="height:75%; width: 100vw;">
    <div class="container-row" id="friend-search">
      <img class="header_menu" src="@/assets/Search_black.png">
      <input class="header_menu header_input" id="room_search" v-model="friendSearch" placeholder="친구 검색">
    </div>
    <FriendsItemView 
      :edit="edit"
      v-for="friend in friends"
      :key="friend.friendId"
      :friend="friend"
    />
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
    }
  },
  methods: {
    getFriends(){
      this.friends = [
        {
          friendId: 1,
          nickname: 'gㅎ하ㅣ힣',
        },
        {
          friendId: 2,
          nickname: '이채은',
        },
        {
          friendId: 3,
          nickname: '싸피싸피싸피',
        },
        {
          friendId: 4,
          nickname: 'ㄹㄴㅇ',
        },
      ]
    },
    onEdit() {
      this.edit = !this.edit
    }
  },
  created() {
    this.getFriends()
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
  input{
    border: none;
    background-color: #F5F5F5;
    padding-left: 7%;
    width: 100%;
  }
</style>