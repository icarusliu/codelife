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
import { mapState } from 'vuex'

export default {
  name: 'systemTopicManager',
  computed: mapState([
    'loginUser'
  ]),
  data () {
    return {
      dataUrl: '/system/topicManager/getAll',
      columns: [
        {
          id: 1,
          title: '专题名称',
          field: 'title',
          width: 200
        },
        {
          id: 2,
          title: '创建者',
          field: 'creator',
          width: 100,
          convert: creator => creator.realName
        },
        {
          id: 3,
          title: '管理员',
          field: 'admin',
          width: 100,
          convert: admin => admin ? admin.realName : ''
        },
        {
          id: 4,
          title: '状态',
          field: 'status',
          width: 100
        },
        {
          id: 5,
          title: '类型',
          field: 'type',
          width: 100
        },
        {
          id: 6,
          title: '介绍',
          field: 'introduction',
          width: 200
        },
        {
          id: 7,
          title: '封面',
          field: 'img',
          width: 100
        },
        {
          id: 8,
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
