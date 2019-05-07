<!-- 专题浏览 -->
<template>
  <div>
    <div class="col">
      <!--搜索操作-->
      <form method="get" @submit.prevent="search"
            class="row m-4">
        <input type="text"
              placeholder="搜索专题" name="key"
              v-model="searchKey"
              class="form-control col-md-4 mr-md-2 offset-md-3"/>
        <input type="submit" value="搜索"
            class="btn btn-primary"/>
      </form>

      <div class="row">
        <div class="col-sm-3 d-none d-sm-block">
          <div class="block ml-0 mr-0 mb-1" v-if="null != loginUser">
            <h5 class="block-title">订阅专题</h5>
            <div class="col-100"></div>
            <div class="block-content">
              <div class="block-item-a" v-for="topic in myTopics" :key="topic.id">
                <router-link :to="{name: 'myTopics', params: {topicId: topic.id}}">{{topic.title}}</router-link>
              </div>
            </div>
          </div>

          <div class="block ml-0 mr-0">
            <h5 class="block-title">热门专题</h5>
            <div class="col-100"></div>
            <div class="block-content">
              <div class="block-item-a" v-for="topic in hotTopics" :key="topic.id">
                <router-link :to="{name: 'myTopics', params: {topicId: topic.id}}">{{topic.title}}</router-link>
              </div>
            </div>
          </div>
        </div>

        <div class="col-sm">
          <div class="row" v-if="topics === null || topics.length === 0">
            无数据
          </div>
          <div class="row topic-block" v-for="topic in topics" :key="topic.id">
            <router-link class="col-sm-12 topic-block-title" :to="{name: 'myTopics', params: {topicId: topic.id}}">
              {{ topic.title }}
              </router-link>

            <img class="col-sm-3 p-0" v-if="topic.img != ''"
                :src="baseUrl + 'topic/getImg' + '?fileName=' + topic.img"/>

            <img class="col-sm-3 p-0" v-if="topic.img == ''"/>

            <div class="col-sm m-0">
              <p>{{topic.introduction}}</p>
              <ul class="col-sm-12">
                <li v-for="article in topic.articles" :key="article.id">
                  <router-link class="article-item-title font-weight-bold"
                    :to="{name: 'articleDetail', params: {id: article.id}}">{{ article.title }}</router-link>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { mapState } from 'vuex'

export default {
  name: 'topicExplorer',
  data () {
    return {
      topics: [], // 未订阅的专题清单
      myTopics: [], // 我订阅的专题清单
      hotTopics: [], // 热门专题清单
      baseUrl: axios.defaults.baseURL,
      searchKey: ''
    }
  },
  computed: mapState([
    'loginUser'
  ]),
  created () {
    this.refreshData()
  },
  methods: {
    refreshData: function () {
      var that = this

      axios.post('/topic/getExplorerData', {key: this.searchKey}).then(function (resp) {
        that.topics = resp.data.topics
        that.myTopics = resp.data.myTopics
        that.hotTopics = resp.data.hotTopics
      })
    },
    search: function () {
      this.refreshData()
    }
  }
}
</script>

<style>
 .topic-title-main {
    font-size: 1.1em;
    color: gray;
    border-bottom: 1px solid #f2f2f2;
  }

  /*TOPIC清单中的TOPIC块*/
  .topic-block {
    margin-right: 1px;
    margin-bottom: 10px;
    border: 1px solid #fff;
    background: #fbfbfb;
    box-shadow: 2px 2px 8px #ccc;
    padding: 10px;
    font-size: 0.9em;
  }

  .topic-block-title {
    border-bottom: 1px dotted #ccc;
    padding-bottom: 5px;
  }

  .topic-block img {
    box-shadow: 2px 2px 2px #bfbfbf;
    border: 1px solid #efefef;
  }

  .topic-block p {
    color: #483D8B;
  }

  .topic-block ul {
    list-style: circle;
    margin: 0px;
    padding-left: 20px;
  }

  .topic-block ul li{
    margin: 4px;
  }

  .topic-block span{

  }

  /*订阅按钮样式*/
  .topic-subscribe-button {
    font-size: 0.8em;
    cursor: pointer;
    color: blue;
  }

  .topic-subscribe-button:hover{
    color: red;
  }
</style>
