import { createStore } from 'vuex'

export default createStore({
  state: {
    nav: true,
    nickname: null,
    loggedIn: false,

    // Modal on/off
    createRoomModal: false,
    passwordModal: false,
    pushModal: false,
    
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
  }
})
