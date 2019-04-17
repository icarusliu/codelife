<!-- 专题浏览 -->
<template>
  <div class="container-fluid">
    <!--搜索操作-->
    <form method="get" th:action="@{/topic/search}"
          class="row m-4">
        <input type="text"
               placeholder="搜索专题" name="key" required
               class="form-control col-md-4 mr-md-2 offset-md-3"/>
        <input type="submit" value="搜索"
            class="btn btn-primary"/>
    </form>

    <div class="row topic-list" v-if="null != loginUser">
        <!--我订阅的专题-->
        <div class="topic-title-main col-12 mb-2">已订阅专题</div>
        <div class="col-sm-6" v-for="topic in myTopics" :key="topic.id">
            <div class="topic-block row">
                <h6 class="col-sm-12 topic-block-title">
                    <div class="row">
                        <span class="col-sm-10">{{topic.title}}</span>
                        <span class="col-sm-2 text-right topic-subscribe-button"
                              onclick="'unSubscribe(' + ${topic.id} + ')'">取消订阅</span>
                    </div>
                </h6>

                <div class="row">
                    <div class="col-sm">
                        <div class="row">
                          <p class="col-sm">
                            {{topic.introduction}}
                          </p>

                          <ul class="col-sm-12 ml-4">
                              <li v-for="article in topic.articles" :key="article.id">
                                  <a th:href="@{/article/detail} + '?id=' + ${article.id}">{{article.title}}</a>
                              </li>
                          </ul>
                        </div>
                    </div>

                    <div class="col-sm-3 p-2">
                      <img class="topic-block-img"
                         v-bind:src="'/topic/getImg}?fileName=' + topic.img" width="200px" height="200px"/>
                    </div>
                </div>

                <span class="row offset-md-10 col-md-2 text-right">
                  <a th:href="@{/topic/detail} + '?id=' + ${topic.id}">>>查看更多</a></span>
            </div>
        </div>
    </div>

    <div class="row p-1 topic-list">
        <div class="col-12 mb-2 mt-4 topic-title-main">
            专题浏览
        </div>

        <!--专题列表-->
        <div class="col-sm-3" v-for="topic in topics" :key="topic.id">
            <div class="row topic-block">
                <h6 class="col-sm-12 topic-block-title">
                    <div class="row">
                        <span class="col-sm-10">{{topic.title}}</span>
                        <span class="col-sm-2 text-right topic-subscribe-button"
                              v-if="null != loginUser"
                              th:onclick="'subscribe(' + ${topic.id} + ')'">订阅</span>
                    </div>
                </h6>

                <p class="m-0">{{topic.introduction}}</p>

                <ul class="col-12">
                    <li v-for="article in topic.articles" :key="article.id">
                        <a th:href="@{/article/detail} + '?id=' + ${article.id}">{{article.title}}</a>
                    </li>
                </ul>
                <span class="offset-md-9"><a th:href="@{/topic/detail} + '?id=' + ${topic.id}">>>查看更多</a></span>
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
      myTopics: [] // 我订阅的专题清单
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

      axios.post('/topic/getExplorerData').then(function (resp) {
        that.topics = resp.data.topics
        that.myTopics = resp.data.myTopics
      })
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
    box-shadow: 1px 1px 1px #bfbfbf;
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
