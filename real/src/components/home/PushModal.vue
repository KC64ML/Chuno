<template>
  <div class="modal-bg">
    <div id="modal">
      알림 신청이 완료되었습니다.
      <div class="container" @click="$emit('')">
        <p>확인</p>
        <img alt="btn" src="@/assets/button_back1.svg">
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PushModal',
}
</script>

<style>

</style>