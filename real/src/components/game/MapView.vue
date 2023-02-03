<template>
    <GMapMap
      :center="player"
      :zoom="18"
      :options="{
        zoomControl: false,
        mapTypeControl: false,
        streetViewControl: false,
        fullscreenControl: false,
        minZoom: 15,
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
      <!-- 다른 사람 위치 -->
      <GMapMarker
        :icon=otherMarkerImg
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
import truePaper from '@/assets/TruePaper.png'

export default {
  name: 'MapView',
  data() {
    return {
      myMarker: true,
      otherMarkerImg: {
        url: truePaper,
        scaledSize: { width: 40, height: 40 }
      },
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
        {
          position: {
            lat: 36.0923108,
            lng: 128.4245156,
          },
        },
        {
          position: {
            lat: 36.1070778,
            lng: 128.4164495,
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
      // auto_reload: false,
      // auto_reload_delay: 1000,
      // auto_reload_func: null,
    };
  },
  mounted() {
    // 자이로 센서 인식
    window.addEventListener('deviceorientation', this.handleOrientation)
    // 내 위치
    this.myLocation()
    // setInterval(this.myLocation(),1000)
    
  },
  methods: {
    // 자이로 센서 인식
    handleOrientation(event) {
      const beta = event.beta
      // console.log(beta)
      if(beta > 130 || beta < 40) {
        this.myMarker = true
      } else {
        this.myMarker = false
      }
      // console.log(window)
    },

    // 내 위치
    myLocation() {
      this.$watchLocation({enableHighAccuracy: true})
        .then((coordinates) => {
          this.player.lat = coordinates.lat
          this.player.lng = coordinates.lng
          console.log(this.player.lat)
          console.log(this.player.lng)
          this.catch()
        })
        .catch((error) => {
          console.log(error)
        })
    },

    // deg2rad(deg) {
    //   return deg * (Math.PI/180)
    // },
    catch(){
      const lat1 = this.player.lat
      const lng1 = this.player.lng
      
      for(const marker of this.markers){
        const lat2 = marker.position.lat
        const lng2 = marker.position.lng
        const r = 6371; //지구의 반지름(km)
        const dLat = (lat2-lat1) * (Math.PI/180)
        const dLon = (lng2-lng1) * (Math.PI/180)
        const a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(lat1 * (Math.PI/180)) * Math.cos(lat2 * (Math.PI/180)) * Math.sin(dLon/2) * Math.sin(dLon/2);
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        const d = r * c; // Distance in km
        const distance = Math.round(d*1000);

        if(distance <= 50){
          console.log('잡을 수 있음')
          // alert('잡을 수 있음')
        } else {
          console.log('잡을 수 없음')
          // alert('잡을 수 있는 사람 없음')
        }
      }
    },
  },
  created() {
    // console.log(window)

  },
  watch: {
    // clearInterval(this.auto_reload_func)
    // player(){
    //   this.catch()
    // }
  }
};
</script>

<style>
body {
  margin: 0;
}
</style>
