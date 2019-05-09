<!-- 个人主页 -->
<template>
    <div>
        <div class="col-sm-2 col-md-2 mr-2 ml-0 pr-0 pl-0 hidden-xs-down sidebar">
            <div class="container side-block container mb-2 pr-3 pl-3">
                <div class="row p-1">
                    <span class="stat-info-title col-sm">{{ null != user ? user.realName : ''}}</span>

                    <hr class="w-100 m-2"/>

                    <div class="stat-info-item col-sm-3">
                        <div class="row text-center"><span class="col p-0">文章</span></div>
                        <div class="row text-center"><span class="col p-0">{{ null == statInfo ? 0 : statInfo.articleCount }}</span></div>
                    </div>

                    <div class="stat-info-item col-sm-3">
                        <div class="row text-center"><span class="col p-0">点赞</span></div>
                        <div class="row text-center">
                            <span class="col p-0">{{ null == statInfo ? 0 : statInfo.praiseCount }}</span></div>
                    </div>

                    <div class="stat-info-item col-sm-3">
                        <div class="row text-center"><span class="col p-0">阅读</span></div>
                        <div class="row text-center">
                            <span class="col p-0">{{null == statInfo ? 0 : statInfo.readCount}}</span></div>
                    </div>

                    <div class="stat-info-item col-sm-3">
                        <div class="row text-center"><span class="col p-0">评论</span></div>
                        <div class="row text-center"><span class="col p-0">{{ null == statInfo ? 0 : statInfo.commentCount }}</span></div>
                    </div>
                </div>
            </div>

            <div class="side-block container">
                <span class="side-block-title row">文章分类</span>
                <router-link class="side-block-item row"
                  :to="{name: 'userIndex', params: {userId: userId, typeId: -1, url: 'userIndex', nowPage: 1, pageSize: 20}}">
                  所有文章({{ null == statInfo ? 0 : statInfo.articleCount }})</router-link>
                <router-link class="side-block-item row" v-for="articleType in types" :key="articleType.id"
                  :to="{name: 'userIndex', params: {url: 'userIndex', nowPage: 1, pageSize: 20,
                    userId: userId, typeId: articleType.id}}">
                  {{ articleType.name }}({{articleType.articleCount}})
                </router-link>
            </div>
        </div>

        <div class="right-content col-sm mr-0">
            <div class="article-tool-bar row text-right m-0">
                <div class="btn btn-link"
                     v-if="loginUser.id === userId">
                    <router-link tag="a" target="_blank" :to="{name: 'newArticle', params: {articleId: -1}}">新建</router-link>
                </div>
            </div>

            <article-list :forumId="-1">
            </article-list>
        </div>
    </div>
</template>

<script>
import { mapState } from 'vuex'
import axios from 'axios'
import pager from '@/components/Pager'
import ArticleList from '@/components/article/ArticleList'

export default {
  name: 'userIndex',
  computed: mapState([
    'loginUser'
  ]),
  components: {
    pager,
    ArticleList
  },
  data () {
    return {
      statInfo: null, // 用户统计信息，包含用户文章总数、评论总数、阅读量以及点赞数
      userId: this.$route.params.userId, // 当前显示的用户编号
      typeId: this.$route.params.typeId, // 当前显示的分类编号
      nowPage: this.$route.params.nowPage, // 当前页
      pageSize: this.$route.params.pageSize, // 每页记录数
      types: [], // 当前用户的文章分类
      articles: [], // 当前页显示的文章清单
      url: 'userIndex',
      pages: 1,
      user: null,
      articleListTitle: '文章清单'
    }
  },
  watch: {
    '$route.params.nowPage': function (newValue) {
      this.nowPage = newValue
      this.refreshData()
    },
    '$route.params.typeId': function (newValue) {
      this.nowPage = 1
      this.typeId = newValue

      this.refreshData()
    },
    '$route.params.userId': function (newValue) {
      this.userId = newValue
      this.nowPage = 1

      this.refreshData()
    }
  },
  created () {
    // 启动时获取所有的分类信息、统计信息以及首页文章信息
    this.refreshData()
  },
  methods: {
    refreshData: function () {
      var params = Object()
      params.userId = this.userId
      params.nowPage = this.nowPage
      params.pageSize = this.pageSize
      if (Number(this.typeId) !== -1) {
        params.typeId = this.typeId
      }
      axios.post('/user/getIndexData', params).then((resp) => {
        this.statInfo = resp.data.statInfo
        this.types = resp.data.types
        this.articles = resp.data.articles
        this.pages = resp.data.pages
        this.user = resp.data.user
      })
    }
  }
}
</script>

<style>
.stat-info-title {
  font-size: 1em;
  line-height: 2em;
  color: black;
  font-weight: bold;
}

.stat-info-item {
  font-size: 0.8em;
}
</style>
