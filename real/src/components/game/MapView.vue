<template>
    <GMapMap
      :center="me"
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
      <!-- 플레이 영역 표시 -->
      <GMapCircle
        :radius="roomInfo.radius"
        :center="{lat: roomInfo.lat, lng: roomInfo.lng}"
        :options="playgroundOptions"
      />
      <!-- 내 위치 -->
      <div v-if="myMarker">
        <GMapMarker
          :animation=4
          :position=this.me
        />
        <GMapCircle
          :radius="catchRadius"
          :center="me"
          :options="circleOptions"
        />
      </div>
      <!-- 노비문서 위치 -->
      <div
        v-for="m in papers"
        :key="m.id"
        @click="ripPaper(m)"
      >
        <GMapMarker
          v-if="!m.ripped"
          :icon=paperMarkerImg
          :animation=1
          :position="m.position"
          @click="openInfoWindow(marker.id)"
          />
          <!-- :clickable="true" -->
      </div>
      <!-- 다른 플레이어 위치 -->
      <div
        v-for="m in others"
        :key="m.id"
        @click="ripPaper(m)"
      >
        <GMapMarker
          v-if="!m.ripped"
          :icon=othersMarkerImg
          :position="m.position"
          :clickable="true"
        />
      </div>
    </GMapMap>
</template>

<script>
// import { OpenVidu } from "openvidu-browser";
// const APPLICATION_SERVER_URL = process.env.RTC;

import truePaper from '@/assets/TruePaper.png'
import othersMarker from '@/assets/runner.png'
import randomLocation from 'random-location'

