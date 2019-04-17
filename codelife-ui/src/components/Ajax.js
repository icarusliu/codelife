import axios from 'axios'

var ajax = Object()
ajax.post = function post (url, data, success) {
  axios.post(url, data).then(function (resp) {
    console.log(resp)

    if (resp.data.errorMessage) {
      alert('请求失败，错误信息：[' + resp.data.errorMessage + ']')
      return
    }

    if (success) {
      success(resp.data)
    }
  })
}

ajax.get = (url, data, success) => {
  console.log(data)

  axios.get(url, {params: data}).then(resp => {
    console.log(resp)
    if (resp.data.errorMessage) {
      alert('请求失败，错误信息：[' + resp.data.errorMessage + ']')
      return
    }

    if (success) {
      success(resp.data)
    }
  })
}

export default ajax
