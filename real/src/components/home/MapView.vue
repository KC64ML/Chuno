<template>
  <GMapMap
    :center="player"
    :zoom="zoom"
    :options="{
      zoomControl: false,
      mapTypeControl: false,
      streetViewControl: false,
      fullscreenControl: false,
      minZoom: 10,
      maxZoom: 18,
    }"
    style="width: 30rem; height: 30rem"
  >

    <GMapMarker
      :animation=4
      :position=this.player
    />

    <GMapCircle
      :radius="radius"
      :center="player"
      :options="circleOptions"
    />
  </GMapMap>
</template>

<script>

export default {
  props:{
    radius: Number,
  },
  data() {
    return {
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
      zoom: 15

    };
  },
  mounted() {
      navigator.geolocation.getCurrentPosition(
      (position) => {
          this.player.lat= position.coords.latitude;
          this.player.lng= position.coords.longitude;
      })
  },
  watch: {
    radius(r) {
      if(r <=550){
        this.zoom = 15
      } else {
        this.zoom = 14
      }
    }
  }

};
</script>

<style>
/* body {
  margin: 0;
} */
</style>
