<!-- 分页组件 -->
<template>
  <div>
    <ul class="pagination">
      <li v-bind:class="firstPage ? 'disabled page-item' : 'page-item'" id="firstPage">
        <router-link class="page-link" :to="firstPageUrl">第一页</router-link>
      </li>
      <li v-bind:class="firstPage ? 'disabled page-item' : 'page-item'" id="lastPage">
        <router-link class="page-link" :to="lastPageUrl">上一页</router-link>
      </li>
      <li v-bind:class="lastPage ? 'disabled page-item' : 'page-item'" id="nextPage">
        <router-link class="page-link" :to="nextPageUrl">下一页</router-link>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  props: [
    'nowPage',
    'pageSize',
    'pages',
    'url',
    'params'
  ],
  data () {
    return {
      firstPageUrl: {},
      lastPageUrl: {},
      nextPageUrl: {},
      firstPage: false,
      lastPage: false
    }
  },
  watch: {
    '$route.params.nowPage': function (newValue) {
      this.nowPage = newValue
      this.setPageUrls()
    }
  },
  created () {
    this.setPageUrls()
  },
  methods: {
    setPageUrls: function () {
      this.firstPageUrl = this.getUrlParams(1)
      this.lastPageUrl = this.getUrlParams(this.nowPage - 1)
      this.nextPageUrl = this.getUrlParams(Number(this.nowPage) + 1)

      this.lastPage = (this.nowPage === this.pages)
      this.firstPage = (this.nowPage === 1)
    },
    getUrlParams: function (newNowPage) {
      var urlParams = Object()
      urlParams.nowPage = newNowPage
      urlParams.pageSize = this.pageSize
      urlParams.url = this.url
      for (var i in this.params) {
        urlParams[i] = this.params[i]
      }

      var retObj = Object()
      retObj.name = this.url
      retObj.params = urlParams

      return retObj
    }
  }
}
</script>
