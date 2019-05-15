<template>
  <div>
    <h4 class="m-4 text-center">专题修改</h4>

    <h5 class="ml-2 text-primary">基本信息</h5>
    <hr/>
    <form class="row m-2" id="topicForm" method="post">
        <label class="error-message" id="errorMessage"></label>
        <div class="w-100"></div>

        <label class="col-sm-1 text-right">标题：</label>
        <input type="text" class="col-sm-6 form-control"
               v-model="topic.title"/>

        <div class="w-100 mb-2"></div>
        <label class="col-sm-1 text-right">介绍：</label>
        <textarea class="col-sm-6 form-control"
                  v-model="topic.introduction"/>

        <div class="w-100 mb-2"></div>
        <label class="col-sm-1 text-right">类型：</label>
        <select class="col-sm-6 form-control" v-model="selTopicType">
            <option v-for="(type, index) in topicTypes" :key="index" :value="index">{{type}}</option>
        </select>

        <div class="w-100 mb-2"></div>
        <label class="col-sm-1 text-right">管理员：</label>
        <input class="col-sm-2 form-control"
                :value="topic.topicAdminName"
                type="text" readonly/>
        <button class="btn btn-secondary ml-2"
                type="button" @click="showSetAdminDialog">变更</button>

        <div class="w-100 mb-2"></div>
        <label class="col-sm-1 text-right">封面：</label>
        <img class="col-sm-2" height="100px" v-if="null != topic && null != topic.img"
                :src="baseUrl + 'topic/getImg?fileName=' + topic.img" id="img"/>
        <input type="file" id="imgInput"
               accept="image/jpg,image/png"
               ref="imgFile"
               class="form-control col-sm-2"/>

        <div class="w-100 mb-2"></div>
        <div class="col-7 text-right">
            <input type="button" value="返回" class="btn btn-secondary" onclick="javascript: history.back(); "/>
            <input type="button" value="提交"
                v-on:click="saveTopic"
                class="btn btn-primary  ml-4"/>
        </div>
    </form>

    <my-dialog v-if="adminDialogShow"
        @close="adminDialogShow=false"
        :submit="setAdmin"
        title="选择管理员">
        <div class="col-sm">
            <div class="row">
              <input class="col-sm form-control mr-2" type="text" v-model="adminKeyWord"
                    placeholder="关键字"
                    autofocus
                    required/>
              <input type="button" class="btn btn-secondary btn-sm col-sm-3" value="搜索" @click="searchAdmin"/>
            </div>
        </div>

        <div class="w-100"></div>

        <select class="col-sm form-control mt-2"
                aria-multiselectable="false"
                v-model="selectTopicAdmin"
                multiple="false">
          <option v-for="user in userList" :key="user.id" :value="user">
            {{user.realName}}
          </option>
        </select>

        <span class="row error-message" id="adminDialogErrorMessage"></span>
    </my-dialog>

    <!-- 文章清单 -->
    <h5 class="ml-2 text-primary">文章清单</h5>
    <hr/>
    <data-table 
      class="col"
      ref="articleTable"
      :columns="articleTable.columns"
      :dataUrl="articleTable.dataUrl"
      :buttons="articleTable.buttons"
      :paged="false">
    </data-table>

    <my-dialog
        v-if="articleTable.dialog.show"
        @close="articleTable.dialog.show=false"
        :submit="addArticles"
        title="添加文章">
        <div class="col-sm">
            <div class="row">
              <input class="col-sm form-control mr-2" type="text" v-model="articleTable.dialog.key"
                    placeholder="关键字"
                    autofocus
                    required/>
              <input type="button" class="btn btn-secondary btn-sm col-sm-3" value="搜索" @click="searchArticle"/>
            </div>
        </div>

        <div class="w-100"></div>

        <select class="col-sm form-control mt-2"
                aria-multiselectable="false"
                v-model="articleTable.dialog.selArticleList"
                multiple="true">
          <option v-for="article in articleTable.dialog.articleList" :key="article.id" :value="article.id">
            {{article.title}}
          </option>
        </select>
    </my-dialog>
</div>
</template>

<script>
import ajax from '@/components/common/Ajax.js'
import MyDialog from '@/components/common/Dialog'
import axios from 'axios'
import DataTable from '@/components/common/DataTable'

