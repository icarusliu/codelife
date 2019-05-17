<template>
    <div v-if="!isLogined" class="text-center content col-100">
        <form class="login-form col-sm-3 pt-2 pl-4 pb-2 m-auto"
            @submit.prevent="submit"
             method="POST">
            <input class="form-control row mb-2" v-model="user.username" type="text"
                    placeholder="用户名" required/>
            <input class="form-control row mb-2" v-model="user.password" type="password" placeholder="密码" required/>
            <div class="row">
                <input type="button" class="btn btn-link offset-sm-6" value="注册"/>
                <input type="submit" class="btn btn-primary ml-1" value="登录"/>
            </div>

            <!-- 如果返回的链接中带有error参数，那么说明后台登录出现异常，可以通过Spring在Session中保存的异常变量获取异常信息并提示 -->
            <div class="mt-1 text-danger"
                    style="font-size: 12px;  ">
                <!-- 登录失败的信息  -->
                {{ errorMessage }}
            </div>
        </form>
    </div>
</template>

<script>
import ajax from '@/components/common/Ajax'
import { mapState } from 'vuex'

export default {
  name: 'login',
  data () {
    return {
      user: {
        username: '',
        password: ''
      },
      errorMessage: ''
    }
  },
  computed: mapState([
    'isLogined'
  ]),
  methods: {
    submit: function () {
      var formData = this.user
      formData.id = 'test'
      formData.client_id = 'codelife'
      formData.client_secret = 'secret'
      formData.scope = 'read'
      formData.grant_type = 'password'

      var that = this
      ajax.post('/oauth/token', formData, function (resp) {
        console.log('oauth token response: ' + resp)

        window.localStorage.setItem('accessToken', resp.access_token)
        that.$store.dispatch('updateLoginUser')
        that.$router.push('/')
      })
    }
  }
}
</script>
