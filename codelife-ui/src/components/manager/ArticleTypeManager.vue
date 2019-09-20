<!-- 分类管理界面 -->
<template>
  <div>
    <data-table class="col"
      :columns="columns"
      :dataUrl="dataUrl"
      :buttons="buttons"
      :paged="false">
    </data-table>

    <my-dialog @close="showDialog=false"
      v-if="showDialog"
      :submit="addType"
      :show="showDialog"
      :title="title">
      <input class="form-control row" type='text' placeholder="分类名称" required v-model="item.name" />
    </my-dialog>
  </div>
</template>

<script>
import DataTable from '@/components/common/DataTable'
import { mapState } from 'vuex'
import MyDialog from '@/components/common/Dialog'
import axios from 'axios'

export default {
  name: 'systemArticleTypeManager',
  computed: mapState([
    'loginUser'
  ]),
  data () {
    return {
      dataUrl: '/articleType/manager/list',
      columns: [
        {
          id: 1,
          title: '分类名称',
          field: 'name',
          width: 500
        },
        {
          id: 2,
          title: '操作',
          buttons: [
            {value: '重命名', event: this.renameType}
          ],
          width: 100
        }
      ],
      buttons: [
        {value: '新增', event: this.add}
      ],
      refreshData: null,
      showDialog: false,
      title: '新增分类',
      item: {
        name: '',
        id: -1
      }
    }
  },
  created () {
  },
  components: {
    DataTable,
    MyDialog
  },
  methods: {
    add: function (refreshData) {
      // 打开新增界面
      this.refreshData = refreshData
      this.showDialog = true
      this.item = {}
    },
    renameType: function (item, refreshData) {
       // 编辑 
       this.refreshData = refreshData
       this.item.name = item.name
       this.item.id = item.id

       this.title = '编辑分类'
       this.showDialog = true
    },
    addType: function () {
      if (!this.item.name) {
        alert('请输入名称')
        return
      }

      let self = this
      let params = {typeName: this.item.name}
      if (this.item.id && this.item.id !== -1) {
        params.id = this.item.id
      }

      axios.post('/articleType/manager/add', params).then(function (resp) {
        self.showDialog = false
        if (self.refreshData) {
          self.refreshData()
        }
      })
    }
  }
}
</script>
