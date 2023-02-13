<template>
	<!-- <LeaveModal
		v-if="leaveModal"
		@on-no-leave="onNoLeave"
		@on-yes-leave="onYesLeave"
	/> -->

	<div v-if="leaveModal" id="item_description_modal" @click="close_item_description_modal">
		<div id="item_description_modal_container" @click="stopingPropagation">
			<div style="text-align: center; font-size: 25px; margin-bottom: 10px;">정말로 나가시겠소?</div>
			<div style="display: flex; justify-content: space-around;">
				<div class="flex_center"
					@click="onYesLeave">
					<img src="@/assets/main_button1.png" class="modal_confirm_button">
					<div class="image_text">네</div>
				</div>
				<div class="flex_center" @click="onNoLeave">
					<img src="@/assets/main_button1.png" class="modal_confirm_button">
					<div class="image_text">아니요</div>
				</div>
			</div>
		</div>
	</div>

	<transition name="menu-retreat">
		<div id="item_menu_modal" v-if="item_menu_modal">
			<div v-if="this.user">
				<div style="margin-bottom: 10px; text-align: center;">아이템</div>
				<div v-for="(e, idx) in item_list[this.user.role]" :key="idx"
					style="display: flex; align-items: center;" @click="item_select(e)">
					<img :src="e.path" alt="" style="margin-right: 5px;">
					<div>{{ e.name }}</div>
				</div>
			</div>
			<br>
			<div v-if="this.user">
				<div style="margin-bottom: 10px; text-align: center;">메뉴</div>
				<div 
					@click="onLeave"
					style="display: flex; align-items: center;"
				>
					<img src="@/assets/leave.png" alt="" style="margin-right: 5px;">
					<div style="color:#A03A2C;">나가기</div>
				</div>
			</div>
		</div>
	</transition>

	<div v-if="item_description_modal" id="item_description_modal" @click="close_item_description_modal">
		<div id="item_description_modal_container" @click="stopingPropagation">
			<div style="text-align: center; font-size:25px; margin-bottom: 10px;">{{ selected_item.name }}</div>
			<div style="text-align: center; margin-bottom: 20px;">{{ selected_item.description }}</div>
			<div style="text-align: center; font-size: 25px; margin-bottom: 10px;">사용하실래요?</div>
			<div style="display: flex; justify-content: space-around;">
				<div class="flex_center"
					@click="[stopingPropagation, item_confirm_yes({ id: selected_item.id, is_implemented: selected_item.is_implemented })]">
					<img src="@/assets/main_button1.png" class="modal_confirm_button">
					<div class="image_text">네</div>
				</div>
				<div class="flex_center" @click="close_item_description_modal">
					<img src="@/assets/main_button1.png" class="modal_confirm_button">
					<div class="image_text">아니요</div>
				</div>
			</div>
		</div>
	</div>

  <div id="game_end_modal_container" v-if="game_end">
    <div id="game_end_modal">
      <div style="text-align:center; font-size: 20px; ">{{ game_end_display }}</div>
      <div style="text-align:center; font-size: 15px; word-break: break-all; width: 90%;">{{ game_end_display_sub }}</div>
      <div class="flex_center" @click="close_item_description_modal">
        <img src="@/assets/main_button1.png" class="modal_confirm_button">
        <div class="image_text" @click="end_confirm">확인</div>
      </div>
    </div>
  </div>

	<OpenViduVue
    :my_cam_modal="my_cam_modal"
    :user="user"></OpenViduVue>

	<transition name="chat-retreat">
		<div id="chat_container" style="padding: 20px 0;" v-if="chat_modal">
			<div id="chat_header"
				style="display: flex; align-items:center; justify-content: space-between; margin-bottom: 10px;">
				<div>
					<img src="@/assets/main_logo2.png" alt="" style="width: 50px; height: 50px; margin-left: 20px;">
				</div>
				<div style="font-size: 30px">
					전언
				</div>
				<div style="font-size: 25px; margin-right: 20px;" @click="close_chat_modal">X</div>
			</div>
			<div v-for="(e, idx) in chat_log" :key="idx">
				<ChatCardVue :chat_dto="e" :my_nickname="user.nickname"></ChatCardVue>
			</div>
		</div>
	</transition>

	<div id="status_bar" style="display: flex; justify-content: space-between">
		<div>남은 시간 : {{ game_timer_minute }}분 {{ game_timer_second }}초</div>
		<div @click="status_open" class="for_trans" :class="{'trans':status_specific_modal}">▼</div>
	</div>
  <transition name="close_specific">
    <div id="status_specific" v-if="status_specific_modal">
      <div>
        <div>잡은 노비 수 : {{ arrested_slave }}명 / {{ total_slave }}명</div>
        <div>찾은 노비 문서 수 : {{ ripped_paper }}장 / {{ total_paper }}장</div>
      </div>
    </div>
  </transition>

	<MapView :user="user" :roomInfo="roomInfo" @on-caught="onCaught" :item_used="item_used[7]" @myArrestSlave="myArrestSlave" @myRippedPaper="myRippedPaper"/>
	<div style="position: absolute; bottom: 0; left: 0;">

		<div id="footer_container">
			<div class="menu_box flex_center" @click="open_chat_modal">
				<img class="menu" src="@/assets/game_chat.png">
			</div>
			<div>
				<input class="map_search" type="text" placeholder="채팅을 입력해 주세요" v-model="chat_data">
			</div>
			<div class="menu_box" @click="transmit_chat">
				<img src="@/assets/paper_plane.svg" alt="">
			</div>
			<div class="menu_box" @click="myCam">
				<img class="menu" src="@/assets/game_myCam.png">
			</div>
			<div class="menu_box" @click="onMenu">
				<img class="menu" src="@/assets/game_menu.png">
			</div>
		</div>
	</div>
	<transition name="toasting">
		<div class="toast_chat" v-if="system_toast">
			<div class="flex_center">
				<img src="@/assets/system_chat.png" style="height: 60px; width: 90vw;">
				<div class="image_text" style="word-break:break-all;">{{ last_chat }}</div>
			</div>
		</div>
	</transition>
	<transition name="toasting">
		<div class="toast_chat" v-if="chat_toast">
			<div class="flex_center">
				<div
					style="font-size: 20px; word-break:break-all; padding: 10px 20px; border-radius: 10px; background-color: rgb(0,0,0,0.6); color: white; width: 90vw;">
					{{ last_chat }}</div>
			</div>
		</div>
	</transition>
	<SpiningModalVue @spinningEnd="spinningEnd" v-if="spinningModal"></SpiningModalVue>

	<transition name="modal-fading">
		<RoleModalVue v-if="roleModal" @modalAllClose="modalAllClose"></RoleModalVue>
	</transition>
