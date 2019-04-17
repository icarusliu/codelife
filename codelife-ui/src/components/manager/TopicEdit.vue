<template>
  <div>
    <h4 class="m-4 text-center">专题修改</h4>

    <h5 class="ml-2 text-primary">基本信息</h5>
    <hr/>
    <form class="row m-2" id="topicForm" method="post">
        <label class="error-message" id="errorMessage"></label>
        <div class="w-100"></div>

        <label class="col-sm-1 text-right">标题：</label>
        <input type="text" class="col-sm-6 form-control" id="title"
               v-model="title"/>

        <div class="w-100 mb-2"></div>
        <label class="col-sm-1 text-right">介绍：</label>
        <textarea class="col-sm-6 form-control" id="introduction"
                  v-model="introduction"/>

        <div class="w-100 mb-2"></div>
        <label class="col-sm-1 text-right">类型：</label>
        <select class="col-sm-6 form-control" id="type" v-model="topicType">
            <option v-for="(type, index) in topicTypes" :key="index" :value="index">{{type}}</option>
        </select>

        <div class="w-100 mb-2"></div>
        <label class="col-sm-1 text-right">管理员：</label>
        <input class="col-sm-2 form-control" id="admin"
                :value="topicAdminName"
                type="text" readonly/>
        <button class="btn btn-secondary ml-2"
                type="button" @click="showSetAdminDialog = true">变更</button>

        <div class="w-100 mb-2"></div>
        <label class="col-sm-1 text-right">封面：</label>
        <img class="col-sm-2" height="100px"
                :src="null != topic ? topic.img : ''" id="img"/>
        <input type="file" id="imgInput"
               accept="image/jpg,image/png"
               onchange="checkFileSize(this); "
               class="form-control col-sm-2"/>

        <div class="w-100 mb-2"></div>
        <div class="col-7 text-right">
            <input type="button" value="返回" class="btn btn-secondary" onclick="javascript: history.back(); "/>
            <input type="button" value="提交"
                v-on:click="saveTopic"
                class="btn btn-primary  ml-4"/>
        </div>
    </form>

    <my-dialog v-if="showSetAdminDialog"
        @close="showSetAdminDialog=false"
        :submit="setAdmin"
        title="test" class="container-fluid"
        id="setAdminDialog">
        <div class="row">
            <input class="col-sm form-control mr-2" type="text" id="adminKeyWord"
                    placeholder="关键字"
                    maxlength="100" autofocus
                    required/>
            <input type="button" class="btn col-sm-3" value="搜索" @click="searchAdmin"/>
        </div>

        <select class="row form-control mt-2"
                id="adminSelect"
                aria-multiselectable="false"
                multiple="false">

        </select>

        <span class="row error-message" id="adminDialogErrorMessage"></span>
    </my-dialog>
</div>
</template>

<script>
import ajax from '@/components/Ajax.js'
import MyDialog from '@/components/common/Dialog'
import $ from 'jquery'

export default {
  data () {
    return {
      topicId: this.$route.params.topicId,
      topic: null,
      articles: [],
      topicTypes: [],
      title: '',
      introduction: '',
      topicType: null,
      topicAdminId: null,
      topicAdminName: '',
      showSetAdminDialog: false
    }
  },
  created () {
    this.loadData()
  },
  methods: {
    loadData: function () {
      // 从服务器加载数据
      let _this = this
      ajax.post('/system/topicManager/getEditInfo', {topicId: this.topicId}, function (data) {
        _this.topic = data.topic
        _this.articles = data.articles
        _this.topicTypes = data.types

        _this.title = data.topic.title
        _this.introduction = data.topic.introduction
        _this.topicAdminName = data.topic.admin.realName
        _this.topicAdminId = data.topic.admin.id

        for (let i = 0, length = _this.topicTypes.length; i < length; i++) {
          let topicType = _this.topicTypes[i]
          if (topicType === data.topic.type) {
            _this.topicType = i + 1
            break
          }
        }
      })
    },
    saveTopic: function () {
    },
    searchAdmin: function () {
      let keyword = $('#adminKeyWord').val().trim()
      if (keyword === '') {
        $('#adminDialogErrorMessage').html('关键字不能为空！')
        $('#adminDialogErrorMessage').show()
        return
      }

      $('#adminDialogErrorMessage').hide()

      $('#adminSelect').html('')
      ajax.post('/user/search', {key: keyword}, function (data) {
        if (!data || data.length === 0) {
          return
        }

        for (var i in data) {
          var user = data[i]

          $('<option value="' + user.id + '">' + user.realName + '</option>')
            .appendTo($('#adminSelect'))
        }

        // 选中第一个
        $('#adminSelect').val(data[0].id)
      })
    },
    setAdmin: function () {
      this.topicAdminId = $('#adminSelect').val()
      this.topicAdminName = $('#adminSelect').text()
      this.showSetAdminDialog = false
    }
  },
  components: {
    MyDialog
  }
}
</script>

<style>

</style>
