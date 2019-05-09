<template>
    <nav class="navbar navbar-expand-md sticky-top navbar-light bg-dark m-0 mb-1" id="mainNav">
        <!--<a th:href="@{/}"><img class="navbar-brand" th:src="@{/icons/mouse.png}" width="30" height="30"/></a>-->
        <!-- 小页面上时导航栏按下拉的方式展现 -->
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarsExampleDefault"
                aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarsExampleDefault">
            <ul class="navbar-nav mr-auto" id="mainNavUl">
                <li class="nav-item text-center"
                    v-if="null == loginUser || !loginUser.systemAdmin">
                    <router-link :to="{name: 'index'}" name="index"
                        class="nav-link text-center font-weight-bold">首页</router-link>
                </li>
                <li class="nav-item"
                    v-if="null == loginUser || !loginUser.systemAdmin">
                    <router-link :to="{name: 'articles', params: {forumId: -1, title: '所有文章'}}"
                         name="index"
                    class="nav-link text-center font-weight-bold">文章浏览</router-link>
                </li>

                <li class="nav-item" v-if="null != loginUser && '' != loginUser && !loginUser.systemAdmin">
                    <router-link :to="{name: 'userIndex',
                        params: {url: 'userIndex', userId: loginUser.id, typeId: -1, nowPage: 1, pageSize: 20}}"
                        class="nav-link text-center font-weight-bold">个人主页</router-link>
                </li>

                <!-- <li class="nav-item" v-if="null != loginUser && '' != loginUser && !loginUser.systemAdmin">
                    <router-link class="nav-link text-center font-weight-bold"
                        :to="{name: 'myTopics', params: {topicId: -1}}">我的专题</router-link>
                </li> -->

                <li class="nav-item"
                    v-if="null == loginUser || !loginUser.systemAdmin">
                    <router-link class="nav-link text-center font-weight-bold"
                        :to="{name: 'topicExplorer'}">专题浏览</router-link>
                </li>

                <!-- 只有管理员才能看到系统管理界面 -->
                <li class="nav-item" v-if="null != loginUser && '' != loginUser">
                    <router-link class="nav-link text-center font-weight-bold"
                        :to="{name: 'systemManager'}">系统管理</router-link>
                </li>

                <li class="nav-item"
                    v-if="null == loginUser || !loginUser.systemAdmin">
                    <router-link class="nav-link text-center font-weight-bold"
                        :to="{name: 'about'}">关于</router-link>
                </li>
            </ul>
            <form class="form-inline my-2 my-lg-0">
                <router-link tag="a" target="_blank" v-if="null != loginUser" class="btn btn-sm btn-link mr-4" 
                    style="color: rgb(151, 229, 255); line-height: 24px; font-size: 0.8em;" :to="{name: 'newArticle', params: {articleId: -1}}">
                    <img src="/static/icons/write.png" width="15px" height="15px">写文章  
                </router-link>

                <div style="font-size: 0.8em; color: #fff;" v-if="null != loginUser && '' != loginUser">
                    登录用户：<font class="mr-4" style="color: #fc3962;">{{ loginUser.realName }}</font>
                    <a href="#" @click="logout" style="color: rgb(245, 222, 222);">注销</a>
                </div>

                 <div v-if="null == loginUser || '' == loginUser" style="font-size: 0.8em; ">
                    <input type="button" class="btn btn-link btn-sm my-nav-link" value="注册" onclick="javascript: register(); "/>
                    <input type="button" class="btn btn-sm btn-success login-button" value="登录" @click="login"/>
                </div>
            </form>
        </div>
    </nav>
</template>

<script>
import { mapState } from 'vuex'
import axios from 'axios'

export default {
  name: 'mainNav',
  computed: mapState([
    'loginUser'
  ]),
  created () {
    this.$store.dispatch('updateLoginUser')
  },
  methods: {
    logout: function () {
      // 退出登录
      axios.post('/customLogout').then((resp) => {
        window.localStorage.removeItem('accessToken')
        this.$store.dispatch('updateLoginUser')
        window.open('/', '_self')
      })
    },
    login: function () {
      this.$router.push({path: '/login'})
    }
  }
}
</script>
