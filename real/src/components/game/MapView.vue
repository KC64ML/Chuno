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
          :position=this.player
        />
        <GMapCircle
          :radius="catchRadius"
          :center="player"
          :options="circleOptions"
        />
      </div>
      <!-- 노비문서 위치 -->
      <div
        v-for="m in markers"
        :key="m.id"
        @click="ripPaper(m)"
      >
        <GMapMarker
          v-if="!m.ripped"
          :icon=otherMarkerImg
          :animation=1
          :position="m.position"
          :clickable="true"
        />

      </div>
    </GMapMap>
</template>

<script>
import { OpenVidu } from "openvidu-browser";
const APPLICATION_SERVER_URL = process.env.RTC;

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

      
      // OpenVidu objects
      OV: undefined,
      session: undefined,
      mainStreamManager: undefined,
      myStreamManager: undefined,
      publisher: undefined,
      subscribers: [],
      enemy_name: undefined,

      // Join form
      mySessionId: this.$route.params.roomId,
      myUserName: "dddd",
    };
  },
  methods: {
    // Web RTC
    async init() {
      this.OV = new OpenVidu();
      this.session = this.OV.initSession();
      this.session.on("streamCreated", ({ stream }) => {
          const subscriber = this.session.subscribe(stream);
          this.subscribers.push(subscriber);
          console.log("스트림을 발견했어요!")
          console.log("현재 사람은 " + this.subscribers.length + "명이에요")
          this.mainStreamManager = subscriber;
          this.mainStreamManager.addVideoElement(this.$refs.video);
          const { connection } = this.mainStreamManager.stream;
          console.log("커넥션 데이터에요:", connection.data)
          const { clientData } = JSON.parse(connection.data);
          this.enemy_name = clientData;
          
      });
      this.session.on("streamDestroyed", ({ stream }) => {
          const index = this.subscribers.indexOf(stream.streamManager, 0);
          if (index >= 0) {
              this.subscribers.splice(index, 1);
          }
          console.log("누군가가 스트림을 종료했어요!")
          console.log("남은 사람은 " + this.subscribers.length + "명이에요")
      });
      this.session.on("exception", ({ exception }) => {
          console.warn("오류ㅠㅠ" + exception);
      });
      await this.getToken(this.mySessionId).then(async (token) => {
        console.log("토큰을생성해요:" + token);
        await this.session.connect(token, { clientData: this.myUserName, role: "good" })
          .then(() => {
            let publisher = this.OV.initPublisher(undefined, {
                audioSource: undefined, // The source of audio. If undefined default microphone
                videoSource: undefined, // The source of video. If undefined default webcam
                publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
                publishVideo: true, // Whether you want to start publishing with your video enabled or not
                resolution: "160x120", // The resolution of your video
                frameRate: 30, // The frame rate of your video
                insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
                mirror: false, // Whether to mirror your local video or not
            });
            this.myStreamManager = publisher;
            this.publisher = publisher;
            this.session.publish(this.publisher);
            this.myStreamManager.addVideoElement(this.$refs.my_video);
          })
          .catch((error) => {
            console.log("There was an error connecting to the session:", error.code, error.message);
          });
      });
      window.addEventListener("beforeunload", this.leaveSession);
  },
  leaveSession() {
      if (this.session) this.session.disconnect();

      this.session = undefined;
      this.mainStreamManager = undefined;
      this.publisher = undefined;
      this.subscribers = [];
      this.OV = undefined;

      window.removeEventListener("beforeunload", this.leaveSession);
  },
  
  async getToken(mySessionId) {
      console.log("getToken 시작")
      const sessionId = await this.createSession(mySessionId);
      return await this.createToken(sessionId);
  },
  async createSession(sessionId) {
      console.log("createSeesion 시작")
      const response = await this.axios.post(APPLICATION_SERVER_URL + 'api/sessions', { customSessionId: sessionId }, {
          headers: { 'Content-Type': 'application/json', },
      });
      return response.data; // The sessionId
  },
  async createToken(sessionId) {
      console.log("createToken 시작")
      const response = await this.axios.post(APPLICATION_SERVER_URL + 'api/sessions/' + sessionId + '/connections', {}, {
          headers: { 'Content-Type': 'application/json', },
      });
      return response.data; // The token
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
                            
    // 노비 문서 생성
    generatePapers(){
      for(let i = 0; i < 10; i++) {
        const randomPoint = randomLocation.randomCirclePoint({latitude: this.roomInfo.lat, longitude: this.roomInfo.lng}, this.roomInfo.radius*0.9)
        let real
        if(i < 5){
          real = true
        } else {
          real = false
        }
        this.markers.push({ 
          id: i,
          position: { lat: randomPoint.latitude, lng: randomPoint.longitude } ,
          real,
          ripped: false,
        })
      }
      console.log(this.markers)
    },
    // 노비문서 찢기
    ripPaper(marker){
      const distance = this.calculateDistance(marker)
      if(distance > this.catchRadius) {
        console.log('노비문서를 확인하시겠습니까?')
      }
    },
    // 범위 밖으로 나갈 시 경고
    outOfPlayground(location){
      if(this.calculateDistance(location) <= this.roomInfo.radius){
        console.log('범위밖으로 나왔습니다!! 플레이 범위 안으로 돌아가세요')
      }
    },
    // 내 위치
    myLocation() {
      this.$watchLocation({enableHighAccuracy: true})
      .then((coordinates) => {
        this.player.lat = coordinates.lat
        this.player.lng = coordinates.lng
        console.log(this.player.lat)
        console.log(this.player.lng)
        // 위치가 변할 때 마다 노비를 잡을 수 있는지, 노비문서를 찢을 수 있는지, 플레이 범위 안인지 확인
        this.catch()
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
    calculateDistance(marker){
      console.log(marker)
      const lat1 = this.player.lat
      const lng1 = this.player.lng
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
      for(const marker of this.markers){
        const distance = this.calculateDistance(marker)
        if(distance <= this.catchRadius){
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
  mounted() {
    this.ripPaper()
    // this.catch()
    this.outOfPlayground({ position: {lat: this.roomInfo.lat, lng: this.roomInfo.lng} })

  },
  computed: {
    catchRadius(){
      return this.roomInfo.radius * 0.1
    }
  },
  // watch: {
  //   player(){
  //     // 위치가 변할 때 마다 노비를 잡을 수 있는지, 노비문서를 찢을 수 있는지, 플레이 범위 안인지 확인
  //     this.catch()
  //     this.ripPaper()
  //     const roomCenter = { position: {lat: this.roomInfo.lat, lng: this.roomInfo.lng} }
  //     console.log(roomCenter)
  //     this.outOfPlayground(roomCenter)
  //     // 위치 공유
  //     this.session.on("streamCreated", function (event) {
  //       this.session.subscribe(event.stream, "subscriber");
  //       // const USER_DATA = {}
  //       console.log("USER DATA: " + event.stream.connection.data);
  //     });
  //   }
  // },
};
</script>

<style>
body {
  margin: 0;
}
</style>
