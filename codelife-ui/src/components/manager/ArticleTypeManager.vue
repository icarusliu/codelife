<!-- 分类管理界面 -->
<template>
  <div class="row">
    <data-table class="col"
      :columns="columns"
      :dataUrl="dataUrl"
      :buttons="buttons"
      :paged="false">
    </data-table>
  </div>
</template>

<script>
import DataTable from '@/components/DataTable'
import { mapState } from 'vuex'

export default {
  name: 'systemArticleTypeManager',
  computed: mapState([
    'loginUser'
  ]),
  data () {
    return {
      dataUrl: '/system/articleTypeManager/getAllForManager',
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
    renameType: function (item) {
    }
  }
}
</script>
