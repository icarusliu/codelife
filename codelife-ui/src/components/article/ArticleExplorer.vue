<!-- 文章浏览界面 -->
<template>
  <div>
     <div class="col-sm-3 col-md-2 hidden-xs-down sidebar pl-0 pr-2">
        <div class="side-block container" id="navUl" >
            <span class="side-block-title row">文章版块</span>
            <router-link :to="{name: 'articles', params: {forumId: forum.id, title: forum.name}}"
              class="side-block-item row"
              v-for="forum in forums" :key="forum.id">
              {{ forum.name + '(' + forum.articleCount + ')' }}
            </router-link>
        </div>
    </div>

    <!-- 右侧文章列表 -->
    <div class="right-content col-sm mr-0">
            <div class="right-content-toolbar row text-right m-0">
                <router-link :to="{name: 'articles', params: {forumId: -1, title: '所有文章'}}">所有文章</router-link>
                <label v-if="-1 != forumId">&nbsp;&nbsp;>&nbsp;&nbsp;</label>
                <label v-if="-1 != forumId" class="right-content-title">{{title}}</label>
            </div>

            <article-list :forumId="forumId">
            </article-list>
        </div>
  </div>
</template>

<script>
import ArticleList from '@/components/article/ArticleList'
import axios from 'axios'

export default {
  name: 'ArticleExplorer',
  components: {
    ArticleList
  },
  data () {
    return {
      forums: [],
      articleCount: 0,
      forumId: -1,
      title: '所有文章'
    }
  },
  watch: {
    '$route.params': function (newValue, oldValue) {
      this.forumId = newValue.forumId
      this.title = newValue.title
    }
  },
  created () {
    this.refreshData()
  },
  methods: {
    refreshData () {
      var that = this
      axios.post('/articleType/getForums').then((resp) => {
        that.forums = resp.data
        that.articleCount = 0
        for (var i in resp.data) {
          that.articleCount += resp.data[i].articleCount
        }
      })
    }
  }
}
</script>
