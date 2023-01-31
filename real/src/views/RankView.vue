<template>
  <HeaderVue></HeaderVue>
  <nav id="header">
    <router-link to="/home">
      <img class="logo" alt="Chuno Logo" src="@/assets/main_logo2.png">
    </router-link>
    <p>랭크</p>
  </nav>
  
  <div>rank</div>
</template>

<script>
import HeaderVue from '@/components/HeaderVue.vue';

export default {
  name: 'RankView',
  components: {
    HeaderVue
  }
}
</script>

<style>

</style>