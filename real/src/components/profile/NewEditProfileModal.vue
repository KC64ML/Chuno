<template>
  <div class="regiterview">
    <div id="make_room_modal">
      <div id="close_button" @click="offing">x</div>
      <div id="modal_title" style="font-size: 24px">프로필 편집</div>
      <div>
        <table>
          <colgroup>
            <col style="width: 90px" />
            <col style="width: 200px" />
          </colgroup>
          <tr id="profile_image">
            <td>
              <div v-if="this.img_url">
                <div id="profile_background">
                  <img
                    :src="this.img_url"
                    alt="profile pic"
                    class="uploadedImg"
                  />
                </div>
                <div>
                  <button @click="clearImage">다시 선택할래요</button>
                </div>
              </div>
              <div v-else>
                <img
                  id="blank_img"
                  src="@/assets/profile_default_with_cam.svg"
                  alt=""
                  @click="profile_click"
                />
              </div>
              <input
                ref="file_input"
                type="file"
                @change="oneFileSelect"
                style="display: none"
              />
            </td>
          </tr>
          <tr>
            <td>닉네임</td>
            <td>
              <input v-model="nickname" style="padding: 0 3%" />
            </td>
          </tr>
          <tr>
            <td>전화번호</td>
            <td>
              <input v-model="phone" style="padding: 0 3%" maxlength="12" />
            </td>
          </tr>
          <tr>
            <td class="grey">전화번호는 '-' 없이 숫자만 입력해주세요.</td>
          </tr>
        </table>
        <div class="flex_center hover_pointer" @click="save">
          <img
            src="@/assets/main_button1.png"
            id="button1"
            style="width: 140px"
          />
          <div class="image_text">저장</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import { formToJSON } from 'axios';

export default {
  name: "NewEditProfileModal",
  props: {
    userInfo: Object,
  },
  data() {
    return {
      nickname: this.userInfo.nickname,
      phone: this.userInfo.phone,
      can_use: false,
      one_file: undefined,
      img_url:
        process.env.VUE_APP_SPRING +
        "resources/images?path=" +
        this.userInfo.profile.path, // userInfo.profile : 기존 이미지
      check_img: false,
    };
  },
  methods: {
    offing() {
      this.$emit("on-modal");
    },

    async check() {
      console.log(this.nickname);
      await this.axios
        .get(process.env.VUE_APP_SPRING + "user/nickname/" + this.nickname)
        .then(({ data }) => {
          console.log("data : " + data.result);
          if (data.code) {
            this.can_use = true;
          } else {
            this.can_use = false;
          }
        });
    },

    profile_click() {
      console.log("profile_click 실행");
      this.$refs.file_input.click();
      // console.log("img : " + this.$refs.file_input.click());
    },
    oneFileSelect(e) {
      // (URL + 'resources/images?path=' + this.img_url)
      this.one_file = e.target.files[0];
      this.img_url = URL.createObjectURL(e.target.files[0]); // 기존에 있는것
      console.log("files : " + e.target.files[0]);
      console.log("one_file : " + this.one_file.path);
      console.log("e : " + e);
      console.log("img_url : " + this.img_url);
    },
    async save() {
      if (this.nickname.length == 0) {
        alert("닉네임을 확인해 주세요");
        return;
      } else {
        // 닉네임이 입력되었을 경우 화인한다.
        await this.check();

        // 현재 자신의 닉네임이 아니고, 다른 사람이 사용하는 닉네임인 경우
        if (this.can_use == true && this.userInfo.nickname != this.nickname) {
          alert("이미 사용 중인 닉네임이에요");
          return;
        }
      }

      console.log("닉네임 사용가능");

      // 현재 프로필을 저장하는 변수, 미래를 가져오는 변수
      // img_url : 이전 파일
      // one_file : 새로운 파일

      const formData = new FormData();
      formData.append("nickname", this.nickname);
      formData.append("phone", this.phone);
      if (this.one_file) {
        formData.append("file", this.one_file);
      }

      console.log("phone : ", this.phone);
      console.log("file : " + this.nickname);
      console.log("this.file : " + this.img_url);
      console.log("this.file : " + this.one_file.path);
      const token = sessionStorage.token;

      this.axios
        .put(process.env.VUE_APP_SPRING + "user/profile", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: token,
          },
        })
        .then((res) => {
          // const code = res.data.code
          // if(code) {
          console.log("회원가입 수정 성공");
          console.log(res);
          console.log(res.data);
          alert("등록완료");
          this.$router.push({ name: "home" });
          // } else {
          //   console.log(res)
          //   console.log('code err')
          // }
        })
        .catch((e) => {
          console.log("회원가입 실패");
          console.log(e);
        });
    },

    reSelect() {
      alert("다시선택");
    },
    clearImage() {
      this.$refs.file_input.value = "";
      this.one_file = undefined;
      this.img_url = undefined;
    },
  },
};
</script>

<style src="@vueform/slider/themes/default.css">
</style>
<style lang="scss" scoped>
$input_height: 30px;
$plma_size: 30px;
// $image_size: 140px;

// #profile_background {
//   // background-color: rgb(24, 138, 28);
//   margin: 20% auto;
//   height: $image_size * 0.8;
//   width: $image_size * 0.8;
//   border-radius: $image_size / 2;
//   display: flex;
//   align-items: center;
//   justify-content: center;
//   overflow: hidden;
// }

#modal_back {
  height: 100%;
  width: 100%;
  background: rgb(0, 0, 0, 0.6);
  z-index: 1;
}

#make_room_modal {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 85%;
  padding: 30px 0;
  background-color: #f2f2f2;
  transform: translate(-50%, -50%);
  font-size: 18%;
  border-radius: 10px;
}

#close_button {
  position: absolute;
  top: 10%;
  right: 10%;
  font-size: 20%;
}

#modal_title {
  text-align: center;
  margin-bottom: 20px;
}

#preview_img {
  width: 100%;
  height: 150%;
}

input {
  height: $input_height;
  background-color: rgb(67, 64, 57, 0.3);
  border-radius: $plma_size / 2;
}

table {
  border-spacing: 0 20px;
}

td:nth-child(1) {
  text-align: center;
}

td:nth-child(2) > div {
  display: flex;
  align-items: center;
}

.hour_type {
  background-color: #141414;
  color: #f2f2f2;
  height: $plma_size;
  line-height: $plma_size;
  text-align: center;
  width: 40%;
  border-radius: $plma_size / 4;
  margin-right: 8%;
}

.time_input {
  width: 40%;
  text-align: center;
}

.plma_button {
  background-color: #141414;
  color: #f2f2f2;
  width: $plma_size;
  height: $plma_size;
  line-height: $plma_size;
  text-align: center;
  border-radius: $plma_size / 4;
}

.option_button {
  height: $plma_size;
  padding: 10%;
  line-height: $plma_size;
  color: white;
}

.option_left {
  border-top-left-radius: $plma_size / 4;
  border-bottom-left-radius: $plma_size / 4;
}

.option_right {
  border-top-right-radius: $plma_size / 4;
  border-bottom-right-radius: $plma_size / 4;
}

.selected {
  background-color: #141414;
}

.unselected {
  background-color: rgb(67, 64, 57, 0.3);
}

.grey {
  color: gray;
}

.uploadedImg {
  width: 70%;
  height: 70%;
  border-radius: 100%;
}

.regiterview {
  height: 100vh;
  width: 100vw;
  background: rgb(0, 0, 0, 0.6);
  position: absolute;
  left: 0;
  top: 0;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>