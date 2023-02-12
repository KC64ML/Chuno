<template>
  <div id="map_modal_container" style="z-index: 99">
    <CatchModal
      v-if="catchModal"
      :catchTarget="catchTarget"
      @on-no-catch="onNoCatch"
      @on-yes-catch="onYesCatch"
    />
    <RipModal
      v-if="ripModal"
      :ripTarget="ripTarget"
      @on-rip="onRip"
    />
    <GMapMap
      :center="location"
      :zoom="18"
      :options="{
        zoomControl: true,
        mapTypeControl: false,
        streetViewControl: false,
        fullscreenControl: false,
        minZoom: 11,
        maxZoom: 18,
      }"
      class="gmap"
    >
      <!-- 플레이 영역 표시 -->
      <GMapCircle
        :radius="roomInfo?.radius"
        :center="{lat: roomInfo?.lat, lng: roomInfo?.lng}"
        :options="playgroundOptions"
      />
      <!-- 내 위치 -->
      <div v-if="user?.role == 'runner'">
        <GMapMarker
          :icon=myRunnerMarkerImg
          :animation=4
          :position=this.location
        />
      </div>
      <div v-if="user?.role == 'chaser'">
        <GMapMarker
          :icon=myChaserMarkerImg
          :animation=4
          :position=this.location
        />
      </div>
      <GMapCircle
        :radius="catchRadius"
        :center="location"
        :options="circleOptions"
      />
      
      <!-- 노비문서 위치 -->
      <div v-if="user?.role == 'runner'">
        <div
          v-for="m in papers"
          :key="m.id"
          >
          <!-- @click="ripPaper(m)" -->
          <GMapMarker
          v-if="!m.ripped"
          :icon=paperMarkerImg
          :animation=1
          :position="m.location"
          @click="ripPaper(m)"
          />
          <!-- @click="openInfoWindow(marker.id)" -->
          <!-- :clickable="true" -->
        </div>
      </div>

      <!-- 다른 플레이어 위치 -->
      <div
        v-for="(o, key, idx) in others"
        :key="idx"
        @click="catchRunner(o)"
      >
        <!-- 내가 노비인데, -->
        <!-- 상대도 노비일 때 -->
        <div v-if="o.role == 'runner' && user.role == 'runner'">
          <GMapMarker
            :icon=otherRunnerMarkerImg
            :position="o.location"
            :clickable="true"
          />
        </div>
        <!-- 상대가 추노일 때 -->
        <div v-if="o.role == 'chaser' && user.role == 'runner'">
          <GMapMarker
            v-if="o.myMarker"
            :icon=otherChaserMarkerImg
            :position="o.location"
            :clickable="true"
          />
        </div>

        <!-- 내가 추노인데, -->
        <!-- 상대도 추노일 때 -->
        <div v-if="o.role == 'chaser' && user.role == 'chaser'">
          <GMapMarker
            :icon=otherChaserMarkerImg
            :position="o.location"
            :clickable="true"
          />
        </div>
        <!-- 상대가 노비일 때 -->
        <div v-if="o.role == 'runner' && user.role == 'chaser'">
          <GMapMarker
            v-if="o.myMarker"
            :icon=otherRunnerMarkerImg
            :position="o.location"
            :clickable="true"
          />
        </div>
      </div>
    </GMapMap>
  </div>
</template>

<script>
import truePaper from '@/assets/TruePaper.png'
import RunnerMarker from '@/assets/slave_img.png'
import ChaserMarker from '@/assets/chuno_img.png'
import MyRunnerMarker from '@/assets/slave_me_img.png'
import MyChaserMarker from '@/assets/chuno_me_img.png'
import CatchModal from './CatchModal.vue';
import RipModal from './RipModal.vue';

