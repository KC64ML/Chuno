<template>
  <div class="container-row" style="justify-content: center">
    <div style="margin: 0.5rem">
      <div class="list_rank2">
        <img src="@/assets/crown_silver.png" alt="" id="crown_rank_2" />
        <img
          :src="
            this.userRank2.path
              ? URL + 'resources/images?path=' + this.userRank2.path
              : defaultProfile
          "
          alt="profile"
          class="profile"
          @click="
            this.$router.push({
              name: 'Profile',
              params: { uid: this.userRank2.id },
            })
          "
        />

        <p>Lv.{{ this.userRank2.level }}</p>
        <p
          @click="
            this.$router.push({
              name: 'Profile',
              params: { uid: this.userRank2.id },
            })
          "
        >
          {{ this.userRank2.nickname }}
        </p>
        <p>
          {{ this.userRank2.runnerWinCount }} |
          {{ this.userRank2.chaserWinCount }} |
          {{ this.userRank2.userCountAvg }}
        </p>
      </div>

      <div id="list_rank1">
        <img src="@/assets/crown_gold.png" alt="" id="crown_rank_1" />
        <img
          :src="
            this.userRank1.path
              ? URL + 'resources/images?path=' + this.userRank1.path
              : defaultProfile
          "
          alt="profile"
          class="profile"
          @click="
            this.$router.push({
              name: 'Profile',
              params: { uid: this.userRank1.id },
            })
          "
        />

        <p>Lv.{{ this.userRank1.level }}</p>
        <p
          @click="
            this.$router.push({
              name: 'Profile',
              params: { uid: this.userRank1.id },
            })
          "
        >
          {{ this.userRank1.nickname }}
        </p>
        <p>
          {{ this.userRank1.runnerWinCount }} |
          {{ this.userRank1.chaserWinCount }} |
          {{ this.userRank1.userCountAvg }}
        </p>
      </div>

      <div class="list_rank3">
        <img src="@/assets/crown_bronze.png" alt="" id="crown_rank_3" />
        <img
          :src="
            this.userRank3.path
              ? URL + 'resources/images?path=' + this.userRank3.path
              : defaultProfile
          "
          alt="profile"
          class="profile"
          @click="
            this.$router.push({
              name: 'Profile',
              params: { uid: this.userRank3.id },
            })
          "
        />

        <p>Lv.{{ this.userRank3.level }}</p>
        <p
          @click="
            this.$router.push({
              name: 'Profile',
              params: { uid: this.userRank3.id },
            })
          "
        >
          {{ this.userRank3.nickname }}
        </p>
        <p>
          {{ this.userRank3.runnerWinCount }} |
          {{ this.userRank3.chaserWinCount }} |
          {{ this.userRank3.userCountAvg }}
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import defaultProfile from "@/assets/profile_default.svg";

export default {
  name: "RankItemView",
  data() {
    return {
      defaultProfile: defaultProfile,
      URL: process.env.VUE_APP_SPRING,
      userRank1: [],
      userRank2: [],
      userRank3: [],
    };
  },
  props: {
    users: Object,
  },
  created() {
    console.log("실행 하겠습니다.");
    console.log("-----------", this.users, "-------------");
    this.users.forEach((user) => {
      if (user.rank == 1) {
        this.userRank1 = user;
      } else if (user.rank == 2) {
        this.userRank2 = user;
      } else if (user.rank == 3) {
        this.userRank3 = user;
      }
    });

    console.log(this.userRank1);
  },
};
</script>

<style>


#list_rank1 {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  align-items : center;
}

.list_rank2 {
  position: absolute;
  left: 20%;
  margin-top: 20%;
}

.list_rank3 {
  position: absolute;
  right: 20%;
  margin-top: 20%;
}

.container-row {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding: 5%;
}

#crown_rank_1 {
  /* height: 30px; */
  width: 30px;
  position: absolute;
  margin-left: 20px;
  transform: translate(0%, -80%);
}
#crown_rank_2 {
  /* height: 30px; */
  width: 30px;
  position: absolute;
  margin-left: 20px;
  transform: translate(0%, -80%);
}
#crown_rank_3 {
  /* height: 30px; */
  width: 30px;
  position: absolute;
  margin-left: 20px;
  transform: translate(0%, -80%);
}

.profile {
  width: 70px;
  height: 70px;
  border-radius: 50%;
}
</style>