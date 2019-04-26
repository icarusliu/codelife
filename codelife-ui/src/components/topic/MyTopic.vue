<!-- 我的专题页面 -->
<template>
<div>

    <div class="p-0 pr-1 col-sm-3 d-none d-sm-block">
        <!--我的专题列表-->
        <div class="block mb-1" v-if="loginUser !== null">
            <h5 class="block-title">
                我的订阅
            </h5>
            <div class="list-group" id="topicList">
                <router-link class="row list-group-item ml-2 mr-2"
                   v-for="topic in myTopics" :key="topic.id"
                    :to="{name: 'myTopics', params: {topicId: topic.id}}">
                    <span>{{ topic.title }}</span>
                    <sup v-if="loginUser != null && topic.admin.id == loginUser.id"
                         style="color: red;">管理员</sup>
                </router-link>
            </div>
        </div>

        <div class="block">
            <h5 class="block-title">
                热门专题
            </h5>
            <router-link class="block-item row"
                v-for="topic in hotTopics" :key="topic.id"
                :to="{name: 'myTopics', params: {topicId: topic.id}}">
                <span>{{ topic.title }}</span>
                <sup v-if="loginUser != null && topic.admin.id == loginUser.id"
                      style="color: red;">管理员</sup>
            </router-link>
        </div>
    </div>

    <div class="col-sm topic-detail-panel mr-1"
      v-if="topic != null">
        <!--上面专题信息-->
        <section class="row topic-detail-panel-info">
            <h1 class="col-sm">
                <span>{{topic.title}}</span>
                <sub>
                  <router-link v-if="null != loginUser
                            && topic.admin.id == loginUser.id"
                        :to="{name: 'topicEdit', params: {topicId: topic.id}}">(编辑)</router-link>
                </sub>
            </h1>

            <div class="w-100 mb-2"></div>
            <p class="col-sm-8">{{topic.introduction}}</p>
            <img class="col-sm-3" height="200px"
                  :src="baseUrl + 'topic/getImg?fileName=' + topic.img" id="img"/>
        </section>

        <!--下面文章清单-->
        <div class="row">
            <article-list class="col-sm" :no-page="true" :forum-id="-1" :topic-id="topic.id">
            </article-list>

            <!--订阅用户清单-->
            <!-- <div class="user-group col-sm-2"
                  th:if="null != userList && 0 != userList.size()">
                <h6 class="group-title user-group-title row">订阅用户清单</h6>
                <router-link
                    class="user-group-item row" v-for="user in userList" :key="user.id"
                    :to="{name: 'userIndex',
                    params: {url: 'userIndex', userId: user.id, typeId: -1, nowPage: 1, pageSize: 20}}">
                    {{user.realName}}
                </router-link>
            </div> -->
        </div>
    </div>
</div>
</template>

<script>
import axios from 'axios'
import { mapState } from 'vuex'
import articleList from '@/components/article/ArticleList'

export default {
  name: 'myTopics',
  data () {
    return {
      myTopics: [], // 订阅的专题清单
      hotTopics: [], // 热门专题
      topic: null, // 显示的专题
      articles: [], // 显示的专题的文章清单
      userList: [], // 显示的专题的订阅用户清单
      topicId: null, // 显示的专题
      baseUrl: axios.defaults.baseURL
    }
  },
  components: {
    articleList: articleList
  },
  computed: mapState([
    'loginUser'
  ]),
  created () {
    this.topicId = this.$route.params.topicId

    // 初始化订阅的专题清单
    this.refreshData()
  },
  watch: {
    '$route.params': function (newValue) {
      this.topicId = this.$route.params.topicId
      this.refreshData()
    }
  },
  methods: {
    /**
     * 刷新页面数据
     */
    refreshData: function () {
      var that = this
      var params = Object()
      if (this.topicId !== -1) {
        params.id = this.topicId
      }

      this.myTopics = []
      this.topic = null
      this.articles = []
      this.userList = []

      axios.post('/topic/getMyTopics', params).then(function (resp) {
        that.myTopics = resp.data.myTopics
        that.topic = resp.data.topic
        that.articles = resp.data.articles
        that.userList = resp.data.userList
        that.hotTopics = resp.data.hotTopics
      })
    }
  }
}
</script>

<style>
  .topic-detail-panel {
    border: 1px solid #AFEEEE;
    background: #fff;
  }

  .topic-detail-panel-info {
    border-bottom: 1px solid #AFEEEE;
    padding-bottom: 4px;
    padding-top: 10px;
    background: #Fafafa;
  }

  .user-group {
    border-left: 1px solid #AFEEEE;
    padding-bottom: 1em;
  }

  .group-title {
    font-weight: bold;
    font-size: 0.8em;
    border-bottom: 1px solid #AFEEEE;
    line-height: 3em;
    color: #483D8B;
    padding-left: 1em;
  }

  .article-group-title {
    background: #F0F8FF;
  }

  .user-group-title {
    background: #F0FFFF;
  }

  .user-group-item {
    font-size: 0.8em;
    margin-left: 1em;
    color: blue;
    line-height: 2.4em;
    background: #fff;
  }
</style>
