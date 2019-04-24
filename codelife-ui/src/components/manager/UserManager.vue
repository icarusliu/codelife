<!-- 专题管理界面 -->
<template>
  <div class="row">
    <data-table 
      class="col"
      :columns="columns"
      :dataUrl="dataUrl"
      :buttons="buttons"
      :paged="false">
    </data-table>
  </div>
</template>

<script>
import DataTable from '@/components/common/DataTable'
import { mapState } from 'vuex'
import axios from 'axios'

export default {
  name: 'systemUserManager',
  computed: mapState([
    'loginUser'
  ]),
  data () {
    return {
      dataUrl: '/manager/user/getAll',
      columns: [
        {
          title: '用户名',
          field: 'username',
          width: 200
        },
        {
          title: '显示名称',
          field: 'realName',
          width: 100
        },
        {
          title: '注册时间',
          field: 'registerTime',
          width: 100
        },
        {
          title: '状态',
          field: 'status',
          width: 100
        },
        {
          title: '登录错误次数',
          field: 'errorCount',
          width: 100
        },
        {
          title: '操作',
          buttons: [
            {value: '审批通过', event: this.approve, condition: item => item.status === 'APPROVING'},
            {value: '解锁', event: this.unlock, condition: item => item.status === 'LOCKED'},
            {value: '注销',
              event: this.unregister,
              condition: item => !item.systemAdmin && (item.status === 'APPROVING' ||
                item.status === 'NORMAL' || item.status === 'LOCKED')},
            {value: '取消注销', event: this.unlock, condition: item => item.status === 'CANCEL'}
          ],
          width: 100
        }
      ],
      buttons: [
      ],
      refreshTable: null
    }
  },
  created () {
  },
  components: {
    DataTable
  },
  methods: {
    approve: function (item, refreshTable) {
      this.refreshTable = refreshTable

      // 审批通过
      let self = this
      axios.post('/manager/user/approve', {id: item.id}).then(function (resp) {
        self.refreshTable && self.refreshTable()
      })
    },
    unlock: function (item, refreshTable) {
      this.refreshTable = refreshTable

      let self = this
      axios.post('/manager/user/unlock', {id: item.id}).then(function (resp) {
        self.refreshTable && self.refreshTable()
      })
    },
    unregister: function (item, refreshTable) {
      this.refreshTable = refreshTable

      let self = this
      axios.post('/manager/user/unregister', {id: item.id}).then(function (resp) {
        self.refreshTable && self.refreshTable()
      })
    }
  }
}
</script>