export default {
  name: 'MapView',
  props:{
    user: Object, // 내 정보
    roomInfo: {   // 방 정보
      type: Object,
      default() {
        return {
          lat: 0,
          lng: 0,
          radius: 0,
        }
      }
    },
  },
  components: {
    CatchModal,
    RipModal,
  },
  data() {
    return {
      // 나랑 관련된 정보
      myMarker: true,
      location: {
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
      myRunnerMarkerImg: {
        url: MyRunnerMarker,
        scaledSize: { width: 40, height: 40 }
      },
      myChaserMarkerImg: {
        url: MyChaserMarker,
        scaledSize: { width: 40, height: 40 }
      },
      // 노비 문서 관련 정보
      papers: [], // props로
      paperMarkerImg: {
        url: truePaper,
        scaledSize: { width: 40, height: 40 }
      },
      // 다른 플레이어 관련 정보
      others: {}, //props로
      otherRunnerMarkerImg: {
        url: RunnerMarker,
        scaledSize: { width: 40, height: 40 }
      },
      otherChaserMarkerImg: {
        url: ChaserMarker,
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
      
      // 노비 잡는 모달
      catchModal: false,
      // 노비 문서 찢는 모달
      ripModal: false,
      catchTrarget: {},
      locationInterval: null,
    };
  },
  beforeUnmount(to, from, next) {
    clearInterval(this.locationInterval);
    next();
  },
  methods: {                  
    init() {
      this.GyroAllow();
      // 노비 문서 위치
      console.log('노비 문서 가져오기')
      const info = JSON.parse(sessionStorage.info)
      const papers = info.slavepaper
      console.log("받아온 노비문서", papers);
      for (let i = 0; i < papers.length; i++){
        console.log(i)
        this.papers.push({ 
              id: i,
              location: { lat: papers[i].lat, lng: papers[i].lng } ,
              real: papers[i].real,
              ripped: false,
        })
      }
        console.log("노비문서 받는 중", this.papers);
      
    },
    erollEvent() {
      new Promise((resolve) => {
        // console.log("promise at MapView");
        this.conn.addEventListener('message', (e) => {
          // console.log("message received at MapView", e);
          const content = JSON.parse(e.data);
          if (content.type == "othersLocation") {
            const other = content.info; // startData가 여기 담겨잇다.
            // console.log('내 닉네임!!!!!!!!')
            // console.log(this.user.nickname)
            if (other.nickname == this.user.nickname) {
              return;
            }
            this.others[other.nickname] = {
              nickname: other.nickname,
              role: other.role,
              location: other.location,
              myMarker: other.myMarker,
              caught: other.caught,
            };
            console.log("ohters 받아오는 중 : ", this.others);
            // if(this.user.role == 'chaser') {
            //   this.catchRunner(other);
            // }
            
          } else if (content.type == "caughtRunner") {
              const content = JSON.parse(e.data);
              const target = content.info.others;
              if(target.nickname == this.user.nickname) { // 내가 잡혔을 때 내 상태 업뎃
                this.$emit('on-caught')
                console.log('나(' + target.nickname + ')는 잡혀버렸다...')
              } else {  // 다른 사람이 잡혔을 때 다른 사람 상태 업뎃
                console.log(target.nickname + '님이' + content.nickname + '님한테 잡혔다...')
                this.others[target.nickname].caught = true
              }
            } else if (content.type == "rippedPaper") {
              const content = JSON.parse(e.data);
              console.log('여기서 오류 확인하자!!!!!!!!!!!!!!')
              // console.log(content)
              const paper = content.info.paper
              console.log('아까 오류 났던 부분임2' + paper)
              console.log(content.nickname + '이(가) 확인한' + paper.id+ '번째 노비문서 상태를 업뎃하자')
              // console.log(paper)
              const target = this.papers[paper.id -1]
              console.log(target)
              target.ripped = true
              console.log('밑에 찍히는 대로 업뎃했다.')
              console.log(target)
          }
        }
        )
        resolve();
      }).then(() => {
        this.myLocationInterval();
      })
    },
    myLocationInterval() {
      // 내 위치
      this.locationInterval = setInterval(() => {this.myLocation()}, 1000);
    },

    // 노비문서 찢기 모달
    ripPaper(marker){
      if(this.user.role == 'chaser'){
        return
      }
      console.log('3. ripPaper 함수 실행')
      console.log(marker)
      const distance = this.calculateDistance(marker)
      if(distance < this.catchRadius) {
        console.log('해당 노비문서를 잡을 수 있습니다.')
        this.ripTarget = marker
        this.ripModal = true
      } else {
        console.log('해당 노비문서가 너무 멀리 있습니다.')
      }
    },
    // 노비문서를 확인하고 나서
    onRip(ripTarget){
      console.log('얘랑')
      console.log(ripTarget)
      const target = this.papers[ripTarget.id]
      target.ripped = true
      console.log('얘랑')
      console.log(target)
      console.log('같아야함')

      this.conn.send(JSON.stringify(
          {
            event:"ripPaper",
            nickname: this.user.nickname,
            room: this.roomInfo.id,
            startData: {
              paper: target,
            }
          }
        ));
      this.conn.send(JSON.stringify(
        {
          event: "chat",
          room: this.roomInfo.id,
          nickname: 'system',
          level: 1,
          msg: `${this.user.nickname}이 노비문서를 찢었습니다.`
        }
      ))
      this.ripModal = false
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
      this.$getLocation({enableHighAccuracy: true})
      .then((coordinates) => {
        this.location.lat = coordinates.lat
        this.location.lng = coordinates.lng
        // console.log(this.location.lat)
        // console.log(this.location.lng)
        // 내가 노비이면
        // if(this.user.role == 'runner') {
        //   this.ripPaper()
        // }
        // 위치 공유
        this.conn.send(JSON.stringify(
          {
            event:"playerLocation",
            nickname: this.user.nickname,
            room: this.roomInfo.id,
            startData: {
              nickname: this.user.nickname,
              role: this.user.role,
              location: this.location,
              myMarker: this.myMarker,
              caught: this.user.caught,
            }
          }
        ));
      })
      .catch((error) => {
        console.log(error)
      })
    },
    GyroAllow() {
      // 자이로스코프 인식
      window.addEventListener('deviceorientation', this.handleOrientation)
      
      var varUA = navigator.userAgent.toLowerCase(); //userAgent 값 얻기

      if ( varUA.indexOf("iphone") > -1||varUA.indexOf("ipad") > -1||varUA.indexOf("ipod") > -1 ) {
          //IOS
          console.log('iOS')
          this.onGyro()
      }
    },
    // 자이로 센서 허용
    onGyro() {
      if (typeof DeviceMotionEvent.requestPermission === 'function') {
        // Handle iOS 13+ devices.
        alert('아이폰의 경우 동작 및 방향 사용에 허가가 필요합니다.')
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
    },

    // 나와 marker의 거리 계산
    calculateDistance(marker){
      console.log('!!calculateDistance 함수 실행됨')
      console.log(marker)
      const lat1 = this.location.lat
      const lng1 = this.location.lng
      const lat2 = marker['location'].lat
      const lng2 = marker['location'].lng
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
    catchRunner(marker){
      console.log('!! catchRunner 함수 실행되기는 함')
      console.log('--------------MARKER-----------------')
      console.log(marker)
      const distance = this.calculateDistance(marker)
      console.log('--------------DISTANCE-----------------')
      console.log(distance)
      if(this.user.role == 'chaser' && marker.role == 'runner' && marker.caught == false && distance <= this.catchRadius){
        console.log('잡을 수 있음' + marker)
        this.catchModal = true
        this.catchTarget = marker
        
      } else {
        console.log('잡을 수 없음')
      }
    },
    // 노비 잡지 않기
    onNoCatch(){
      this.catchModal = false
      console.log('노비 안잡을래..')
    },
    // 노비 잡기
    onYesCatch(target){
      console.log('노비 잡을래!!!')
      console.log('--------------TARGET-----------------')
      console.log(target)
      this.catchModal = false
      target.caught = true
      this.conn.send(JSON.stringify(
          {
            event:'catchRunner',
            nickname: this.user.nickname,
            room: this.roomInfo.id,
            startData: {
              others : target,
            }
          }
      ))
      this.conn.send(JSON.stringify(
        {
          event: 'chat',
          room: this.roomInfo.id,
          nickname: 'system',
          level: 1,
          msg: `${this.user.nickname}이 ${target.nickname}을 잡았습니다.`
        }
      ))
    },
  },
  created() {
    console.log("MapView created start");
    this.init();
    setTimeout(() => {
      this.erollEvent();
    }, 5000);
  },
  mounted() {
    this.outOfPlayground({location: {lat: this.roomInfo.lat, lng: this.roomInfo.lng}})

  },
  computed: {
    catchRadius(){
      return this.roomInfo.radius * 0.1
    }
  },
  watch: {
    roomInfo() {
      console.log("roomInfo at MapView");
      console.log(this.roomInfo);
    }
  },
};
</script>

<style lang="scss" scoped>
@import "@/assets/scss/variable.scss";
  #map_modal_container {
    position: absolute;
    bottom: $footer-height;
    top: $video-height;
    width: 100vw;
  }
  #map_modal_container > div {
    height: 100%
  }
  .gmap {
    position: relative;
    width: 100%;
    height: 100%;
  }
</style>
