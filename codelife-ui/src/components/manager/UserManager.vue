<!-- 专题管理界面 -->
<template>
  <div class="row">
    <data-table class="col"
      :columns="columns"
      :dataUrl="dataUrl"
      :buttons="buttons"
      :paged=false>
    </data-table>
  </div>
</template>

<script>
import DataTable from '@/components/DataTable'
import ajax from '@/components/Ajax.js'
import { mapState } from 'vuex'

export default {
  name: 'systemUserManager',
  computed: mapState([
    'loginUser'
  ]),
  data () {
    return {
      dataUrl: '/userManager/getAll',
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
      ]
    }
  },
  created () {
  },
  components: {
    DataTable
  },
  methods: {
    approve: function (item) {
      // 审批通过
      ajax.post('/userManager/approve', {id: item.id}, () => {
        item.status = 'NORMAL'
      })
    },
    unlock: function (item) {
      ajax.post('/userManager/unlock', {id: item.id}, () => {
        item.status = 'NORMAL'
      })
    },
    unregister: function (item) {
      ajax.post('/userManager/unregister', {id: item.id}, () => {
        item.status = 'CANCEL'
      })
    }
  }
}
</script>
