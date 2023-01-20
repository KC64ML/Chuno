<template>
  <div id="app">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <nav id="header">
      <router-link to="/">
        <img class="logo" alt="Chuno Logo" src="@/assets/round_logo.svg">
      </router-link>
      <br>
      <router-link to="/start">Start</router-link> |
      <router-link to="/login">Login</router-link> 
    </nav>
    <router-view/>
    
    <nav id="default">
      <!-- <img alt="Shop" src="@/assets/home.svg"> -->
      <router-link :to="{ name: 'Search' }">
        <img alt="search" src="@/assets/search.svg">
      </router-link>
      <router-link :to="{ name: 'Profile' }">
        <img alt="profile" src="@/assets/account.svg">
      </router-link>
      <router-link :to="{ name: 'Shop' }">
        <img alt="shop" src="@/assets/shop.svg">
      </router-link>
      <router-link :to="{ name: 'Rank' }">
        <img alt="rank" src="@/assets/rank.svg">
      </router-link>
      </nav>
  </div>
</template>

<style>
@font-face {
    font-family: 'WandohopeR';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-10@1.0/WandohopeR.woff') format('woff');
    font-weight: bold;
    font-style: normal;
}
body {
  font-family: 'WandohopeR';
  text-align: center;
  background-image: url( "@/assets/background.png" );
  position:relative;
  /* /* width: 100vw; */
  height: 100%;
  /* background-size: cover; */
}
nav img {
  height: 100px;
}
nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
#default {
  /* position: fixed; */
  background-color: #1D182C;
  /* background-image: url(@/assets/footer.png); */
  /* width: 100%; */
  /* height: 10vh; */
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding: 5%;
}
#default img{
  width: 50px;
  height: auto;
  /* padding-left: 10px;
  padding-right: 10px; */
}
</style>
