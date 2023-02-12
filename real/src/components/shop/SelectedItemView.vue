<template>
  <div
    class="box container-row selected"
    style="width: 80vw; height: 20vh; margin: 5% 0"
  >
    <div v-if="item">
      <img
        :src="URL + 'resources/images?path=' + item.imgPath"
        alt="item"
        style="height: 60px;margin-right:15px"
        @error="replaceByDefault"
      />
    </div>
    <div v-else>
      <div class="line-box"></div>
    </div>
    <div>
      <p style="margin-bottom:10px; font-size:20px;">{{ item?.name }}</p>
      <p>{{ item?.description }}</p>
      <div :class="item ? 'inner-box inner-selected' : 'inner-box inner-box-height'">
        <div class="inner">가격</div>
        <div class="inner">{{ item?.price }} 냥</div>
        <div class="button-black inner" @click="buy">구매</div>
      </div>
    </div>
  </div>
</template>

<script>
import img from "@/assets/no_image.png";

export default {
  name: "SelectedItemView",
  props: {
    item: Object,
  },
  data() {
    return {
      URL: process.env.VUE_APP_SPRING,
    };
  },
  methods: {
    buy() {
      console.log("구매 버튼");
      console.log(this.item);
      const token = sessionStorage.token;
      console.log(token);
      this.axios
        .post(process.env.VUE_APP_SPRING + "user/shop/" + this.item.id, "", {
          headers: { Authorization: token },
        })
        .then((res) => {
          const code = res.data.code;
          if (code) {
            console.log("샀다");
            this.$emit("get-user");
            console.log(res.data);
          } else {
            console.log("못샀다");
            console.log(res.data);
          }
        })
        .catch((e) => {
          console.log(e);
        });
    },
    replaceByDefault(e) {
      e.target.src = img;
    },
  },
  created() {
    console.log("item", this.item);
    console.log("item", this.item == null);
    console.log("item", this.item == {});
  },
};
</script>

<style>
.box {
  background-color: #f5f5f5;
  border-radius: 10%;
  padding: 5%;
  position: relative;
}
.inner-box {
  display: grid;
  grid-template-columns: 1fr 3fr 2fr;
  text-align: center;
}
.inner-selected {
  position: absolute;
  bottom: 10px;
}
.inner-box-height {
  height: 100%;
}
.selected {
  vertical-align: middle;
}
.button-black {
  background-color: #1d182c;
  color: #f5f5f5;
  border-radius: 10%;
  padding: 3% 5%;
}
.inner {
  margin-top: auto;
  margin-bottom: 10px;
}

.line-box {
  border: 3px solid black;
  width: 80px;
  height: 80px;
  border-radius: 20%;
}
</style>