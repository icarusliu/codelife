<!-- 文章清单 -->
<template>
  <div class="row" style="box-sizing: content-box; ">
    <div class="col mr-0 ml-0 pr-0 pl-0">
      <div class="article-item row m-0 p-0 pl-2" v-for="article in articles" :key="article.id">
        <img class="col-3 col-md-2 m-2 p-0 mr-4" v-if="article.coverFileId" 
          style="box-shadow: 0px 0px 10px #bbb; max-height: 120px;"
          :src="baseUrl + 'file/download/' + article.coverFileId"/>
        <div class="col p-0 pt-1">
          <sup v-if="article.recommended" style="color: red; ">[置顶]</sup>
          <router-link class="article-item-title font-weight-bold"
              :to="{name: 'articleDetail', params: {id: article.id}}">{{ article.title }}</router-link>
          <p class="article-item-remark" :class="{'remark-img': article.coverFileId, 'remark-no-img': !article.coverFileId}">{{article.remark}}</p>

          <div class="row m-0 p-0">
            <div class="article-item-info-row col m-0 p-0">
              <div class="row ml-0 mr-0 article-item-info">
                <span class="mr-2">{{article.createDate}}</span>
                阅读：<span class="mr-2">{{article.readCount}}</span>
                点赞：<span>{{article.praiseCount}}</span>
              </div>
            </div>
            <span class="edit-button btn btn-link" @click="editArticle(article.id)"
              v-if="null != loginUser && article.authorID === loginUser.id">
              编辑
            </span>
          </div>
        </div>
      </div>
      <div class="article-item text-center hidden" id="loadingInfo" v-if="isLoading && !noPage">
          <p class="article-item-remark">加载中...</p>
      </div>

      <div class="article-item text-center hidden" id="noMoreDataInfo" v-if="noMoreData && !noPage">
        <p class="article-item-remark">无更多数据</p>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import axios from 'axios'
import $ from 'jquery'

export default {
  props: ['forumId', 'noPage', 'topicId'],
  name: 'articleList',
  computed: mapState([
    'loginUser'
  ]),
  data () {
    return {
      articles: [],
      nowPage: 1,
      pageSize: 20,
      noMoreData: false,
      isLoading: false,
      baseUrl: axios.defaults.baseURL
    }
  },
  watch: {
    forumId: function (newValue, oldValue) {
      this.articles = []
      this.nowPage = 1
      this.refreshData()
    }
  },
  methods: {
    // 刷新数据
    refreshData: function () {
      var that = this
      that.isLoading = true

      var params = {nowPage: this.nowPage, pageSize: this.pageSize}
      if (this.forumId !== -1) {
        params.forumId = this.forumId
      } 

      if (this.topicId && this.topicId !== -1) {
        params.topicId = this.topicId
      }

      axios.post('/article/getForExplorer', params)
        .then(function (resp) {
          if (resp.data !== null && resp.data.length > 0) {
            for (var i in resp.data) {
              that.articles.push(resp.data[i])
            }
          }

          var length = resp.data.length
          if (length === that.pageSize) {
            // 可能还有更多数据
            that.noMoreData = false
            that.nowPage++
          } else {
            that.noMoreData = true
          }

          that.isLoading = false
        }).catch(function (error) {
          if (error.response) {
            if (error.response.status === '404') {
              alert('（404）服务器地址错误，请确认！')
            }
          } else {
            alert('请求服务器异常，异常信息：[' + error + ']')
          }
        })
    },
    editArticle: function (id) {
      // 打开编辑界面
      this.$router.push({path: '/newArticle/' + id})
    }
  },
  created () {
    this.refreshData()
    var that = this

    $(window).scroll(function () {
      // 当滚动条滚动到最底下时，自动加载上一页数据
      if ($(window).scrollTop() + 20 >= $(document).height() - $(window).height()) {
        // 继续加载文档数据
        if (that.isLoading || that.noMoreData) {
          return
        }

        that.refreshData()
      }
    })
  }
}
</script>

<style>
/*文章清单样式*/
.article-tool-bar a {
    font-size: 0.8em;
}
.article-tool-bar {
    border-bottom: 1px solid #efefef;
}

.article-item {
    border-bottom: 1px dotted #efefef;
    padding-top: 4px;
    padding-left: 4px;
}

.article-item:hover {
    background: #f8f8f8;
}

.article-item-title {
    font-size: 1em;
    color: black;
}

.article-item-remark {
    font-size: 0.8em;
    margin-top: 4px;
    color: gray;
    max-height: 80px;
    margin-bottom: 0px;
    overflow: hidden; 
    text-overflow: ellipsis;
}

.article-item-info {
    font-size: 13px;
    line-height: 23px;
    color: #bbb;
}

.article-item-info>span {
    color: #3261A7;
    margin-right: 10px;
}

.edit-button {
    margin: 0px;
    padding: 0px;
    font-size: 13px!important;
    line-height: 23px;
    color: blue;
    margin-right: 10px;
}

.remark-img {
  height: 80px!important;
}

.remark-no-img {
  height: auto!important; 
}
</style>
