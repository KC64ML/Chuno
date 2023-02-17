<template>
  <HeaderVue :title="'랭킹'"></HeaderVue>

  <div
    v-if="this.users != null"
    class="childernbar_up"
  >
    <RankTop3View :users="users" />

  </div>
  
  <div class="childernbar_down">
    <RankListView :users="users" :myId="myId" />
  </div>
</template>

<script>
import HeaderVue from "@/components/HeaderVue.vue";
import RankListView from "@/components/rank/RankListView.vue";
import RankTop3View from "@/components/rank/RankTop3View.vue";

export default {
  name: "RankView",
  components: {
    HeaderVue,
    RankTop3View,
    RankListView,
  },
  data() {
    return {
      users: undefined,
      myId: null,
    };
  },
  methods: {
    getUser() {
      const token = sessionStorage.token;
      this.axios
        .get(process.env.VUE_APP_SPRING + "user", {
          headers: { Authorization: token },
        })
        .then((res) => {
          const code = res.data.code;
          if (code) {
            this.myId = res.data.result;
          } else {
            console.log("code err");
          }
        });
    },

    getRank() {
      const token = sessionStorage.token;
      this.axios
        .get(process.env.VUE_APP_SPRING + "user/rank", {
          headers: { Authorization: token },
        })
        .then((res) => {
          this.users = res.data;
          console.log("****", res.data);
        })
        .catch(() => {
          console.log("error");
        });
    },
    onGame() {
      this.$router.push({ name: "game", params: { roomId: 1 } });
    },
  },
  created() {
    this.getRank();
  },
};
</script>

<style scoped>
.childernbar_up {
  /* bottom: 120%; */
  margin-bottom: 80%;
}

.childernbar_down {
  height: 80%; 
}
</style>