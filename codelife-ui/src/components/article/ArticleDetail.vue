<!-- 文章展示界面 -->
<template>
  <div v-if="null != article">
    <!--文章目录-->
    <div class="d-none d-sm-block col-sm-2 p-2 article-catalog fixed-top" style="z-index: 1000; margin-top: 60px; ">
        <div class="article-catalog-content ml-2" v-html="catalog">
        </div>
    </div>

    <!--文章内容-->
    <div class="article-block col-sm-8 offset-sm-2 mr-0">
        <!--文章标题 -->
        <div class="row">
            <div class="col text-center">
                <h2 id="title">{{ article.title }}</h2>
                <p class="article-subtitle text-muted mr-4">
                    <span class="mr-2">{{
                          article.authorName + '&nbsp;发表于&nbsp;' + article.createDate
                        }}</span>
                    <span  class="mr-2">{{
                              '|&nbsp; &nbsp;' + article.readCount + '&nbsp; 次阅读'
                            }}</span>
                    <span id="praiseCount">{{article.praiseCount}}</span>&nbsp; 次点赞
                    <span class="btn btn-link edit-button mb-1 ml-4" v-if="null != loginUser && article.authorID === loginUser.id">编辑</span>
                </p>
            </div>
        </div>

        <!-- 文章内容  -->
        <div class="row">
            <div class="col" id="content" v-html="htmlContent">
            </div>
        </div>

        <!--点赞等按钮-->
        <div class="row article-detail-footer m-2">
            <div class="col text-center ml-2">
                <button type="button" class="btn btn-link"
                        id="praiseButton"
                        @click="doPraise()">
                    <img :src="praiseImg" id="praiseImg"/>
                </button>
            </div>
        </div>

        <!--评论对话框-->
        <form class="row m-2" method="post" id="commentForm">
            <div class="w-100 mb-1">
              <input class="col-sm-4 clear"
                type="text" v-model="comment.showName" placeholder="显示名称"/>
            </div>
            <textarea class="col-sm mr-3 form-control" placeholder="评论内容"
                      id="commentContent" v-model="comment.content"></textarea>
            <div class="w-100 mb-1"></div>
            <input class="btn btn-primary btn-small" type="button" value="提交" @click="addComment"/>
        </form>

        <!--评论内容-->
        <div class="comment-list row">
            <div class="comment-list-item col-sm-12" v-for="(comment, index) in comments" :key="comment.id">
                <h6 class="comment-list-item-info">
                    <span class="mr-2">{{ (index + 1) + '楼'}}</span>
                    <span class="mr-2">
                        {{comment.showName != null ? comment.showName : (
                          comment.commentUser != null && comment.commentUser.id == article.authorID
                        ? '作者' : (comment.commentUser != null ? comment.commentUser.realName : '匿名用户')
                        )}}
                    </span>
                    评论于<cite>{{comment.commentTime}}</cite>
                </h6>
                <p class="comment-list-item-content">{{comment.content}}</p>

                <div class="comment-list-item-buttons row">
                    <input class="btn btn-link" type="button" value="答复"
                            @click="replyComment(comment)"/>
                </div>

                <!--子评论-->
                <div class="sub-comment-list comment-list row ml-0" v-if="comment.children != null
                    && comment.children.length != 0">
                    <div class="comment-list-item col-sm-12" v-for="subComment in comment.children" :key="subComment.id">
                        <h6 class="comment-list-item-info">
                              <span class="mr-2">
                                {{subComment.showName != null ? subComment.showName : (
                                  subComment.commentUser != null && subComment.commentUser.id == article.authorID
                                ? '作者' : (subComment.commentUser != null ? subComment.commentUser.realName : '匿名用户')
                                )}}
                              </span>
                            评论于<cite>{{subComment.commentTime}}</cite>
                        </h6>
                        <p class="comment-list-item-content">{{subComment.content}}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 相关文章-->
    <div class="d-none d-sm-block col-sm-2 ml-0">
      <div class="row side-block">
        <label class="w-100 side-block-title">相关文章</label>
        <router-link :to="{name: 'articleDetail', params: {id: article.id}}"
          class="side-block-item w-100"
          v-for="article in relatedArticles" :key="article.id">
          {{ article.title }}
        </router-link>
      </div>
    </div>
</div>
</template>

<script>
import { mapState } from 'vuex'
import ajax from '@/components/common/Ajax.js'
import Vue from 'vue'
import '@/static/css/article-detail.css'
import '@/static/css/comment.css'
import hljs from 'highlight.js'
import 'highlight.js/styles/googlecode.css'
import showdown from 'showdown'
import {processHtml} from '@/static/js/markdown.js'
import axios from 'axios'
import $ from 'jquery'

Vue.directive('highlight', function (el) {
  let blocks = el.querySelectorAll('pre code')
  blocks.forEach((block) => {
    hljs.highlightBlock(block)
  })
})