</template>

<script>
import OpenViduVue from '@/components/game/OpenViduVue.vue';
import MapView from '@/components/game/MapView.vue'; // huh
import RoleModalVue from '@/components/game/RoleModalVue.vue';
import ChatCardVue from '@/components/game/ChatCardVue.vue';
// import LeaveModal from '@/components/game/LeaveModal.vue';
const APPLICATION_SERVER_URL = process.env.VUE_APP_RTC;

import SpiningModalVue from '@/components/game/SpiningModalVue.vue'

export default {

  name: 'GameView',
  components: {
    MapView,
    OpenViduVue,
    SpiningModalVue,
    RoleModalVue,
    ChatCardVue,
	// LeaveModal,
  },
  async created() {
    this.conn.addEventListener('message', (e)  => {
      var content = JSON.parse(e.data);
      if (content.type == 'chat') {
        console.log({ nickname: content.nickname, msg: content.message })
        this.chat_log.push({ nickname: content.nickname, msg: content.message })

        // 최근 메세지를 토스트로 띄우고 싶어요!!!
        if (content.nickname == "system") {
					this.last_chat = content.message;
					this.system_toast = true;
				} else {
					this.last_chat = content.nickname + " : " + content.message;
					this.chat_toast = true;
				}

				setTimeout(() => {
					this.system_toast = false;
					this.chat_toast = false;
				}, 3000)
      } else if (content.type == 'caughtRunner') {
        this.arrested_slave++;
        if (this.arrested_slave == this.total_slave) {
          this.victory_team = 'chaser';
          this.victoryCnt(this.user.role, this.victory_team);
          this.makeDisplay(this.victory_team);
          this.game_ending();
        }
      } else if (content.type == 'rippedPaper') {
        if(content.info.paper.real == true) {
          this.ripped_paper++;
          if (this.ripped_paper == this.total_paper) {
            this.victory_team = 'runner';
            this.victoryCnt(this.user.role, this.victory_team);
            this.makeDisplay(this.victory_team);
            this.game_ending();
          }
        }
      }
    })
    await this.axios.get(APPLICATION_SERVER_URL + 'user',
          {
              headers: { Authorization: sessionStorage.token }
          }).then(({ data }) => {
              if (data.code) {
                  this.user = data.result;
              }
          });
    await this.axios.get(APPLICATION_SERVER_URL + 'room/' + this.$route.params.roomId)
      .then(({ data }) => {
        if (data.code) {
          this.roomInfo = data.result;
          console.log("GameView room info loaded");
        }
      })
    const info = JSON.parse(sessionStorage.info);
    this.user.role = this.getMyRole(info.teamslave, info.teamchuno, this.user.nickname);
    this.total_paper = info.teamslave.length;
    this.total_slave = info.teamslave.length;
    this.player_len = info.teamchuno.length + info.teamslave.length;
    this.user.caught = false;
    console.log("GameView created complete");
    console.log("-----------------------")
    console.log(this.user);
    console.log(this.roomInfo);

    /* 게임 시간 카운트 로직 */
    this.game_intervaling();
  },
  data() {
    return {
      my_cam_modal: { active: false },
      item_menu_modal: false,
      user: undefined,
      usedItem: [],
      // 개발이 끝나면 true로 고쳐줘요
      spinningModal: true,
      roleModal: false,
      roomInfo: undefined,

      system_toast: false,
			chat_toast: false,
			last_chat: "",
      chat_modal: false,
      chat_data: "",
      chat_log:[],

      // status-bar를 위한 변수에요
      status_specific_modal: true,

      // 노비문서 셔플을 위한 변수에요
      player_len: 0,

      // 게임 종료를 위한 변수에요
      game_end: false,
      game_end_display: "",
      game_end_display_sub: "",
      message_victory: "우리의 승리다!^^",
      message_lose: "우리의 패배다ㅜㅜ",
      line_zip: [
        "난 말이다.. 다 싫구나.. 네가 추운게 싫고 아픈게 싫고 네가 힘든게 싫구나..",
        "어쩌면.. 우리 모두는 우리가 살고 있는 바로 지금이 가장 암울한 시대라고 생각하고 살고 있는지도 모르겠다",
        "잊어라.. 기억이 많으면, 슬픔도 많은 법이다.",
        "누가 재미있어서 사나.. 다들 내일이 재밌을 줄 알고 사는거지..",
        "우리 세상이 오는 것이 아니라, 만드는 거라 하셨네",
        "인간의 눈이란 간사해서, 간혹 보고싶은 대로 봐버리기도 하지..",
        "살길이 있는데, 죽을 생각 부터 해서 쓰나.. 죽은 정승이 산 정승만 못한 법이야.",
        "내 비록 가진게 없어 '번듯하게'는 못살겠지만, '반듯하게'는 살걸세",
      ],
      victory_team: "",
      game_interval: null,
      game_timer: 1800,
      total_slave: 0,
      arrested_slave: 0,
      my_arrest_slave: 0,
      total_paper: 0,
      ripped_paper: 0,
      my_ripped_paper: 0,
      chaserWin: 0,
      runnerWin: 0,

      item_description_modal: false,
      selected_item: {},//
      item_list: {
        "runner": [
          {
            id: 1, name: "천리안", path: require('@/assets/item1.png'), is_implemented: false,
            description: "자신의 위치를 드러내지 않고 가장 가까운 추노꾼의 위치를 확인할 수 있다."
          },
          {
            id: 2, name: "위장", path: require('@/assets/item2.png'), is_implemented: false,
            description: "추노꾼이 자신을 잡을 수 있는 범위를 축소한다."
          },
          {
            id: 3, name: "확실한 정보통", path: require('@/assets/item3.png'), is_implemented: false,
            description: "진짜 노비문서의 위치를 확인할 수 있다."
          },
          {
            id: 4, name: "먹물탄", path: require('@/assets/item4.png'), is_implemented: true,
            description: "먹물을 뿌려 내 화면을 가릴 수 있다.",
          },
        ],
        "chaser": [
        {
            id: 5, name: "조명탄", path: require('@/assets/item5.png'), is_implemented: false,
            description: "30초간 노비의 위치를 지도에 표시할 수 있다."
          },
          {
            id: 6, name: "긴 오랏줄", path: require('@/assets/item6.png'), is_implemented: false,
            description: "자신이 노비를 잡을 수 있는 범위를 확대할 수 있다."
          },
          {
            id: 7, name: "거짓 정보통", path: require('@/assets/item7.png'), is_implemented: true,
            description: "노비 문서의 위치를 셔플할 수 있다."
          },
          {
            id: 8, name: "연막탄", path: require('@/assets/item8.png'), is_implemented: false,
            description: "연기를 피워 내 화면을 가릴 수 있다.",
          },
        ]
      },
      item_used: [0, 0, 0, 0, 0, 0, 0, 0, 0,],
    }
  },
  computed: {
    game_timer_minute() {
      return parseInt(this.game_timer / 60)
    },
    game_timer_second() {
      return this.game_timer % 60;
    }
  },
  methods: {
    game_intervaling() {
      this.game_interval = setInterval(() => {
        this.game_timer--;
        if (this.game_timer == 0) {
          if (this.ripped_paper > this.arrested_slave) {
            this.victory_team = 'runner';
            this.victoryCnt(this.user.role, this.victory_team);
            this.makeDisplay(this.victory_team);
            this.game_ending();
          } else if (this.ripped_paper < this.arrested_slave) {
            this.victory_team = 'chaser';
            this.victoryCnt(this.user.role, this.victory_team);
            this.makeDisplay(this.victory_team);
            this.game_ending();
          } else {
            this.victory_team = 'none';
            this.victoryCnt(this.user.role, this.victory_team);
            this.makeDisplay(this.victory_team);
            this.game_ending();
          }
        }
      }, 1000);
    },
    game_ending() {
      console.log("게임엔딩에 왓어요!")
      clearInterval(this.game_interval);
      this.game_end = true;
    },
	onYesLeave(){
		console.log('진짜로 나간다!!!!!')
		this.leaveModal = false
		this.$router.push({ name: 'home' })
	},
	onNoLeave(){
		console.log('안나갈건데')
		this.leaveModal = false
	},
	onLeave(){
		this.leaveModal = true
		console.log(this.leaveModal)
	},
    stopingPropagation(e) {
      e.stopPropagation();
    },
    onMenu() {
      this.item_menu_modal = !this.item_menu_modal
    },
    getMyRole(teamslave, teamchuno, nickname) {
      for (let i = 0; i < teamslave.length; i++) {
        if (teamslave[i].nickname == nickname) {
          return "runner";
        }
      }
      for (let i = 0; i < teamchuno.length; i++) {
        if (teamchuno[i].nickname == nickname) {
          return "chaser";
        }
      }
    },
    myCam() {
      this.my_cam_modal.active = !this.my_cam_modal.active
      console.log("mycam 눌림");
    },
    onCaught(){
      this.user.caught = true
    },
    spinningEnd() {
      this.spinningModal = false;
      this.roleModal = true;
    },
    modalAllClose() {
      this.roleModal = false;
    },

    open_chat_modal() {
      this.chat_modal = !this.chat_modal;
    },
    close_chat_modal() {
      this.chat_modal = false;
    },
    transmit_chat() {
      this.conn.send(JSON.stringify({
        "event": "chat",
        "room": this.roomInfo.id,
        "nickname": this.user.nickname,
        "level": this.user.level,
        "msg": this.chat_data,
      }))
      this.chat_data = "";
    },
    clearChatData() {
      this.chat_data = "";
    },

		item_select(e) {
			this.selected_item = e;
			this.item_description_modal = true;
		},
		close_item_description_modal() {
			this.selected_item = {};
			this.item_description_modal = false;
			this.item_menu_modal = false;
		},
		async item_confirm_yes(item) {
			if (item.id == 1) {
				// 천리안: 가장 가까운 추노꾼 위치 표시
				console.log(1)
			} else if (item.id == 2) {
				// 위장: 추노꾼의 catch 범위 축소
				console.log(2)
			} else if (item.id == 3) {
				// 확실한 정보통: 진짜 노비 문서 위치 표시
				console.log(3)
				this.visibility = true
				setTimeout(this.visibility = false, 30000)
			} else if (item.id == 4) {
				// 먹물탄
				console.log("먹물탄 사용 : ", 4);
				this.conn.send(JSON.stringify(
					{
						event: "useItem",
						level: 4,
						nickname: this.user.nickname,
						room: this.roomInfo.id,
						startData: {
							"isStart": 1,
						}
					}
				));
				// 5초 후에 제거
        setTimeout(() => {
          this.conn.send(JSON.stringify(
            {
              event: "useItem",
              level: 4,
              nickname: this.user.nickname,
              room: this.roomInfo.id,
              startData: {
                "isStart": 0,
              }
            }
          )
          )
        }, 60000 * 5);
				this.close_item_description_modal();
			} else if (item.id == 7) {
				this.item_used[7]++;
				this.close_item_description_modal();
			}
		},
		status_open() {
			this.status_specific_modal = !this.status_specific_modal;
      console.log(this.status_specific_modal)
		},
    myArrestSlave() {
      this.my_arrest_slave++;
    },
    myRippedPaper() {
      this.my_ripped_paper++;
    },
    async end_confirm() {
      var result = await this.axios.put(process.env.VUE_APP_SPRING + "room/endRoom", {
        "catchCount": this.my_arrest_slave,
        "paperCount": this.my_ripped_paper,
        "exp": 0,
        "money": 0,
        "chaserWin": this.chaserWin,
        "chaserCnt": this.my_arrest_slave,
        "runnerWin": this.runnerWin,
        "runnerCnt": this.my_ripped_paper
      }, {headers: {Authorization: sessionStorage.getItem("token")}}).then(res => res.data);
      alert(result.successOrFailure);
      this.$router.push({name: "gameResult", params: {role: this.user.role}});
    },
    makeDisplay(e) {
      console.log("메이크디스클레이에 왔어요")
      if (e == 'none') {
        this.game_end_display = "비겼어요..."
        this.game_end_display_sub = "인생엔 승리도 패배도 없는 것인가 봐요"
      } else if (this.user.role == e) {
        this.game_end_display = this.message_victory;
        this.game_end_display_sub = this.line_zip[Math.floor(Math.random() * this.line_zip.length)];
      } else {
        this.game_end_display = this.message_lose;
        this.game_end_display_sub = this.line_zip[Math.floor(Math.random() * this.line_zip.length)];
      }
    },
    victoryCnt(a, b) {
      if (a == b) {
        if (a == 'chaser') this.chaserWin = 1;
        else if (a == 'runner') this.runnerWin = 1;
      }
    }
	}
}

