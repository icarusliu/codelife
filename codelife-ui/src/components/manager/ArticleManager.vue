<template>
  <!-- 左侧导航页面 -->
  <div>
      <!-- 右侧文章列表 -->
      <data-table class="col" :columns="columns" :dataUrl="dataUrl" :buttons="buttons" :paged="true">
      </data-table>
  </div>
</template>

<script>
import DataTable from '@/components/DataTable'
import ajax from '@/components/Ajax.js'
import { mapState } from 'vuex'

export default {
  name: 'systemArticleManager',
  computed: mapState([
    'loginUser'
  ]),
  data () {
    return {
      dataUrl: '/article/manager/list',
      columns: [
        {
          id: 1,
          title: '标题',
          field: 'title',
          width: 500
        },
        {
          id: 2,
          title: '作者',
          field: 'authorName',
          width: 60
        },
        {
          id: 3,
          title: '发表时间',
          field: 'createDate',
          width: 180
        },
        {
          id: 4,
          title: '阅读数',
          field: 'readCount',
          width: 60
        },
        {
          id: 5,
          title: '点赞数',
          field: 'praiseCount',
          width: 60
        },
        {
          id: 6,
          title: '是否置顶',
          field: 'fixTop',
          convert: this.convertFixTop,
          width: 60
        },
        {
          id: 7,
          title: '操作',
          buttons: [
            {value: '置顶', event: this.fixTop, convert: this.fixTopButtonValueConvert},
            {value: '删除', event: this.deleteRow},
            {value: '编辑', event: this.editRow}
          ],
          width: 200
        }
      ],
      buttons: [
        {value: '新增', event: this.add}
      ]
    }
  },
  created () {
  },
  components: {
    DataTable
  },
  methods: {
    fixTopButtonValueConvert: function (item) {
      // 置顶按钮显示文字转换，如果当前行已经置顶，显示取消置顶；否则显示置顶
      if ((this.loginUser.systemAdmin && item.recommended) ||
          (!this.loginUser.systemAdmin && item.fixTop)) {
        return '取消置顶'
      }

      return '置顶'
    },
    fixTop: function (item) {
      var url = '/system/articleManager/fixTop'

      // 进行置顶或者取消置顶
      if ((this.loginUser.systemAdmin && item.recommended) ||
          (!this.loginUser.systemAdmin && item.fixTop)) {
        // 已经置顶则取消置顶
        url = '/system/articleManager/unFixTop'
      }

      // 调用后台服务进行置顶或者取消置顶处理
      let _this = this
      ajax.post(url, {id: item.id}, function (data) {
        if (_this.loginUser.systemAdmin) {
          item.recommended = !item.recommended
        } else {
          item.fixTop = !item.fixTop
        }
      })
    },
    add: function () {
      // 打开新增界面
      let route = this.$router.resolve({name: '/'})
      window.open(route.href + 'newArticle/-1', '_blank')
    },

    convertFixTop: function (fixTop) {
      return fixTop ? '是' : '否'
    },
    deleteRow: function (item, dataList) {
      // 删除记录
      if (!confirm('将删除该行，是否确认？')) {
        return
      }

      // 向服务器提交删除申请
      ajax.post('/system/articleManager/deleteArticle', {id: item.id}, function (data) {
        // 从表格中删除数据
        if (dataList) {
          for (var i = 0, length = dataList.length; i < length; i++) {
            if (item === dataList[i]) {
              dataList.splice(i, 1)
              break
            }
          }
        }
      })
    },
    editRow: function (item) {
      // 打开编辑界面
      let route = this.$router.resolve({name: '/'})
      window.open(route.href + 'newArticle/' + item.id, '_blank')
    }
  }
}
</script>
