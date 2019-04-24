<!-- 角色管理界面 -->
<template>
  <div class="row">
    <data-table
      class="col"
      :columns="columns"
      :dataUrl="dataUrl"
      :buttons="buttons"
      :paged=false>
    </data-table>

    <my-dialog @close="dialog.show=false"
      v-if="dialog.show"
      :submit="addRole"
      :show="dialog.show"
      :title="dialog.title">
      <input class="form-control row mb-1" type='text' placeholder="角色名称" required v-model="dialog.item.name" />
      <input class="form-control row" type='text' placeholder="角色说明" required v-model="dialog.item.remark" />
    </my-dialog>
  </div>
</template>

<script>
import DataTable from '@/components/common/DataTable'
import MyDialog from '@/components/common/Dialog'
import { mapState } from 'vuex'
import axios from 'axios'

export default {
  name: 'systemRoleManager',
  computed: mapState([
    'loginUser'
  ]),
  data () {
    return {
      dataUrl: '/role/getAll',
      columns: [
        {
          title: '名称',
          field: 'name',
          width: 200
        },
        {
          title: '说明',
          field: 'remark',
          width: 100
        },
        {
          title: '操作',
          buttons: [
            {value: '修改', event: this.modifyRole}
          ],
          width: 100
        }
      ],
      buttons: [
        {value: '新增', event: this.add}
      ],
      dialog: {
        show: false,
        title: '',
        item: {
          id: -1,
          name: '',
          remark: ''
        }
      },
      refreshTable: null
    }
  },
  created () {
  },
  components: {
    DataTable,
    MyDialog
  },
  methods: {
    add: function (refreshTable) {
      // 打开新增界面
      this.dialog.item = {
        id: -1,
        name: '',
        remark: ''
      }
      this.dialog.title = '新增角色'
      this.dialog.show = true
      this.refreshTable = refreshTable
    },
    addRole: function () {
      if (!this.dialog.item.name) {
        alert('角色名称不能为空')
        return
      }

      if (!this.dialog.item.remark) {
        alert('角色说明不能为空')
        return
      }

      let params = {
        roleName: this.dialog.item.name,
        remark: this.dialog.item.remark
      }

      if (this.dialog.item.id && this.dialog.item.id !== -1) {
        params.id = this.dialog.item.id
      }

      let self = this
      axios.post('/role/save', params).then(function (resp) {
        self.refreshTable && self.refreshTable()
        self.dialog.show = false
      })
    },
    modifyRole: function (item, refreshTable) {
      this.refreshTable = refreshTable
      this.dialog.item = item
      this.dialog.show = true
      this.dialog.title = '修改角色'
    }
  }
}
</script>