</script>

<style lang="scss" scoped>
@import "@/assets/scss/variable.scss";
$button_width: 50px;
$item_modal_confirm_button_height: 60px;
$status_height: 30px;

#footer_container {
	z-index: 1012;
	position: absolute;
	bottom: 0;
	background-color: black;
	height: $footer_height;
	width: 100vw;
	display: grid;
	grid-template-columns: $button_width 1fr $button_width $button_width $button_width;
	justify-content: space-around;
	align-items: center;
}

#footer_container>div {
	width: 100%;
}

.menu-window {
	float: right;
	position: absolute;
	bottom: 60px;
}

.menu_box {
	width: 20%;
	display: flex;
	flex-direction: column;
	align-items: center;
}

.map_search {
	display: block;
	margin: 0 auto;
	width: 95%;
	height: 40px;
	background-color: #F1F1F180;
	color: white;
	padding: 0 10px;
	border-radius: 15px;
	font-size: 20px;
}

.map_search::placeholder {
	color: rgba(255, 255, 255, 0.56)
}

#chat_container {
	z-index: 1011;
	position: absolute;
	bottom: $footer-height;
	background-image: url("@/assets/main_back_horizon.png");
	background-size: cover;
	width: 100vw;
	max-height: 60%;
	min-height: 200px;
	overflow-y: scroll;
	animation-name: chat_container;
	animation-duration: 0.4s;
	animation-iteration-count: 1;
}

