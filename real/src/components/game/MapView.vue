<template>
    <GMapMap
      :center="player"
      :zoom="18"
      :options="{
        zoomControl: true,
        mapTypeControl: false,
        streetViewControl: false,
        fullscreenControl: false,
        minZoom: 11,
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
          :radius="500"
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
import randomLocation from 'random-location'

export default {
  name: 'MapView',
  data() {
    return {
      myMarker: true,
      otherMarkerImg: {
        url: truePaper,
        scaledSize: { width: 40, height: 40 }
      },
      markers: [],
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
      
      // OpenVidu objects
      OV: undefined,
      session: undefined,
      myVideoStream: null,
      mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],

      // Join form
      mySessionId: "SessionA",
      myUserName: "Participant" + Math.floor(Math.random() * 100),
    };
  },
  methods: {
    // 노비 문서 생성
    generatePapers(){
      for(let i = 0; i < 10; i++) {
        const randomPoint = randomLocation.randomCirclePoint({latitude: 36.107084, longitude: 128.420823}, 400)
        this.markers.push({ position: { lat: randomPoint.latitude, lng: randomPoint.longitude } })
      }
      console.log(this.markers)
    },
    
    // Open vidu
    // sendData() {
    //   ssession.connect(token, "USER_DATA")
    //   .then(
    //     console.log(USER_DATA)
    //   )
    //   .catch(
    //     console.log('err')
    //   )
  
    //   session.on("streamCreated", function (event) {
    //     session.subscribe(event.stream, "subscriber");
    //     console.log("USER DATA: " + event.stream.connection.data);
    //   });
    // },

    
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
    
    // 자이로 센서 인식
    handleOrientation(event) {
      const beta = event.beta
      console.log(beta)
      if(beta > 130 || beta < 40) {
        this.myMarker = true
      } else {
        this.myMarker = false
      }
      // console.log(window)
    },

    // 노비문서 찢기, 노비 잡기
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

        if(distance <= 500){
          console.log('잡을 수 있음')
        } else {
          console.log('잡을 수 없음')
        }
      }
    },
  },
  created() {
    // 자이로 센서 인식
    window.addEventListener('deviceorientation', this.handleOrientation)
    // 내 위치
    this.myLocation()
    // setInterval(this.myLocation(),1000)
    this.generatePapers()
  },
  watch: {
  }
};
</script>

<style>
body {
  margin: 0;
}
</style>
