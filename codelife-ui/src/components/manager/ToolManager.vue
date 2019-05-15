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
      :submit="add"
      :title="dialog.title">
      <div class="form-row">
        <label>工具名称：</label>
        <input class="form-row-control" type='text' placeholder="工具名称" required v-model="dialog.item.name" />
      </div>
      
      <div class="form-row">
        <label>工具说明：</label>
        <textarea style="width: 360px;"
            placeholder="工具说明" required v-model="dialog.item.desc" />
      </div>

      <div class="form-row">
        <label>工具文件：</label>
        <input class="form-row-control" type='file' ref="toolFile"/>
      </div>

      <div class="form-row">
        <label>工具图片：</label>
        <input class="form-row-control" type='file' ref="imgFiles" multiple="true" accept="image/*"/>
      </div>

      <div class="form-row image-block">
        <img v-for="image in dialog.images" class="image"
          :key="image.id" :src="baseUrl + 'file/download/' + image.id"/>
      </div>
    </my-dialog>
  </div>
</template>

<script>
import DataTable from '@/components/common/DataTable'
import MyDialog from '@/components/common/Dialog'
import { mapState } from 'vuex'
import ajax from '@/components/common/Ajax.js'
import axios from 'axios'

export default {
  name: 'toolManager',
  computed: mapState([
    'loginUser'
  ]),
  data () {
    return {
      dataUrl: '/tool',
      columns: [
        {
          title: '名称',
          field: 'name',
          width: 200
        },
        {
          title: '说明',
          field: 'desc',
          width: 100
        },
        {
          title: '上传时间',
          field: 'updateTime',
          width: 100
        },
        {
          title: '下载次数',
          field: 'fileInfo',
          convert: (data) => data ? data.downloadCount : 0
        },
        {
          title: '操作',
          buttons: [
            {value: '修改', event: this.modify},
            {value: '删除', event: this.delete}
          ],
          width: 100
        }
      ],
      buttons: [
        {value: '新增', event: this.showAddDialog}
      ],
      dialog: {
        show: false,
        title: '',
        item: {
          id: -1,
          name: '',
          desc: ''
        }
      },
      images: [],
      refreshTable: null,
      baseUrl: axios.defaults.baseURL
    }
  },
  created () {
  },
  components: {
    DataTable,
    MyDialog
  },
  methods: {
    showAddDialog: function (refreshTable) {
      // 打开新增界面
      this.dialog.item = {
        id: -1,
        name: '',
        desc: ''
      }
      this.dialog.title = '新增工具'
      this.dialog.show = true
      this.refreshTable = refreshTable
    },
    add: function () {
      if (!this.dialog.item.name) {
        alert('工具名称不能为空')
        return
      }

      if (!this.dialog.item.desc) {
        alert('工具说明不能为空')
        return
      }

      // 检查文件是否为空
      let files = this.$refs.toolFile.files
      if ((!files || files.length === 0) && (!this.dialog.item.id || this.dialog.item.id === -1)) {
          alert('请选择工具安装文件')
          return
      }

      // 文件不为空，则先上传
      let _this = this
      if (!files || files.length === 0) {
        _this.uploadImgAndAddInternal()
      } else {
        ajax.uploadFile('常用工具', files[0], resp => {
          _this.uploadImgAndAddInternal(resp.data)
        })
      }
    },

    uploadImgAndAddInternal: function (files) {
      // 如果图片不为空，则先上传图片  
        let imgFiles = this.$refs.imgFiles.files
        let _this = this

        if (imgFiles && imgFiles.length !== 0) {
          ajax.batchUpload('常用工具', imgFiles, imgFileInfos => {
            _this.addInternal(files, imgFileInfos.data)
          })
        } else {
          _this.addInternal(files)
        }
    },

    addInternal: function (files, imgFiles) {
      let params = {
          name: this.dialog.item.name,
          desc: this.dialog.item.desc,
          fileInfo: files,
          images: imgFiles
      }

      if (this.dialog.item.id && this.dialog.item.id !== -1) {
          params.id = this.dialog.item.id
      }

      let _this = this
      ajax.jsonPost('/tool', params, function (resp) {
          _this.refreshTable && _this.refreshTable()
          _this.dialog.show = false
      })
    },

    modify: function (item, refreshTable) {
      this.refreshTable = refreshTable
      this.dialog.item = item
      this.dialog.show = true
      this.dialog.title = '修改工具'
      this.dialog.images = item.images
    },
    delete: (item, refreshTable) => {
        if (confirm('确认删除？')) {
            ajax.delete('/tool', {id: item.id}, resp => {
                refreshTable()
            })
        }
    }
  }
}
</script>

<style>
.image-block {
  border: 1px solid #bbb;
  background: #efefef;
}

.image {
  width: 100px; 
  height: 100px; 
  margin: 10px; 
  border: 1px solid #efefef;
  box-shadow: 0px 0px 5px gray;
}
</style>
