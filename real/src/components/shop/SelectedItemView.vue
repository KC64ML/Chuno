<template>
  <div class="box container-row selected" style="width:80vw; height: 20vh; margin:5% 0;">
    <img :src="URL + 'resources/images?path=' + item.imgPath" alt="item" style="height:60px;">
    <div>
      <p>{{ item.name }}</p>
      <p>{{ item.description }}</p>
      <div class="container-row">
        <p>가격</p>
        <p>{{ item.price }} 냥</p>
        <p class="button-black" @click="buy">구매</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SelectedItemView',
  props: {
    item: Object,
  },
  data(){
    return {
      URL: process.env.VUE_APP_SPRING,
    }
  },
  methods: {
    buy(){
      console.log('구매 버튼')
      console.log(this.item)
      const token = sessionStorage.token
      console.log(token)
      this.axios.post(process.env.VUE_APP_SPRING + 'user/shop/' + this.item.id, '', { headers: { Authorization: token } })
        .then((res) => {
          const code = res.data.code
          if(code) {
            console.log('샀다')
            this.$emit('get-user')
            console.log(res.data)
          } else{
            console.log('못샀다')
            console.log(res.data)
          }
        })
        .catch((e) => {
          console.log(e)
        })
    }
  },

}
</script>

<style>
.box {
  background-color: #F5F5F5;
  border-radius: 10%;
  padding: 5%;
}
.selected {
  vertical-align: middle;
}
.button-black{
  background-color: #1D182C;
  color: #F5F5F5;
  border-radius: 10%;
  padding: 3% 5%;
}
</style>