import axios from 'axios'

var ajax = Object()
ajax.post = function post (url, data, success) {
  axios.post(url, data).then(function (resp) {
    console.log(resp)

    if (resp.errorMessage) {
      alert('请求失败，错误信息：[' + resp.errorMessage + ']')
      return
    }

    if (success) {
      success(resp.data)
    }
  })
}

ajax.delete = (url, data, success) => {
  axios.delete(url, {params: data}).then(resp => {
    if (resp.errorMessage) {
      alert('请求失败，错误消息：[' + resp.errorMessage + ']')
    }

    success && success(resp.data)
  })
}

ajax.jsonPost = function (url, data, success) {
  axios.post(url, data, {
    headers: {'Content-Type': 'application/json'},
    transformRequest: data => JSON.stringify(data)
  }).then(function (resp) {
    console.log(resp)

    if (resp.errorMessage) {
      alert('请求失败，错误信息：[' + resp.data.errorMessage + ']')
      return
    }

    if (success) {
      success(resp)
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

/**
 * 文件上传
 */
ajax.uploadFile = (module, file, success) => {
  let url = '/file/upload'
  let loginUser = window.localStorage.getItem('loginUserId')

  console.log('loginUser: ' + loginUser)

  var uploadUser = -1
  if (loginUser) {
    uploadUser = loginUser
  }

  let formData = new FormData()
  formData.append('uploadUser', uploadUser)
  formData.append('module', module)
  formData.append('file', file)

  ajax.post(url, formData, success)
}

ajax.batchUpload = (module, files, success) => {
  let url = '/file/batchUpload'
  let loginUser = window.localStorage.getItem('loginUserId')

  console.log('loginUser: ' + loginUser)

  var uploadUser = -1
  if (loginUser) {
    uploadUser = loginUser
  }

  let formData = new FormData()
  formData.append('uploadUser', uploadUser)
  formData.append('module', module)

  for (var i = 0; i < files.length; i++) {
    formData.append('files', files[i])
  }

  ajax.post(url, formData, success)
}

export default ajax
