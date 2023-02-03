<template>
  <div>
    <div class="profile">   
      <label for="file">
        <!-- <img src="@/assets/profile_frame.png" alt="profileFrame"> -->
        <!-- <img src="@/assets/camera.svg" alt="upload pic"> -->
        <img :src="img ? img : imgDefault" alt="profilePic" class="uploadedImg">
        <input type="file" accept="image/*" id="file" class="inputfile" @change="upload">
      </label>

    </div>

    <div>
      <label for="nickname_input">닉네임</label>
      <input type="text" id="nickname_input" v-model="nickname" maxlength="6">

      <div class="check" v-if="nickname.length">
        <div v-if="!lengthValid || !useValid">
          <p v-if="!lengthValid">닉네임은 최대 6글자까지 가능합니다.</p>
          <p v-if="!useValid">이미 사용 중인 닉네임입니다.</p>
        </div>
        <div v-else>
          <p style="color:green;">사용 가능한 닉네임입니다.</p>
        </div>
      </div>
      <div v-else>
        <br>
      </div>
      <div class="container" @click="onSave">
        <p>저장</p>
        <img alt="btn" src="@/assets/main_button1.png">
      </div>
    </div>
  </div>
</template>

<script>
import imgDefault from '@/assets/profile_default.svg'

export default {
  name: 'RegisterView',
  data() {
    return {
      imgDefault,
      img: null,
      nickname: '',
      valid: true,
      lengthValid: true,
      useValid: true,

    }
  },
  methods: {
    // 프로필 사진
    upload(event) {
      let file = event.target.files
      let reader = new FileReader()

      reader.readAsDataURL(file[0])
      reader.onload = event => {
        console.log('img added')
        this.img = event.target.result
        console.log(this.img)
      }
    },
    // 닉네임 유효성 검사
    check() {
      if(this.nickname.length > 6) {
        this.lengthValid = false
      } else {
        this.lengthValid = true
      }
      // 중복체크 코드로 바꾸기
      this.axios.get(
        process.env + `user/nickname/${this.nickname}`
      )
        .then(res => {
          console.log(res.data)
          const validity = res.data.code
          if (validity) {
            console.log('가능')
            this.useValid = false
          } else {
            console.log('불가능')
            this.useValid = true
          }
        })
    },
    async onSave() {
      if(this.lengthValid && this.useValid) {
        alert("가입하기");
        const nick = this.nickname
        // const email = new URL(window.location.href).searchParams.get('email');
        const email = this.$route.params.email
        var token = await this.axios.post(process.env + "register", {"nick": nick, "email": email});
        console.log("회원가입 완료", token.data);
        // token에 토큰이 담겨있어요 쎄션스토리지에 넣어서 사용해하세요
        sessionStorage.setItem("token", token.data);
        //이곳에 회원가입이 완료하고 돌아갈 곳을 달아주세요
        this.$router.push({ name: 'Home' })
      } else {
        alert('닉네임을 바르게 설정하세요.')
      }
    }
  },
  watch: {
    'nickname': 'check',
  }

}
</script>

<style>
.profile{
  text-align: center;
}
.profile .uploadedImg {
  top: 0;
  left: 40%;
  height: 100px;
  width: 100px;
  border-radius: 100%;
  object-fit: cover;
  border: 2px solid #1D182C
}
.inputfile {
  width: 0;
  height: 0%;
  overflow: hidden;
}
/* .uploadBtn {
  width: 30px;
  height: 30px;
  background-color: #1D182C;
  border-radius: 100%;
  vertical-align: middle;
} */
.check {
  font-size: 11px;
  color: red;
}
.container{
  /* text-align: center; */
  display: flex;
  justify-content: center;
}
.container p {
  position: absolute;
  top: 57%;
  color: white;
  font-size: 24pt;
}
.container img{
  height: 70px;
}
</style>