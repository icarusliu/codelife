<!-- 文章清单 -->
<template>
  <div class="row">
    <div class="col mr-0 ml-0 pr-0 pl-0">
      <div id="articleList">
        <div class="article-item" v-for="article in articles" :key="article.id">
            <sup v-if="article.recommended" style="color: red; ">[置顶]</sup>
            <router-link class="article-item-title font-weight-bold"
                :to="{name: 'articleDetail', params: {id: article.id}}">{{ article.title }}</router-link>

            <p class="article-item-remark">{{article.remark}}</p>
            <div class="row ml-0 mr-0">
              <div class="article-item-info col-sm">
                <span class="mr-4">{{article.authorName}}</span>
                发表于：<span class="mr-5">{{article.createDate}}</span>
                阅读次数：<span class="mr-5">{{article.readCount}}</span>
                点赞数：<span>{{article.praiseCount}}</span>
              </div>
              <div class="article-list-item-buttons col-sm-2 text-right">
                <div class="btn btn-link mb-2 mr-2" @click="editArticle(article.id)"
                  v-if="null != loginUser && article.authorID === loginUser.id">
                  编辑
                </div>
              </div>
            </div>
        </div>
      </div>
      <div class="article-item text-center hidden" id="loadingInfo" v-if="isLoading">
          <p class="article-item-remark">加载中...</p>
      </div>

      <div class="article-item text-center hidden" id="noMoreDataInfo" v-if="noMoreData">
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
  props: ['forumId'],
  name: 'articleList',
  computed: mapState([
    'loginUser'
  ]),
  data () {
    return {
      articles: [],
      nowPage: 1,
      pageSize: 12,
      noMoreData: false,
      isLoading: false
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
      let route = this.$router.resolve({name: '/'})
      window.open(route.href + 'newArticle/' + id, '_blank')
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
    padding-top: 10px;
    padding-left: 10px;
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
}

.article-item-info {
    font-size: 0.9em;
    color: #bbb;
}

.article-item-info>span, .article-item-info>cite {
    color: #3261A7;
}

.article-list-item-buttons .btn {
    font-size: 0.8em;
    line-height: 1em;
    margin: 0px;
    padding: 0px;
}

</style>
