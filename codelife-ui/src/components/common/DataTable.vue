<!--
  VUE数据表格
  使用示例：
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

  属性：
  columns: 表格列，每一列需要包含以下属性：title(列名)/field(可选，如果列是一些按钮的集合时可以为空；列所对应的属性)/
    width(列宽)/convert(可选，列的转换函数，如True转换成是等)/buttons(可选，如果列是一些按钮的集合时表示的是这些按钮的集合)
  dataUrl: 获取数据的链接；默认是Get方式获取数据
  buttons: 表格工具栏的附加按钮；表格会自动带有刷新按钮，如果需要其它的按钮就需要在此属性中进行指定了。
  paged: 是否分页；如果分页时，后台提供数据的服务接口需要接收offset(表示的是与第一条记录的偏移值)和limit(表示的是记录条数)两个参数，
    返回的数据需要是{total: *, rows: []}这样格式的数据； 如果不分页，那么返回的需要是[]这种格式的数据；

  其中，对于操作列，其中定义的按钮可以包含以下属性：
  value: 按钮显示的名称
  event: 按钮点击时的处理函数，会传入两个参数：第一个参数是该行的数据；第二个参数是表格当前展示的所有数据;
    注意对这两个参数的修改，也同时会体现到表格的展现上，典型的应用如删除某一列数据
  conditon: 根据该行数据来决定当前按钮是否显示。如：
    buttons: [
      {value: '审批通过', event: this.approve, condition: item => item.status == 'APPROVING'},
      {value: '解锁', event: this.unlock, condition: item => item.status == 'LOCKED'},
      {value: '注销', event: this.unregister, condition: item => !item.systemAdmin && (item.status == 'APPROVING' ||
        item.status == 'NORMAL' || item.status == 'LOCKED')},
      {value: '取消注销', event: this.unlock, condition: item => item.status == 'CANCEL'}
    ],
  convert: 指定按钮显示名称的转换器，根据当前行的数据来决定按钮显示名称。

-->
<template>
  <div class="m-0 p-2">
    <div class="datatable-toolbar pb-2">
      <input type='button' class='btn' value='刷新' v-on:click="refreshData"/>
      <input type='button' class='btn' v-for='button in buttons'
        :key='button.value' :value='button.value' v-on:click="button.event(refreshData)"/>
    </div>
    <table class="datatable">
      <thead>
        <tr>
          <th v-for='column in columns' :key="column.title" :width="column.width != null ? column.width : 100">
            {{column.title}}
          </th>
        </tr>

      </thead>
      <tbody>
        <tr v-for='data in dataList' :key='data.id'>
          <td v-for="column in columns" :key="column.title">
            <p v-if="column.buttons == null && column.convert == null">{{ data[column.field] }}</p>
            <p v-else-if="column.convert != null">{{ column.convert(data[column.field]) }}</p>
            <p v-else>
              <span v-for="button in column.buttons" :key="button.value">
                <input type='button' class="datatable-button mr-2"
                  v-if="button.condition ? button.condition(data) : true"
                  v-bind:value="button.convert != null ? button.convert(data) : button.value"
                  v-on:click="button.event(data, refreshData)"/>
              </span>
            </p>
          </td>
        </tr>
      </tbody>
    </table>
    <div class="datatable-footer row m-1 justify-content-center" v-if="paged">
        <label class="m-0 pt-1 mr-4">总数据：{{ total }} 条</label>
        <label class="m-0 pt-1 mr-4">当前页：{{ nowPage }} / {{ Math.ceil(total / pageSize) }}</label>
        <input class="mr-2 page-item" type="button" value="第一页" v-bind:disabled="firstButtonDisabled"
          v-on:click="firstPage"/>
        <input class="mr-2" type="button" value="上一页" id="lastPage" :disabled="lastButtonDisabled"
          v-on:click="lastPage"/>
        <input type="button" value="下一页" id="nextPage" :disabled="nextButtonDisabled"
          v-on:click="nextPage"/>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'dataTable',
  props: ['columns', 'dataUrl', 'buttons', 'paged'],
  data () {
    return {
      dataList: [],
      nowPage: 1,
      pageSize: 10,
      total: 0,
      firstButtonDisabled: true,
      lastButtonDisabled: true,
      nextButtonDisabled: true
    }
  },
  created () {
    this.refreshData()
  },
  methods: {
    // 刷新数据
    refreshData: function () {
      var _this = this
      var params = Object()

      if (this.paged) {
        params.offset = (this.nowPage - 1) * this.pageSize
        params.limit = this.pageSize
      }

      axios.get(this.dataUrl, {params: params}).then(function (resp) {
        if (!_this.paged) {
          // 不分页时
          _this.dataList = resp.data
          return
        }

        _this.dataList = resp.data.rows
        _this.total = resp.data.total

        // 根据总数据条数等来设置分页按钮状态
        if (_this.total == null || _this.total <= _this.pageSize) {
          // 没有数据或者只有一页
          _this.firstButtonDisabled = true
          _this.lastButtonDisabled = true
          _this.nextButtonDisabled = true
        } else {
          // 否则是有多页数据
          // 如果当前不是第一页，那么上一页和首页按钮可用，否则不可用
          _this.firstButtonDisabled = _this.lastButtonDisabled = (_this.nowPage === 1)

          // 如果总记录数比当前页数据+前面页数据要大，那么下一页可用，否则不可用
          _this.nextButtonDisabled = !(_this.total > (_this.nowPage * _this.pageSize))
        }
      })
    },
    firstPage: function () {
      this.nowPage = 1
      this.refreshData()
    },
    lastPage: function () {
      this.nowPage--
      this.refreshData()
    },
    nextPage: function () {
      this.nowPage++
      this.refreshData()
    }
  }
}
</script>

<style>
.datatable-toolbar {
  line-height: 1.2em;
}

.datatable-toolbar input[type=button] {
  background: #F0F8FF;
  line-height: 1em;
}

.datatable-toolbar input[type=button]:hover:enabled{
  background: #cce4f0;
}

.datatable-footer input[type=button], .datatable-button {
  background: #efefef;
  border: 1px solid #ddd;
  font-size: 0.8em;
  line-height: 1.8em;
}

.datatable-footer {
  font-size: 0.8em;
  line-height: 1.8em;
}

.datatable-footer input[type=button]:hover:enabled {
  background: #fff;
}

.datatable-footer label {
  color: #444;
}

.datatable {
  width: 100%;
  margin: 0px;
  padding: 0px;
  line-height: 2em;
  font-size: 14px;
}

.datatable th{
  background: #efffff;
  text-align: center;
  border: 1px solid #ADD8E6;
}

.datatable td{
  border: 1px solid #ADD8E6;
  padding-left: 10px;

  max-width: 300px;
}

.datatable td>p {
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