@keyframes chat_container {
	0% {
		opacity: 0;
		transform: translateY(100%);
	}
}


#item_menu_modal {
	padding: 10px 20px;
	border-radius: 20px;
	background-color: rgb(0, 0, 0, 0.3);
	color: white;
	position: absolute;
	bottom: $footer-height + 10px;
	right: 10px;
	animation-name: item_menu_modal;
	animation-duration: 0.5s;
	animation-iteration-count: 1;
	box-shadow: 0 5px 5px rgb(0, 0, 0, 0.3);
	z-index: 100;
}

@keyframes item_menu_modal {
	0% {
		opacity: 0;
		transform: translateY(100%)
	}
}

#item_description_modal {
	z-index: 100010;
	background-color: rgb(0, 0, 0, 0.5);
	position: absolute;
	top: 0;
	left: 0;
	width: 100vw;
	height: 100%;
}

#item_description_modal_container {
	background-color: #F2F2F2;
	width: 100vw * 0.85;
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	border-radius: 20px;
	padding: 20px;
	word-break: break-all;
	animation-name: item_description_modal_container;
	animation-duration: 0.5s;
	animation-iteration-count: 1;
}

@keyframes item_description_modal_container {
	0% {
		transform: translate(-50%, -50%) scale(0);
	}

	70% {
		transform: translate(-50%, -50%) scale(1.3);
	}

	85% {
		transform: translate(-50%, -50%) scale(0.8);
	}

	95% {
		transform: translate(-50%, -50%) scale(1.1);
	}
}

