import { createStore } from 'vuex'

export default createStore({
  state: {
    nickname: undefined,
    // Modal
    createRoomModal: false,
    passwordModal: false,
    pushModal: false,
    menuModal: false,
    itemModal: false,
    // Game
    item : [],
  },
  getters: {
  },
  mutations: {
  },
  actions: {
    useItem(context){
      context.state.itemModal = true
      console.log(context.item)
    }
  },
  modules: {
  }
})
