<template>
  <div>
      <!-- 左侧导航页面 -->
      <div class="col-sm-3 col-md-2 pr-0 pl-0 mr-1 bg-faded sidebar">
          <div class="list-group" id="navUl" >
              <router-link class="list-group-item"
                :to="{name: 'systemRoleManager'}"
                  v-if="null != loginUser && loginUser.systemAdmin">角色管理</router-link>
              <router-link class="list-group-item"
                :to="{name: 'systemUserManager'}"
                  v-if="null != loginUser && loginUser.systemAdmin">用户管理</router-link>
              <router-link class="list-group-item"
                :to="{name: 'systemArticleManager'}"
                  v-if="null != loginUser">文章管理</router-link>
              <router-link class="list-group-item"
                :to="{name: 'systemArticleTypeManager'}"
                  v-if="null != loginUser">文章分类管理</router-link>
              <router-link class="list-group-item"
                :to="{name: 'systemTopicManager'}"
                  v-if="null != loginUser && (loginUser.systemAdmin || loginUser.topicAdmin)">专题管理</router-link>
              <router-link class="list-group-item"
                :to="{name: 'toolManager'}"
                  v-if="null != loginUser && loginUser.systemAdmin">工具管理</router-link>
              <router-link class="list-group-item"
                :to="{name: 'systemIndexManager'}"
                  v-if="null != loginUser && loginUser.systemAdmin">首页管理</router-link>
          </div>
      </div>

      <!-- 右侧文章列表 -->
      <div class="col-sm col-md right-content">
        <router-view></router-view>
      </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'systemManager',
  computed: mapState([
    'loginUser'
  ])
}
</script>

<style>

</style>
