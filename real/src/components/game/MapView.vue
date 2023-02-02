<template>
  <GMapMap
    :center="player"
    :zoom="18"
    :options="{
      zoomControl: false,
      mapTypeControl: false,
      streetViewControl: false,
      fullscreenControl: false,
      minZoom: 10,
      maxZoom: 18,
    }"
    style="width: 100vw; height: 25rem"
  >
    <!-- 내 위치 -->
    <div v-if="myMarker">
      <GMapMarker
        :animation=4
        :position=this.player
      />
      <GMapCircle
        :radius="50"
        :center="player"
        :options="circleOptions"
      />
    </div>

    <GMapMarker
      :key="index"
      :animation=1
      v-for="(m, index) in markers"
      :position="m.position"
      :clickable="true"
      @click="center = m.position"
    />

  </GMapMap>
</template>

<script>
export default {
  name: {},
  data() {
    return {
      myMarker: true,
      // center: { lat: 36.0, lng: 128.1163344 },
      markers: [
        {
          position: {
            lat: 36.108135,
            lng: 128.42335,
          },
        },
        {
          position: {
            lat: 36.10565,
            lng: 128.42335,
          },
        },
      ],
      player: {
        lat: null,
        lng: null,
      },
      circleOptions: {
        strokeColor: "#0000FF",
        strokeOpacity: 0.3,
        strokeWeight: 2,
        fillColor: "#0000FF",
        fillOpacity: 0.15,
      },
      // 
      auto_reload: false,
      auto_reload_delay: 1000,
      auto_reload_func: null,
    };
  },
  mounted() {
    // 자이로 센서 인식
    window.addEventListener('deviceorientation', this.handleOrientation)
    // 내 위치
    this.myLocation()
  },
  methods: {
    handleOrientation(event) {
      const beta = event.beta
      console.log(beta)
      if(beta > 130 || beta < 40) {
        this.myMarker = true
      } else {
        this.myMarker = false
      }
      console.log(this.myMarker)
    },
    myLocation() {
        this.$watchLocation({enableHighAccuracy: true})
        .then((coordinates) => {
          // console.log(coordinates)
            this.player.lat = coordinates.lat
            this.player.lng = coordinates.lng
            console.log(this.player.lat)
            console.log(this.player.lng)
        })
        .catch((error) => {
            console.log(error);
      })
    }
  },
  created() {
    // this.myLocation()
  },
  unmounted() {
    // clearInterval(this.auto_reload_func)
  }
};
</script>

<style>
body {
  margin: 0;
}
</style>