export default {
  name: 'MapView',
  props:{
    // papers: Object, // 노비 문서 정보
    // others: Object, // 다른 플레이어 위치 정보
  },
  data() {
    return {
      // 나랑 관련된 정보
      myMarker: true,
      me: {
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
      // 노비 문서 관련 정보
      papers: [], // props로
      paperMarkerImg: {
        url: truePaper,
        scaledSize: { width: 40, height: 40 }
      },
      // 다른 플레이어 관련 정보
      others: [], //props로
      othersMarkerImg: {
        url: othersMarker,
        scaledSize: { width: 40, height: 40 }
      },

      // 게임 반경 표시
      playgroundOptions: {
        strokeColor: "#FF0000",
        strokeOpacity: 0.3,
        strokeWeight: 5,
        fillColor: "#FFFFFF",
        fillOpacity: 0,
      },
      // roomInfo: {
      //   room_id: 1,
      //   title: "방이름1",
      //   is_public: true,
      //   password: null,
      //   lat: 36.119485,
      //   lng: 128.3445734,
      //   radius: 1000,
      //   host_id: "gogo",
      //   room_start_time: new Date(2023, 1, 1, 13, 20, 0)
      //   },
      roomInfo: {
        room_id: 1,
        title: "방이름1",
        is_public: true,
        password: null,
        lat: 36.103879,
        lng: 128.4187361,
        radius: 1000,
        host_id: "gogo",
        room_start_time: new Date(2023, 1, 1, 13, 20, 0)
      },
    };
  },
  methods: {                  
    // 노비 문서 생성 -> 백에서 받아오는 API로 수정하기
    // generatePapers(){
    //   console.log('2. generatePapers 함수 실행')

    //   for(let i = 0; i < 10; i++) {
    //     const randomPoint = randomLocation.randomCirclePoint({latitude: this.roomInfo.lat, longitude: this.roomInfo.lng}, this.roomInfo.radius*0.9)
    //     let real
    //     if(i < 5){
    //       real = true
    //     } else {
    //       real = false
    //     }
    //     this.papers.push({ 
    //       id: i,
    //       position: { lat: randomPoint.latitude, lng: randomPoint.longitude } ,
    //       real: real,
    //       ripped: false,
    //     })
    //   }
    //   console.log(this.papers)
    // },
    generatePlayer(){
      console.log('2. generatePapers 함수 실행')

      for(let i = 0; i < 10; i++) {
        const randomPoint = randomLocation.randomCirclePoint({latitude: this.roomInfo.lat, longitude: this.roomInfo.lng}, this.roomInfo.radius*0.9)
        let real
        if(i < 5){
          real = true
        } else {
          real = false
        }
        this.others.push({ 
          id: i,
          position: { lat: randomPoint.latitude, lng: randomPoint.longitude } ,
          real: real,
          ripped: false,
        })
      }
      console.log(this.papers)
    },

    // 노비문서 찢기
    ripPaper(marker){
      console.log('3. ripPaper 함수 실행')
      const distance = this.calculateDistance(marker)
      if(distance > this.catchRadius) {
        console.log('노비문서를 확인하시겠습니까?')
        alert('노비문서를 확인하시겠습니까?')
      }
    },
    // 범위 밖으로 나갈 시 경고
    outOfPlayground(location){
      console.log('4. outOfPlayground 함수 실행')
      if(this.calculateDistance(location) <= this.roomInfo.radius){
        console.log('범위밖으로 나왔습니다!! 플레이 범위 안으로 돌아가세요')
      } else{
        console.log('정상 범위 안 입니다.')
      }
    },
    // 내 위치
    myLocation() {
      console.log('1. myLocation 함수 실행')
      this.$getLocation({enableHighAccuracy: true})
      .then((coordinates) => {
        this.me.lat = coordinates.lat
        this.me.lng = coordinates.lng
        console.log(this.me.lat)
        console.log(this.me.lng)
        // 위치가 변할 때 마다 노비를 잡을 수 있는지, 노비문서를 찢을 수 있는지, 플레이 범위 안인지 확인
        // this.catch()
        // this.ripPaper()
        // const roomCenter = { position: {lat: this.roomInfo.lat, lng: this.roomInfo.lng} }
        // console.log('---------------------')
        // console.log(roomCenter)
        // this.outOfPlayground({ position: {lat: this.roomInfo.lat, lng: this.roomInfo.lng} })
        // 위치 공유
        // this.session.on("streamCreated", function (event) {
        //   this.session.subscribe(event.stream, "subscriber");
        //   // const USER_DATA = {}
        //   console.log("USER DATA: " + event.stream.connection.data);
        // });
      })
      .catch((error) => {
        console.log(error)
      })
    },
    // 자이로 센서 허용
    onGyro() {
      if (typeof DeviceMotionEvent.requestPermission === 'function') {
        // Handle iOS 13+ devices.
        alert('아이폰의 경우 동장 및 방향 사용에 허가가 필요합니다.')
        DeviceMotionEvent.requestPermission()
          .then((state) => {
            if (state === 'granted') {
              // window.addEventListener('devicemotion', handleOrientation);
            } else {
              console.error('Request to access the orientation was rejected');
            }
          })
          .catch(console.error);
      }
    },
    // 자이로 센서 인식
    handleOrientation(event) {
      const beta = event.beta
      console.log(beta)
      if(beta > 125 || beta < 55) {
        this.myMarker = true
      } else {
        this.myMarker = false
      }
      // console.log(window)
    },
    // 나와 marker의 거리 계산
    calculateDistance(marker){
      console.log('!!calculateDistance 함수 실행됨')
      console.log(marker)
      const lat1 = this.me.lat
      const lng1 = this.me.lng
      const lat2 = marker.position.lat
      const lng2 = marker.position.lng
      const r = 6371; //지구의 반지름(km)
      const dLat = (lat2-lat1) * (Math.PI/180)
      const dLon = (lng2-lng1) * (Math.PI/180)
      const a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(lat1 * (Math.PI/180)) * Math.cos(lat2 * (Math.PI/180)) * Math.sin(dLon/2) * Math.sin(dLon/2);
      const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
      const d = r * c; // Distance in km
      const distance = Math.round(d*1000);
      return distance
    },
    // 노비 잡기
    catch(){
      console.log('!! catch 함수 실행되기는 함')
      for(const marker of this.others){
        const distance = this.calculateDistance(marker)
        if(distance <= this.catchRadius){
          alert('잡으세요.')
          // console.log('잡을 수 있음')
          this.conn.send(JSON.stringify(
            {
              event:'catch',
              // nickname:(선택),
              room: this.roomInfo.id,
              startData: {
                others : marker,
              }
            }
          ));
        } else {
          console.log('잡을 수 없음')
        }
      }
    },
  },
  created() {
    // 자이로스코프 인식
    window.addEventListener('deviceorientation', this.handleOrientation)
    
    var varUA = navigator.userAgent.toLowerCase(); //userAgent 값 얻기
 
    if ( varUA.indexOf("iphone") > -1||varUA.indexOf("ipad") > -1||varUA.indexOf("ipod") > -1 ) {
        //IOS
        console.log('iOS')
        this.onGyro()
    }

    // 내 위치
    console.log('내 위치 가져오기')
    this.myLocation()
    // setInterval(this.myLocation(),1000)

    // 노비 문서 위치
    console.log('노비 문서 가져오기')
    const papers = sessionStorage.info.slavepaper
    for (let i = 0; i < papers.length; i++){
      console.log(i)
      this.papers.push({ 
            id: i,
            position: { lat: papers[i].lat, lng: papers[i].lng } ,
            real: papers[i].real,
            ripped: false,
          })
    }
    console.log(this.papers)
    // this.catch()


    // this.generatePlayer()

  },
  mounted() {
    this.ripPaper()
    this.outOfPlayground({position: {lat: this.roomInfo.lat, lng: this.roomInfo.lng}})
    // this.catch()
    setInterval(this.catch(), 3000)

  },
  computed: {
    catchRadius(){
      return this.roomInfo.radius * 0.1
    }
  },
  watch: {
    // player(){
    //   // 위치가 변할 때 마다 노비를 잡을 수 있는지, 노비문서를 찢을 수 있는지, 플레이 범위 안인지 확인
    //   this.catch()
    //   this.ripPaper()
    //   const roomCenter = { position: {lat: this.roomInfo.lat, lng: this.roomInfo.lng} }
    //   console.log(roomCenter)
    //   this.outOfPlayground(roomCenter)
    //   // 위치 공유
    //   this.session.on("streamCreated", function (event) {
    //     this.session.subscribe(event.stream, "subscriber");
    //     // const USER_DATA = {}
    //     console.log("USER DATA: " + event.stream.connection.data);
    //   });
    // }
  },
};
</script>

<style>
body {
  margin: 0;
}
</style>
