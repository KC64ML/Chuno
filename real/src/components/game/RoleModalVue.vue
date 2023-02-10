<template>
  <div id="role_container">
    <div class="centering" style="margin-top: 40px">
      <img src="@/assets/main_logo2.png" alt="" />
    </div>
    <div class="centering big_text">당신은</div>
    <div class="centering role_text">
      {{ my_role }}
    </div>
    <div class="centering big_text">입니다.</div>
    <div style="display: flex; justify-content: space-evenly">
      <div>
        <div class="centering big_text" style="margin-bottom: 10px">노비팀</div>
        <div v-for="(p, idx) in teamslave" :key="idx">
          <RoleCardVue :comp="p == nickname ? 'yellow' : 'white'" :nickname="p">
          </RoleCardVue>
        </div>
      </div>
      <div>
        <div class="centering big_text" style="margin-bottom: 10px">추노팀</div>
        <div v-for="(p, idx) in teamchuno" :key="idx">
          <RoleCardVue :comp="p == nickname ? 'yellow' : 'white'" :nickname="p">
          </RoleCardVue>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import RoleCardVue from "@/components/game/RoleCardVue.vue";

export default {
  components: {
    RoleCardVue,
  },
  data() {
    return {
      info: undefined,
      nickname : '',
      teamchuno: [],
      teamslave: [],
      my_role: "undefined",
    };
  },
  async created() {
    this.info = JSON.parse(sessionStorage.getItem("info"));
    const token = sessionStorage.token;
    this.nickname = await this.axios
        .get(process.env.VUE_APP_SPRING + "user", {
          headers: { Authorization: token },
        })
        .then((res) => res.data.result.nickname);
    this.teamchuno = this.info.teamchuno.map((e) => e.nickname);
    this.teamslave = this.info.teamslave.map((e) => e.nickname);
    if (this.teamchuno.includes(this.nickname)) this.my_role = "추노꾼";
    else if (this.teamslave.includes(this.nickname)) this.my_role = "노비";
  },
};
</script>

<style lang="scss" scoped>
$big_text_size: 24px;

#role_container {
  width: 100vw;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  background-image: url("@/assets/main_back.png");
  background-size: cover;
  z-index: 1000;
}
.centering {
  text-align: center;
}
.big_text {
  font-size: $big_text_size;
  margin: 20px 0;
}
.role_text {
  font-size: 48px;
  font-weight: 800;
}
.yellow {
  margin-bottom: 10px;
  background-color: #f9cf65;
}
.white {
  margin-bottom: 10px;
  background-color: white;
}
</style>