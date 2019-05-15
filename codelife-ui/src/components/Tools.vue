<template>
    <div>
        <div class="col">
          <div class="row tool-item"
            v-for="tool in tools" :key="tool.id">
            <h5 class="col d-block">{{tool.name}}</h5>
            <div class="w-100"></div>
            <div class="col-4 images">
              <img class="image" v-if="null != tool.images && 0 != tool.images.length"
                :src="baseUrl + 'file/download/' + tool.images[0].id"/>
            </div>
            <div class="col d-block tool-info">
              <p class="row">{{tool.desc}}</p>
              <span class="row">上传时间：<label>{{tool.updateTime}}</label></span>
              <span class="row">下载次数：<label>{{tool.downloadCount}}</label></span>
              <span class="row">
                <a class="my-btn my-btn-gray" 
                  v-if="null != tool.fileInfo"
                  :href="baseUrl + 'file/download/' + tool.fileInfo.id">下载</a>
              </span>
            </div>
          </div>
        </div>
    </div>
</template>

<script>
import ajax from '@/components/common/Ajax'
import axios from 'axios'
import { mapState } from 'vuex'

export default {
  name: 'tools',
  data () {
    return {
      tools: [],
      baseUrl: axios.defaults.baseURL
    }
  },
  computed: mapState([
    'isLogined'
  ]),
  created () {
    this.refreshData()
  },
  methods: {
    refreshData: function () {
      let _this = this
      ajax.get('/tool', {}, resp => {
        _this.tools = resp
      })
    }
  }
}
</script>

<style>
.tool-item {
  border: 1px solid #ccc;
  background: #fff;
  margin-bottom: 4px;
  box-shadow: 2px 2px 5px #bbb;

  padding-bottom: 10px;
}

.tool-item>h5 {
  border-bottom: 1px solid #eee;

  height: 40px; 
  line-height: 40px;
  font-size: 20px;
}

.tool-info>span {
  line-height: 30px; 
  height: 30px;
  margin-bottom: 4px; 
  font-size: 14px;
}

.tool-info>span>label {
  color: blue;
}

.images {
  margin: 0px 10px;
  padding: 0px;
  box-shadow: 0px 0px 3px gray;
}

.image {
  width: 100%; 
  height: 200px;
}
</style>