export default {
  data () {
    return {
      topicId: this.$route.params.topicId,
      topic: {},
      articles: [],
      topicTypes: [],
      adminDialogShow: false,
      selectTopicAdmin: null,
      userList: [],
      adminKeyWord: '',
      selTopicType: -1,
      baseUrl: axios.defaults.baseURL,
      articleTable: {
        columns: [{
          title: '标题',
          field: 'title'
        }, {
          title: '作者',
          field: 'authorName'
        }, {
          title: '创建时间',
          field: 'createDate'
        }, {
          title: '操作',
          buttons: [
            {value: '删除', event: this.removeFromTopic}
          ]
        }],
        dataUrl: '/topic/getTopicArticles?topicId=' + this.$route.params.topicId,
        buttons: [{value: '新增', event: this.showArticleAddDialog}],
        dialog: {
          show: false,
          key: '',
          articleList: [],
          selArticleList: []
        },
        refreshTable: null
      }
    }
  },
  created () {
    this.loadData()
  },
  methods: {
    loadData: function () {
      // 从服务器加载数据
      let _this = this
      ajax.post('/topic/manager/getEditInfo', {topicId: this.topicId}, function (data) {
        _this.topic = data.topic
        _this.articles = data.articles
        _this.topicTypes = data.types

        for (var i = 0; i < _this.topicTypes.length; i++) {
          if (_this.topicTypes[i] === _this.topic.type) {
            _this.selTopicType = i
            break
          }
        }
      })
    },
    showSetAdminDialog: function () {
      this.adminDialogShow = true
      this.searchAdmin()
    },
    saveTopic: function () {
      var params = new FormData()
      params.append('id', this.topicId)
      params.append('title', this.topic.title)
      params.append('introduction', this.topic.introduction)
      params.append('type', this.selTopicType)

      if (this.selectTopicAdmin) {
        params.append('admin', this.selectTopicAdmin.id)
      }

      let files = this.$refs.imgFile.files
      if (files) {
        params.append('img', files[0])
      }
      var config = {
        headers: {'Content-Type': 'multipart/form-data'}
      }

      let _this = this
      axios.post('/topic/manager/update', params, config).then(function (resp) {
        alert('保存成功')
        _this.loadData()
      })
    },
    searchAdmin: function () {
      let keyword = this.adminKeyWord.trim()

      let _this = this
      ajax.post('/user/search', {key: keyword}, function (data) {
        if (!data || data.length === 0) {
          return
        }

        _this.userList = data
      })
    },
    setAdmin: function () {
      this.showSetAdminDialog = false
    }, 

    /**
     * 显示文章添加界面
     */
    showArticleAddDialog: function (refreshTable) {
      this.articleTable.dialog.show = true
      this.articleTable.refreshTable = refreshTable
    },

    /**
     * 搜索文章
     */
    searchArticle: function () {
      let key = this.articleTable.dialog.key
      var _this = this
      axios.get('/topic/manager/searchArticleNoInTopic', {params: {key: key, topicId: this.topicId, limit: 10}})
        .then(resp => {
          _this.articleTable.dialog.articleList = resp.data
        })
    },

    /**
     * 添加文章到专题
     */
    addArticles: function () {
      let selArticles = this.articleTable.dialog.selArticleList
      let _this = this
      if (selArticles) {
        axios.post('/topic/manager/addArticles', {id: this.topicId, articles: selArticles})
          .then(resp => {
            _this.articleTable.dialog.show = false
            _this.articleTable.refreshTable && _this.articleTable.refreshTable()
          })
      }
    },

    /**
     * 从专题中删除文章
     */
    removeFromTopic: function (item, refreshTable) {
      if (confirm('确定从专题中删除文章？')) {
        axios.post('/topic/manager/deleteArticle', {id: this.topicId, articleId: item.id})
        .then(resp => {
          refreshTable && refreshTable()
        })
      }
    }
  },
  components: {
    MyDialog,
    DataTable
  }
}
</script>

<style>
  select {
    padding: 0px;
  }

  select>option {
    line-height: 30px;
    height: 30px;
    padding-top: 4px;
  }
</style>
