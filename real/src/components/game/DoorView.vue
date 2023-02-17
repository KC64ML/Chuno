<template>
   <div class="wrapper" @click="open">
      <img src="https://source.unsplash.com/mou0S7ViElQ" alt="minion">
      <div :class="opened ? 'door open-left-door left-door' : 'door left-door'">
        <div class="shape"></div>
        <div class="shape"></div>
        <div id="left-knob" class="knob"></div>
      </div>
      <div :class="opened ? 'door open-right-door right-door' : 'door right-door'">
        <div class="shape"></div>
        <div class="shape"></div>
        <div id="right-knob" class="knob"></div>
      </div>
    </div>
    <!-- <button @click="open">문 열기</button> -->
</template>

<script>
export default {
  name: 'DoorView',
  data(){
    return {
      opened: false
    }
  },

  methods: {
    open(){
      this.opened = true
    }
  }
}
</script>

<style scoped>
 /* General Style */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

/* body {
  height: 100vh;
  width: 100%;
  background-color: gray;
  display: flex;
  justify-content: center;
  align-items: center;
} */

.wrapper {
  height: 100vh;
  /* width: 250px; */
  /* border: 5px solid #61412D; */
  position: relative;
  /* 3D */
  perspective: 1000px;
}

img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.door {
  background-color: #61412D;
  height: 100vh;
  width: 50%;
  position: absolute;
  /* inner layout */
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
}

.left-door {
  top: 0;
  left: 0;
  border-right: 1px solid black;
  /* transition */
  transform-origin: left;
  transition: transform 3s;
}

.right-door {
  top: 0;
  right: 0;
  border-left: 1px solid black;
  /* transition */
  transform-origin: right;
  transition: transform 3s;
}

/* .shape {
  border: 4px solid black;
  width: 100px;
  height: 130px;
} */

/* .knob {
  width: 10px;
  height: 50px;
  background-color: #D7DADE;
  position: absolute;
} */

#left-knob {
  top: 50%;
  right: 10px;
}

#right-knob {
  top: 50%;
  left: 10px;
}

/* Door swing effect */
.open-left-door{
  /* transform: rotateY(-140deg); */
  transform: rotateY(140deg);
}

.open-right-door{
  /* transform: rotateY(140deg); */
  transform: rotateY(-140deg);
}

</style>