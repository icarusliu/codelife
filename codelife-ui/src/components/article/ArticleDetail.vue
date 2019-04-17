<!-- 文章展示界面 -->
<template>
  <div v-if="null != article">
    <!--文章目录-->
    <div class="hidden-xs-down col-sm-2 p-2 article-catalog fixed-top " style="z-index: 1000; margin-top: 60px; ">
        <div class="article-catalog-content ml-2" v-html="catalog">
        </div>
    </div>

    <!--文章内容-->
    <div class="article-block col-sm offset-sm-2 mr-1">
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
                </p>
            </div>
        </div>

        <!-- 文章内容  -->
        <div class="row">
            <div class="col m-0 p-0" id="content">
                <mavon-editor v-model="article.content"
                    :toolbarsFlag=false
                    defaultOpen="preview"
                    :navigation="false"
                    :subfield="false">
                </mavon-editor>
            </div>
        </div>

        <!--点赞等按钮-->
        <div class="row article-detail-footer m-2">
            <div class="col text-center ml-2">
                <button type="button" class="btn btn-link"
                        id="praiseButton"
                        onclick="javascript: doPraise(); ">
                    <img src="/static/icons/thumb-up-2x.png" id="praiseImg"/>
                </button>
            </div>
        </div>

        <!--评论对话框-->
        <form class="row m-2" method="post" id="commentForm">
            <textarea class="col-sm mr-3 form-control" placeholder="评论内容" required
                      id="commentContent" v-model="comment.content"></textarea>
            <div>
                <div class="row ml-1 text-center">
                    <input class="mr-1" type="checkbox" v-model="comment.anonymos" style="margin-top: 2px; "/>
                    <label for="anonymos" style="line-height: 16px; ">匿名</label>
                </div>
                <div class="w-100"></div>
                <input class="btn btn-primary" type="button" value="提交" @click="addComment"/>
            </div>
        </form>

        <!--评论内容-->
        <div class="comment-list row">
            <div class="comment-list-item col-sm-12" v-for="(comment, index) in comments" :key="comment.id">
                <h6 class="comment-list-item-info">
                    <span class="mr-2">{{ (index + 1) + '楼'}}</span>
                    <span class="mr-2">
                        {{comment.commentUser != null && comment.commentUser.id == article.authorID
                        ? '题主' : (comment.commentUser != null ? comment.commentUser.realName : '匿名用户')}}
                    </span>
                    评论于<cite>{{comment.commentTime}}</cite>
                </h6>
                <p class="comment-list-item-content">{{comment.content}}</p>

                <div class="comment-list-item-buttons row">
                    <input class="btn btn-link" type="button" value="答复"
                            th:onclick="'replyComment(' + ${comment.id} + ')'"/>
                </div>

                <!--子评论-->
                <div class="sub-comment-list comment-list row ml-0" v-if="comment.children != null
                    && comment.children.length != 0">
                    <div class="comment-list-item col-sm-12" v-for="subComment in comment.children" :key="subComment.id">
                        <h6 class="comment-list-item-info">
                              <span class="mr-2">{{subComment.commentUser != null
                                        && subComment.commentUser.id == article.authorID
                                  ? '题主' : (subComment.commentUser != null
                                                ? subComment.commentUser.realName : '匿名用户')}}</span>
                            评论于<cite>{{subComment.commentTime}}</cite>
                        </h6>
                        <p class="comment-list-item-content">{{subComment.content}}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</template>

<script>
import ajax from '@/components/Ajax.js'
import Vue from 'vue'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import '@/static/css/article-detail.css'
import '@/static/css/comment.css'

Vue.use(mavonEditor)

export default {
  data () {
    return {
      articleId: this.$route.params.id, // 文章编号，从请求参数中获取
      article: null, // 文章
      comments: [], // 文章对应的评论清单
      comment: {
        anonymos: false,
        content: '',
        type: 'article',
        id: this.articleId ? this.articleId : -1
      },
      catalog: ''
    }
  },
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

        let catalogHtml = processHtml(_this.article.content)
        _this.catalog = '<h5 class="article-catalog-title">目录</h5>' + catalogHtml
      })

      // 获取评论清单
      ajax.get('/comment/findByDestination', {id: this.articleId, type: 'article'}, data => {
        console.log(data)
      })
    },

    /**
     * 新增评论
     */
    addComment: function () {
      if (!this.comment.content) {
        alert('请输入评论内容')
        return
      }

      ajax.post('/comment/add', this.comment, data => {
        console.log(data)
      })
    }
  }
}

/**
 * 判断指定行是否是目录
 * 以<h数字>开头且</h数字>结尾的行
 **/
function isCatalog (line) {
  // 至少15个字符才是标题行
  if (line < 15) {
    return false
  }

  // 至少要包含id=""这样的字符才是标题行
  if (line.indexOf('id="') === -1) {
    return false
  }

  return /<h\d.*>.*<\/h\d+>/.test(line)
}

/**
 * 获取标题行的ID及标题内容
 **/
export function processCatalog (line) {
  var result = Object()

  // 获取标题的层级，主要是h后的数字，如<h6 id="test">...</h6>
  // 取第三位的数字就可以了
  var level = line.substring(2, 3)
  result.level = level

  // 先去掉第一个引号前的内容
  line = line.substring(line.indexOf('"') + 1)

  // 获取剩下的第一个引号前的内容为ID
  result.id = line.substring(0, line.indexOf('"'))

  // 获取>与<之间的内容
  line = />.*</.exec(line)
  line = line.toString()
  line = line.substring(1, line.length - 1)

  result.title = line

  return result
}

/**
 * 获取并组装目录
 **/
function processHtml (content) {
  // 1. 章节转换 #开头的部分 其中#转换成h1，##转换成h2等；
  var lines = content.split('\n')
  var catalogHtml = '' // 存储目录

  for (var j in lines) {
    var line = lines[j]

    // 如果是目录
    if (isCatalog(line)) {
      // 是目录时生成目录的HTML
      var titleObject = processCatalog(line)
      for (var i = 1; i < titleObject.level; i++) {
        catalogHtml += '&nbsp &nbsp '
      }
      catalogHtml += '<a href="#"' + titleObject.id + '>' + titleObject.title + '</a></br>'
    }
  }

  return catalogHtml
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

  .article-catalog-content a{
    font-size: 0.8em;
    line-height: 2em;
    color: #4169E1;
  }

  #title {
    line-height: 80px;
  }

  .article-block {
    background: #fff;
    margin-right: 10px;
    z-index: 1;
    box-shadow: 1px 1px 5px gray;
  }

  .article-subtitle {
    font-size: 0.8em;
  }

  #content {
    background: #fcfcfc;
  }

  #content>p{
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
