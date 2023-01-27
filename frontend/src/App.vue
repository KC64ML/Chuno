<template>
  <div id="app">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <router-link to="/start">Start</router-link> |
    <router-link to="/login">Login</router-link>
    <router-view />

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
  background-image: url("@/assets/background.png");
  position: relative;
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
  position: fixed;
  bottom: 0%;
  background-color: #1D182C;
  /* background-image: url(@/assets/footer.png); */
  width: 50vw;
  /* height: 10vh; */
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding: 5%;
}

#default img {
  width: 50px;
  height: auto;
  /* padding-left: 10px;
  padding-right: 10px; */
}

/* 모달 */
.modal-bg {
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  position: fixed;
  padding: 20px;
}

.modal {
  position: fixed;
  background-color: #F5F5F5;
  border-radius: 10%;
  width: 70vw;
}

/* 헤더 */
#header {
  display: flex;
  flex-direction: row;
  padding: 5%;
  font-size: 20pt;
}

#header img {
  height: 50px;
  width: 50px;
  padding-right: 10px;
}

#header p {
  margin: 0;
  vertical-align: middle;

}
</style>
