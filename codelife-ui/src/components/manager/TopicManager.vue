<!-- 专题管理界面 -->
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
import DataTable from '@/components/common/DataTable'
import { mapState } from 'vuex'
import axios from 'axios'

export default {
  name: 'systemTopicManager',
  computed: mapState([
    'loginUser'
  ]),
  data () {
    return {
      dataUrl: '/topic/manager/getAll',
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
          title: '操作',
          buttons: [
            {value: '删除', event: this.delete},
            {value: '编辑', event: this.edit}
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
      let route = this.$router.resolve({name: '/'})
      window.open(route.href + 'topic/topicEdit/-1', '_blank')
    },
    delete: function (item, refreshTable) {
      if (!confirm('确定删除专题？')) {
        return
      }

      axios.post('/topic/manager/delete', {id: item.id}).then(resp => {
        refreshTable && refreshTable()
      })
    },
    edit: function (item) {
      // 打开编辑界面
      window.open('/topic/topicEdit/' + item.id, '_self')
    }
  }
}
</script>
