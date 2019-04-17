// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from '@/store/store.js'
import Vuex from 'vuex'
import Axios from 'axios'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.min'

Vue.config.productionTip = false

Vue.use(Vuex)

// 返回数据统一处理
Axios.interceptors.response.use(
  response => {
    if (response.status === 200 && response.data && response.data.statusCode === '200') {
      return Promise.resolve(response.data)
    } else {
      if (response.status !== 200) {
        alert('系统内部错误')
      } else {
        return Promise.reject(response.data)
      }
    }
  }
)

/* eslint-disable no-new */
new Vue({
  el: '#root',
  router,
  store,
  components: { App },
  template: '<App/>'
})
