<template>
    <div>
        <!-- <login class="col-sm-2"></login> -->
        <div class="block col-sm">
          <h5 class="row block-title mb-0 mr-0 ml-0">热门文章</h5>
          <article-list class="mr-0 ml-0"  :forumId="-1" :title="title"></article-list>
        </div>
        <div class="col-sm-3 ml-1 mr-0 pl-0 pr-0">
          <div class="block">
              <h5 class="block-title block-title-topic">热门专题</h5>
              <div class="w-100"></div>
              <div class="article-item" v-for="topic in topics" :key="topic.id">
                  <a class="article-item-title font-weight-bold"
                      v-bind:href="'/topic/detail?id=' + topic.id">{{ topic.title }}</a>
                  <p class="article-item-remark">{{ topic.introduction }}</p>
              </div>
          </div>
      </div>
    </div>
</template>

<script>
import ArticleList from '@/components/article/ArticleList'
import { mapState } from 'vuex'
import axios from 'axios'

export default {
  name: 'index',
  components: {
    ArticleList
  },
  computed: mapState([
    'isLogined'
  ]),
  data () {
    return {
      topics: [],
      title: '热门文章'
    }
  },
  created () {
    var that = this
    axios.post('/topic/getForIndex').then(function (resp) {
      var data = resp.data
      that.topics = data.topics
    })
  },
  watch: {
    isLogined (newValue, oldValue) {
      var that = this
      if (newValue && !oldValue) {
        // 登录成功，刷新
        axios.post('/topic/getForIndex').then(function (resp) {
          var data = resp.data
          that.topics = data.topics
        })
      }
    }
  }
}
</script>
