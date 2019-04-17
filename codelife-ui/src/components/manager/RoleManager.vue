<!-- 角色管理界面 -->
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
import { mapState } from 'vuex'

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
      ]
    }
  },
  created () {
  },
  components: {
    DataTable
  },
  methods: {
    add: function () {
      // 打开新增界面
    },
    modifyRole: function (item) {
    }
  }
}
</script>
