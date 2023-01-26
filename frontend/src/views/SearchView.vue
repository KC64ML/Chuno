<template>
  <nav id="header">
    <router-link to="/">
      <img class="logo" alt="Chuno Logo" src="@/assets/round_logo.svg">
    </router-link>
    <form @submit.prevent="searchRoom">
      <input type="search" id="search" v-model="roomTitle" @input="searchRoom" placeholder="검색어를 입력하세요.">
      <!-- <button>검색</button> -->
    </form>
    <!-- <p>검색</p> -->
  </nav>
    <div>search</div>
    <div>{{ roomTitle }}</div>

</template>

<script>
export default {
  name: 'SearchView',
  data() {
    return {
      roomTitle: '',
    }
  },
  methods: {
    searchRoom() {
      // const moviedata = this.$store.state.movies
      // if (this.title) {
      //   const searchMovies = moviedata.filter(movie => {
      //     return movie.title.includes(this.title)
      //   })
      //   this.movies = searchMovies
      // } 
      // else {
      //   this.getList()
      // }
    },
  }
}
</script>

<style>

</style>