.modal_confirm_button {
	height: $item_modal_confirm_button_height;
}

.toast_chat {
	z-index: 100100;
	position: absolute;
	bottom: $footer-height + 40px;
	animation-name: toasting;
	animation-duration: 0.25s;
	animation-iteration-count: 1;
}

@keyframes toasting {
	0% {
		opacity: 0;
		transform: scale(0) translateY(30px);
	}
}

.toasting-leave-active {
	transition: all 0.25s;
}

.toasting-leave-to {
	opacity: 0;
	transform: scale(0) translateY(30px);
}

.modal-fading-leave-active {
	transition: all 0.5s;
}

.modal-fading-leave-to {
	opacity: 0;
	transform: scale(0) translateY(100%);
}

.menu-retreat-leave-active {
	transition: all 0.5s;
}

.menu-retreat-leave-to {
	opacity: 0;
	transform: translateY(100%);
}
.chat-retreat-leave-active {
	transition: all 0.5s;
}
.chat-retreat-leave-to {
	opacity: 0;
	transform: translateY(100%);
}
#status_bar {
	position: absolute;
	top: $video-height;
	width: 100vw;
	z-index: 1100;
	color: white;
	background-color: rgb(0,0,0,0.7);
	padding: 0 20px;
	height: $status_height;
	line-height: $status_height;
}
#status_specific {
	position: absolute;
	z-index: 1199;
	top: calc($video-height + $status_height);
	width: 100vw;
	padding: 5px 20px 10px;
	color: white;
	background-color: rgb(0,0,0,0.5);
  animation-name: open_status;
  animation-duration: 0.5s;
  animation-iteration-count: 1;
}
@keyframes open_status {
  0% {
    opacity: 0;
    transform: translateY(-30%);
  }
  
}
.close_specific-leave-active {
  transition: all 0.5s
}
.close_specific-leave-to {
  opacity: 0;
  transform: translateY(-30%)
}
.for_trans {
  transition: all 0.5s;
}
.trans {
  transform: rotateZ(180deg);
}
#game_end_modal_container {
  z-index: 1000000;
  position: absolute;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100%;
  background-color: rgb(0,0,0,0.5);
}
#game_end_modal {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  padding: 20px;
  width: 90vw;
  background-color: #F2F2F2;
  border-radius: 20px;
  animation-name: game_end_modal_appear;
  animation-duration: 1s;
  animation-iteration-count: 1;
}
@keyframes game_end_modal_appear {
  0% {transform: translate(-50%, -50%) scale(0);}
  70% {transform: translate(-50%, -50%) scale(1.3);}
  85% {transform: translate(-50%, -50%) scale(0.8);}
  95% {transform: translate(-50%, -50%) scale(1.1);}
  100% {transform: translate(-50%, -50%) scale(1);}
}
</style>