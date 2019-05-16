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

Axios.defaults.baseURL = process.env.API_ROOT + 'api/'
Axios.defaults.withCredentials = true

// 转换数据成标准格式
Axios.defaults.transformRequest = function (data) {
  if (data instanceof FormData) {
    return data
  }

  var result = new URLSearchParams()

  for (var i in data) {
    result.append(i, data[i])
  }

  return result
}

Axios.interceptors.request.use(config => {
  let token = window.localStorage.getItem('accessToken')
  if (token && config.url.indexOf('/oauth/token') === -1) {
    config.withCredentials = true
    config.headers.Authorization = 'Bearer' + token

    console.log(config.headers.Authorization)
  }

  return config
})

// 返回数据统一处理
Axios.interceptors.response.use(
  response => {
    console.log('response: ' + response)

    if (response.status === 200 && response.data && response.data.statusCode === '200') {
      return Promise.resolve(response.data)
    } else if (response.status === 401) {
      window.open('/', '_self')
    } else {
      if (response.status !== 200) {
        alert('系统内部错误')
      } else {
        if (response.data) {
          return Promise.resolve(response.data)
        } else {
          return Promise.resolve(response)
        }
      }
    }
  },
  error => {
    console.error(error)
    let statusCode = error.response.status
    if (statusCode === 401 && error.response.data.error === 'invalid_token') {
      // Token失效
      console.log('invalid_token')
      window.localStorage.removeItem('accessToken')
      store.dispatch('updateLoginUser')
      router.push('/login')
    }
    return Promise.reject(error)
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