export default {
  data () {
    return {
      articleId: this.$route.params.id, // 文章编号，从请求参数中获取
      article: null, // 文章
      comments: [], // 文章对应的评论清单
      relatedArticles: [],
      comment: {
        anonymos: false,
        content: '',
        type: 'article'
      },
      catalog: '',
      htmlContent: '',

      praiseImg: '/static/icons/thumb-up-2x.png',
      praised: false,
      parentCommentId: -1
    }
  },
  computed: mapState([
    'loginUser'
  ]),
  watch: {
    /**
     * 监控请求参数是否变化，如果变化则刷新数据
     */
    '$route.params': function (newValue) {
      this.articleId = this.$route.params.id

      this.refreshData()
    }
  },
  created () {
    this.refreshData()

    let cachedShowName = localStorage.getItem('commentShowName')
    if (cachedShowName) {
      this.comment.showName = cachedShowName
    }
  },
  methods: {
    /**
     * 刷新数据
     */
    refreshData: function () {
      let _this = this
      ajax.post('/article/getDetail', {id: this.articleId}, function (data) {
        _this.article = data.article
        _this.comments = data.comments
        _this.relatedArticles = data.relatedArticles

        var html = data.article.content
        html = _this.getConverter().makeHtml(html)
        let result = processHtml(html)

        let catalogHtml = result.catalog
        _this.catalog = '<h5 class="article-catalog-title">目录</h5>' + catalogHtml
        _this.htmlContent = result.html
      })
    },

    /**
     * 新增评论
     */
    addComment: function () {
      this.comment.articleId = this.articleId
      if (this.parentCommentId !== -1) {
        this.comment.parent = this.parentCommentId
      }

      if (!this.comment.showName) {
        alert('请输入显示名称')
        return
      }

      localStorage.setItem('commentShowName', this.comment.showName)

      if (!this.comment.content) {
        alert('请输入评论内容')
        return
      }

      let _this = this
      axios.post('/article/comment/add', this.comment).then(function (data) {
        console.log('Add comment result: ' + data)
        axios.get('/article/comment/findByArticle', {params: {articleId: _this.articleId}}).then(function (resp) {
          console.log(resp)
          _this.comments = resp.data
          _this.parentCommentId = -1
          _this.comment.content = ''
        })
      })
    },

    replyComment: function (parent) {
      this.parentCommentId = parent.id
      let parentUser = (parent.commentUser !== null && parent.commentUser.id === this.article.authorID) ? '作者' : (parent.showName === null ? '匿名用户' : parent.showName)

      this.comment.content = '【回复: ' + parentUser + '】'
      $('#commentContent').focus()
    },

    /**
     * 点赞
     */
    doPraise: function () {
      let _this = this
      let url = '/article/praise'
      if (this.praised) {
        url = '/article/unpraise'
      }

      axios.post(url, {id: this.articleId}).then(resp => {
        _this.praised = !_this.praised

        if (_this.praised) {
          _this.praiseImg = '/static/icons/thumb-up-2x-1.png'
        } else {
          _this.praiseImg = '/static/icons/thumb-up-2x.png'
        }
      })
    },

    /**
     * 获取MD转换器
     */
    getConverter: () => {
      return new showdown.Converter({extensions: (function () {
        function htmlunencode (text) {
          return (
            text.replace(/&amp/g, '&').replace(/&lt/g, '<').replace(/&gt/g, '>')
          )
        }
        return [
          {
            type: 'output',
            filter: function (text, converter, options) {
              //  use new shodown's regexp engine to conditionally parse codeblocks
              let left = '<pre><code\\b[^>]*>'
              let right = '</code></pre>'
              let flags = 'g'
              let replacement = function (wholeMatch, match, left, right) {
                //  unescape match to prevent double escaping
                match = htmlunencode(match)
                return left + hljs.highlightAuto(match).value + right
              }
              return showdown.helper.replaceRecursiveRegExp(text, replacement, left, right, flags)
            }
          }
        ]
      })()})
    }
  }
}
</script>

<style>
  /*文章目录样式*/
  .article-catalog-title {
    font-size: 0.8em;
    line-height: 2em;
    border-bottom: 1px solid #bbb;
    color: #00008B;
  }

  .article-catalog-content {
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .article-catalog-content a{
    font-size: 0.8em;
    line-height: 2em;
    color: #4169E1;
    overflow: hidden; 
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  #title {
    line-height: 80px;
    overflow: hidden;  
    text-overflow: ellipsis;
  }

  .article-block {
    background: #fff;
    margin-right: 10px;
    z-index: 1;
    box-shadow: 1px 1px 5px gray;
  }

  .article-subtitle>* {
    font-size: 13px!important;
    line-height: 25px!important;
  }

  #content {
    background: #fcfcfc;
    font-size: 14px;
  }

  #content pre{
    border: 1px solid #eee;
    padding: 10px;
    background: #fefefe;
  }

  #content blockquote{
    border: 1px solid #eeeeef;
    border-left: 10px solid #ddd;
    padding: 10px;
    background: #f8f8f8;
    color: #777;
  }

  #content table{
    border: 1px solid gray;
    padding: 0px;
    margin: 20px;
  }

  #content img {
    max-width: 100%;
  }

  #content table thead{
    background: #efefef;
  }

  #content table th, td {
    border: 1px solid gray;
    min-width: 100px;
  }

  /*文章尾部栏，如点赞按钮、转发按钮等*/
  .article-detail-footer button {
    cursor: pointer;
  }
  .article-detail-footer button:hover {
    background: #bbb;
  }
</style>
