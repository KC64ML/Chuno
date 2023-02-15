<template>
    <div id="modal_back">
        <div id="make_room_modal">
            <div id="close_button" @click="offing">x</div>
            <div id="modal_title" style="font-size: 24px;">호패 편집</div>
            <div style="text-align: center;">
                <div v-if="img_url == 'profile/default.png'" id="profile_background">
                  <img
                  id="blank_img"
                  src="@/assets/profile_default_with_cam.svg"
                  alt=""
                  @click="profile_click"
                />
                </div>
                <div v-else>
                    <div id="profile_background">
                    <img
                      :src="URL + this.img_url"
                      alt="profile pic"
                      class="uploadedImg"
                    />
                    </div>
                    <div>
                    <button @click="clearImage">현재 사진 삭제</button>
                    </div>
                </div>
                <input
                    ref="file_input"
                    type="file"
                    @change="oneFileSelect"
                    style="display: none"
                />
            </div>
                <table style="width: 300px; margin: 0 auto;">
                    <colgroup>
                        <col style="width: 90px">
                        <col style="width: 200px">
                    </colgroup>
                    <tr>
                      <td>닉네임</td>
                      <td>
                          <input v-model="nickname" style="padding: 0 20px">
                      </td>
                    </tr>
                    <tr>
                      <td></td>
                      <td>
                        <p v-if="!can_use && nickname != userInfo.nickname && nickname.length > 0" 
                          style="color: red;">이미 사용 중인 닉네임 입니다.</p>
                      </td>
                    </tr>
                    <tr>
                      <td>전화번호</td>
                      <td>
                        <input v-model="phone" style="padding: 0 20px">
                      </td>
                    </tr>
                    <!-- <tr>
                      <td></td>
                      <td> -->
                        <!-- </td>
                        </tr> -->
                      </table>
                <p style="size: 18%; color: gray; text-align: center;">전화번호는 '-' 없이 숫자만 입력해주세요.</p>
                <div class="flex_center hover_pointer" @click="save">
                    <img src="@/assets/main_button1.png" id="button1" style="width: 140px">
                    <div class="image_text">저장</div>
                </div>
            </div>
    </div>
</template>

<script>
export default {
    props: {
      userInfo: Object,
    },
    data() {
      return {
        nickname: this.userInfo.nickname,
        phone: this.userInfo.phone,
        can_use: false,
        one_file: undefined,
        URL: process.env.VUE_APP_SPRING + "resources/images?path=",
        img_url: this.userInfo.profile.path, // userInfo.profile : 기존 이미지
        check_img: false,
      }
    },
    methods: {
      offing() {
        this.$emit("on-editProfile");
      },
      check() {
      console.log(this.nickname);
      this.axios
        .get(process.env.VUE_APP_SPRING + "user/nickname/" + this.nickname)
        .then(({ data }) => {
          console.log("result : " + data.result);
          console.log("code : " + data.code);
          if(!data.code){
            //없는 닉네임
            this.can_use = true
          } else {
            if(this.nickname == this.userInfo.nickname){
              this.can_use = true
            } else {
              this.can_use = false
            }
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
        // 현재 자신의 닉네임이 아니고, 다른 사람이 사용하는 닉네임인 경우
        if (this.can_use == false && this.nickname != this.userInfo.nickname) {
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
      } else {
        formData.append("file", this.one_file);

      }

      console.log("phone : ", this.phone);
      console.log("file : " + this.nickname);
      console.log("this.file : " + this.img_url);
      // console.log("this.file : " + this.one_file.path);
      const token = sessionStorage.token;

      this.axios
        .put(process.env.VUE_APP_SPRING + "user/profile", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: token,
          },
        })
        .then(() => {
          console.log("프로필 편집 성공");
          this.$emit('on-editProfile')
          this.$router.push({ name: "Profile", params: { uid: this.userInfo.id } });
        })
        .catch((e) => {
          console.log("프로필 편집 실패");
          console.log(e);
        });
    },

    reSelect() {
      alert("다시선택");
    },
    clearImage() {
      this.$refs.file_input.value = "";
      this.one_file = undefined;
      this.img_url = 'profile/default.png';
    },
  },
  watch: {
    'nickname' : 'check',
  }
}

</script>

<style src="@vueform/slider/themes/default.css"></style>
<style lang="scss" scoped>
$input_height: 30px;
$plma_size: 30px;
$image_size: 140px;

#profile_background {
  margin: auto;
  height: $image_size;
  width: $image_size;
  border-radius: $image_size / 2;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  object-fit: cover;
}
#modal_back {
    position: absolute;
    width: 100vw;
    height: 100%;
    background: rgb(0, 0, 0, 0.6);
    z-index: 5;
}

#make_room_modal {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 85%;
    padding: 30px 0;
    background-color: #f2f2f2;
    transform: translate(-50%, -50%);
    font-size: 18px;
    border-radius: 10px;
    animation-name: modal_appearance;
    animation-iteration-count: 1;
    animation-duration: 0.8s;
}

#close_button {
    position: absolute;
    top: 10px;
    right: 20px;
    font-size: 20px;
}

#modal_title {
    text-align: center;
    margin-bottom: 20px;
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

td:nth-child(2)>div {
    display: flex;
    align-items: center;
}
.hour_type {
    background-color: #141414;
    color: #f2f2f2;
    height: $plma_size;
    line-height: $plma_size;
    text-align: center;
    width: 40px;
    border-radius: $plma_size / 4;
    margin-right: 8px;
}
.time_input {
    width: 40px;
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
    padding: 0 20px;
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

@keyframes modal_appearance {
    0% {transform: translate(-50%, -50%) scale(0);}
    70% {transform: translate(-50%, -50%) scale(1.2);}
    85% {transform: translate(-50%, -50%) scale(0.9);}
    95% {transform: translate(-50%, -50%) scale(1.05);}
}
</style>