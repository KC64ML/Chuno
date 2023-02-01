<template>
  <GMapMap
    :center="player"
    :zoom="18"
    :options="{
      zoomControl: false,
      mapTypeControl: false,
      streetViewControl: false,
      fullscreenControl: false,
      minZoom: 16,
      maxZoom: 18,
    }"
    style="width: 100vw; height: 25rem"
  >

    <GMapMarker
      :animation=4
      :position=this.player
    />
    <GMapMarker
      :key="index"
      :animation=1
      v-for="(m, index) in markers"
      :position="m.position"
      :clickable="true"

      @click="center = m.position"
    />

    <GMapCircle
      :radius="50"
      :center="player"
      :options="circleOptions"
    />
  </GMapMap>
</template>

<script>
export default {
  name: {},
  data() {
    return {
      center: { lat: 36.0, lng: 128.1163344 },
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
          lat: 36.107,
          lng: 128.420,
      },
      circleOptions: {
        strokeColor: "#0000FF",
        strokeOpacity: 0.3,
        strokeWeight: 2,
        fillColor: "#0000FF",
        fillOpacity: 0.15,
      },

    };
  },
  mounted() {
      navigator.geolocation.getCurrentPosition(
      (position) => {
          this.player.lat= position.coords.latitude;
          this.player.lng= position.coords.longitude;
          // this.center.lat = position.coords.latitude;
          // this.center.lng = position.coords.longitude;
      })
  },

};
</script>

<style>
body {
  margin: 0;
}
</style>
