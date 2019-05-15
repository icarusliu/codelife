<template>
  <div style="height: 100%;">
    <div class="col">
      <div class="row mb-2 mt-2">
        <label class="item-title">标题：</label>
        <input class="col-sm form-control"
        type="text" id="title" placeholder="标题" style="height: 40px;"
        autofocus
        v-model="title"/>
      </div>
      <div class="row mb-2 mt-2">
        <label class="item-title">分类：</label>
        <select name="type" class="col-sm-2 form-control" id="type" v-model="typeId">
          <option v-for="type in types" :key="type.id" :value="type.id">{{type.name}}</option>
        </select>
        <label class="item-title" v-if="null == article">版块：</label>
        <select name="forumId" class="col-sm-2 form-control" id="forum" required v-if="null == article"
          v-model="forumId">
          <option :value="forum.id" v-for="forum in forums" :key="forum.id">{{forum.name}}</option>
        </select>
        <div class="col-sm-1">
          <button type="button" class="btn btn-primary" v-on:click="submit">发表</button>
        </div>
      </div>

      <mavon-editor class="row content"
        v-model="content"></mavon-editor>
    </div>

    <dialog
      @close="fileManager.showDialog=false"
      v-if="fileManager.showDialog"
      :submit="fileManager.showDIalog=false"
      :title="fileManager.dialogTitle">
      <div>
        
      </div>
    </dialog>
  </div>
</template>

<script>
import ajax from '@/components/common/Ajax.js'
import Vue from 'vue'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import '@/static/css/article-detail.css'
import '@/static/css/comment.css'
import Dialog from '@/components/common/Dialog'

Vue.use(mavonEditor)

export default {
  data () {
    return {
      forums: [], // 版块
      types: [], // 分类
      myTopics: [], // 我参与的专题
      article: null, // 如果是编辑模式下，表示的是编辑的文章
      articleId: this.$route.params.articleId, // 传入的文章编号，如果不是-1，表示是编辑模式
      title: '',
      content: '',
      typeId: null,
      forumId: null,
      topicId: null,
      errorMessage: null,
      
      fileManager: {
        showDialog: false,
        dialogTitle: '文件管理'
      }
    }
  },
  components: {
    Dialog
  },
  created () {
    this.reload()
  },
  watch: {
    '$route.params': function (newValue) {
      this.articleId = newValue.articleId

      this.reload()
    }
  },
  methods: {
    reload: function () {
      let params = Object()
      if (Number(this.articleId) !== -1) {
        params.articleId = this.articleId
      }

      var _this = this
      ajax.post('/article/manager/getDataForNew', params, function (d) {
        let data = d.data
        _this.article = data.article
        _this.forums = data.forums
        _this.types = data.types

        if (data.article) {
          _this.title = data.article.title
          _this.content = data.article.content
          _this.typeId = data.article.articleType.id
          _this.forumId = data.article.forum.id
        } else {
          _this.title = ''
          _this.content = ''
          _this.typeId = data.types ? data.types[0].id : null
          _this.forumId = data.forums ? data.forums[0].id : null
        }
      })
    },
    submit: function () {
      // 提交新增请求
      if (!this.typeId) {
        this.errorMessage = '分类不能为空'
        return
      }

      if (this.title.trim() === '') {
        this.errorMessage = '标题不能为空'
        return
      }

      if (this.content.trim() === '') {
        this.errorMessage = '内容不能为空'
        return
      }

      this.errorMessage = ''

      let params = Object()
      params.type = this.typeId
      params.title = this.title.trim()
      params.content = this.content.trim()
      if (Number(this.articleId) !== -1) {
        params.id = this.articleId
      }
      if (this.forumId !== null) {
        params.forumId = this.forumId
      }

      if (this.topicId !== null && Number(this.topicId) !== 0) {
        params.topic = this.topicId
      }

      ajax.post('/article/manager/save', params, function () {
        alert('保存成功！')
        window.history.back(-1)
      })
    }
  }
}
</script>

<style>
  html,body,.container-fluid {
    height: 100%;
    background: #fff;
  }

  .btn {
    min-width: 60px;
  }

  .item-title {
    line-height: 30px;
    text-align: right;
    width: 60px;
  }

  .content {
    height: calc(100% - 110px);
  }
</style>
