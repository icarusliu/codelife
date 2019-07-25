import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    loginUser: null,
    isLogined: false
  },
  actions: {
    updateLoginUser: function ({commit, state}) {
      axios.post('/getLoginUser').then((resp) => {
        commit('setLoginUser', resp.data)
        window.localStorage.setItem('loginUserId', resp.data.id)
      })
    }
  },
  mutations: {
    setLoginUser: (state, loginUser) => {
      state.loginUser = loginUser
      state.isLogined = loginUser !== null && loginUser !== ''
    }
  },
  getters: {
  },
  modules: {
  }
})
export default store
