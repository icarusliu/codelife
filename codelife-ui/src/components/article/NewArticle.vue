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
        <label class="item-title">关键词：</label>
        <input class="col-sm form-control"
        type="text" id="keywords" placeholder="关键词" style="height: 40px;"
        autofocus
        v-model="keywords"/>
      </div>

      <div class="row mb-2 mt-2">
        <label class="item-title">分类：</label>
        <select name="type" class="col-sm-2 form-control" id="type" v-model="typeId">
          <option v-for="type in types" :key="type.id" :value="type.id">{{type.name}}</option>
        </select>
        <label class="item-title" v-if="null == article">版块：</label>
        <select name="forumId" class="col-sm-2 form-control" id="forum" required
          v-model="forumId">
          <option :value="forum.id" v-for="forum in forums" :key="forum.id">{{forum.name}}</option>
        </select>
        <div class="col-sm-1 pt-2">
          <input type="checkbox" v-model="draft" class="mr-1"/>存草稿
        </div>
        <div class="col-sm-2">
          <button type="button" class="my-btn my-btn-gray mr-2 mt-2" v-on:click="showCoverManageDialog">封面管理</button>
          <button type="button" class="my-btn" v-on:click="submit">发表</button>
        </div>
      </div>

      <mavon-editor class="row content" @imgAdd='imgAdd' ref="md"
        v-model="content"></mavon-editor>
    </div>

    <my-dialog @close="cover.showDialog=false"
      v-if="cover.showDialog"
      :submit="closeCoverDialog"
      :title="cover.dialogTitle"
      :showCloseButton="false"
      :buttons="cover.buttons">
        <div class="col">
          <div class="row article-images">
            <img class="col-sm-3" @click="selCoverImage(image.id)" :class="{'selected': image.id === cover.selImage}"
              v-for="image in cover.images" :key="image.id" :src="baseUrl + 'file/download/' + image.id"/>
            <span class="col-sm-3"
              style="position: relative;">
              +
              <input type="file" style="opacity: 0; position: absolute; top: 0; left: 0; "
                ref="coverImage"
                @change="uploadImage"/>
            </span>
          </div>
        </div>
    </my-dialog>

    <!-- <my-dialog
      @close="fileManager.showDialog=false"
      v-if="fileManager.showDialog"
      :submit="fileManager.showDialog=false"
      :title="fileManager.dialogTitle">
      <div>
        
      </div>
    </my-dialog> -->
  </div>
</template>

<script>
import ajax from '@/components/common/Ajax.js'
import axios from 'axios'
import Vue from 'vue'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import '@/static/css/article-detail.css'
import '@/static/css/comment.css'
import MyDialog from '@/components/common/Dialog'

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
      keywords: '',
      content: '',
      typeId: null,
      forumId: null,
      topicId: null,
      errorMessage: null,
      
      fileManager: {
        showDialog: false,
        dialogTitle: '文件管理'
      },
      draft: false,
      baseUrl: axios.defaults.baseURL,

      cover: {
        showDialog: false,
        dialogTitle: '封面管理',
        images: [],
        selImage: null
      }
    }
  },
  components: {
    MyDialog
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
      ajax.post('/article/manager/getDataForNew', params, function (data) {
        _this.article = data.article
        _this.forums = data.forums
        _this.types = data.types

        if (data.article) {
          _this.title = data.article.title
          _this.content = data.article.content
          _this.typeId = data.article.articleType.id
          _this.forumId = data.article.forum.id

          _this.cover.selImage = data.article.coverFileId
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
      params.keywords = this.keywords.trim()
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

      if (this.draft) {
        params.status = 4
      }

      if (this.cover.selImage) {
        params.coverFileId = this.cover.selImage
      }

      ajax.post('/article/manager/save', params, function () {
        alert('保存成功！')
        window.history.back(-1)
      })
    },

    // 添加图片
    imgAdd: function (pos, file) {
      let _this = this
      ajax.uploadFile('文章', file, (resp) => {
        _this.$refs.md.$img2Url(pos, _this.baseUrl + 'file/download/' + resp.id)
      })
    },

    // 显示封面管理界面
    showCoverManageDialog: function () {
      // 从content中获取所有图片链接
      // 封面可以从文章中的图片中执行，也可以单独上传
      this.cover.showDialog = true
    },

    closeCoverDialog: function () {
      this.cover.showDialog = false
    },

    // 将当前选择的图片设为封面
    selCoverImage: function (id) {
      this.cover.selImage = id
    },

    uploadImage: function () {
      let _this = this
      let files = this.$refs.coverImage.files
      if (!files || files.length === 0) {
        return
      }

      let file = files[0]

      ajax.uploadFile('文章图片', file, (resp) => {
        _this.cover.images.push(resp)
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

  .article-images>span, .article-images>img {
    width: 100px; 
    height: 100px; 
    font-size: 60px;
    text-align: center;
    display: block; 
    border: 1px solid #efefef;
  }

  .article-images>span>input,
  .article-images>span>img {
    cursor: pointer; 
  }

  .article-images>span:hover {
    background: #eee;
  }

  .article-images>.selected {
    border: 2px solid rgb(124, 161, 240); 
  }
</style>
