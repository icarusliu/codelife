<template>
    <div v-if="isLogined" class="text-center content col-100">
        <form class="login-form col-sm-3 pt-2 pl-4 pb-2 m-auto"
            @submit.prevent="submit"
             method="POST">
            <input class="form-control row mb-2" v-model="user.password" type="password" placeholder="原密码" required/>
            <input class="form-control row mb-2" v-model="user.newPassword" type="password" placeholder="新密码" required/>
            <input class="form-control row mb-2" v-model="user.newPassword2" type="password" placeholder="请重复新密码" required/>
            <div class="row">
                <span class="col-sm"></span>
                <input type="submit" class="btn btn-primary ml-1 mr-4" value="更新"/>
            </div>

            <!-- 如果返回的链接中带有error参数，那么说明后台登录出现异常，可以通过Spring在Session中保存的异常变量获取异常信息并提示 -->
            <div class="mt-1 text-danger" v-if="null != errorMessage && '' != errorMessage"
                    style="font-size: 12px;  ">
                <!-- 登录失败的信息  -->
                {{ errorMessage }}
            </div>

            <div class="mt-1 text-success" style="font-size: 12px;" v-if="null != successMessage && '' != successMessage">
              {{successMessage}}
            </div>
        </form>
    </div>
</template>

<script>
import ajax from '@/components/common/Ajax'
import { mapState } from 'vuex'

export default {
  name: 'updatePassword',
  data () {
    return {
      user: {
        password: '',
        newPassword: '',
        newPassword2: ''
      },
      errorMessage: '',
      successMessage: ''
    }
  },
  computed: mapState([
    'isLogined',
    'loginUser'
  ]),
  methods: {
    submit: function () {
      if (this.user.newPassword !== this.user.newPassword2) {
        this.errorMessage = '两次输入密码不一致'
        return
      }

      this.errorMessage = ''

      var formData = this.user
      formData.password = this.user.password
      formData.newPassword = this.user.newPassword
      formData.userId = this.loginUser.id

      var that = this
      ajax.post('/user/resetPassword', formData, function (resp) {
        that.successMessage = '更新成功'
      })
    }
  }
}
</script>
