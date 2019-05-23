import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'index',
      component: (resolve) => require(['@/components/Index.vue'], resolve)
    },
    {
      path: '/articles/:forumId/:title',
      name: 'articles',
      component: (resolve) => require(['@/components/article/ArticleExplorer.vue'], resolve)
    },
    {
      path: '/userIndex/:userId/:typeId/:nowPage/:pageSize/:url',
      name: 'userIndex',
      component: (resolve) => require(['@/components/UserIndex.vue'], resolve)
    },
    {
      path: '/topic/myTopics/:topicId',
      name: 'myTopics',
      component: (resolve) => require(['@/components/topic/MyTopic.vue'], resolve)
    },
    {
      path: '/topic/topicExplorer',
      name: 'topicExplorer',
      component: (resolve) => require(['@/components/topic/TopicExplorer.vue'], resolve)
    },
    {
      path: '/tools',
      name: 'tools',
      component: (resolve) => require(['@/components/Tools.vue'], resolve)
    },
    {
      path: '/systemManager',
      name: 'systemManager',
      component: (resolve) => require(['@/components/manager/SystemManager.vue'], resolve),
      children: [
        {
          name: 'systemRoleManager',
          path: 'roleManager',
          component: (resolve) => require(['@/components/manager/RoleManager.vue'], resolve)
        },
        {
          name: 'systemUserManager',
          path: 'userManager',
          component: (resolve) => require(['@/components/manager/UserManager.vue'], resolve)
        },
        {
          name: 'systemArticleManager',
          path: 'articleManager',
          component: (resolve) => require(['@/components/manager/ArticleManager.vue'], resolve)
        },
        {
          name: 'systemArticleTypeManager',
          path: 'articleTypeManager',
          component: (resolve) => require(['@/components/manager/ArticleTypeManager.vue'], resolve)
        },
        {
          name: 'systemTopicManager',
          path: 'topicManager',
          component: (resolve) => require(['@/components/manager/TopicManager.vue'], resolve)
        },
        {
          name: 'systemIndexManager',
          path: 'indexManager',
          component: (resolve) => require(['@/components/manager/IndexManager.vue'], resolve)
        },
        {
          path: '/topic/topicEdit/:topicId',
          name: 'topicEdit',
          component: (resolve) => require(['@/components/manager/TopicEdit.vue'], resolve)
        },
        {
          path: 'toolManager',
          name: 'toolManager',
          component: (resolve) => require(['@/components/manager/ToolManager.vue'], resolve)
        }
      ]
    },
    {
      path: '/about',
      name: 'about',
      component: (resolve) => require(['@/components/About.vue'], resolve)
    },
    {
      path: '/articleDetail/:id',
      name: 'articleDetail',
      component: (resolve) => require(['@/components/article/ArticleDetail.vue'], resolve)
    },
    {
      path: '/newArticle/:articleId',
      name: 'newArticle',
      component: (resolve) => require(['@/components/article/NewArticle.vue'], resolve)
    },
    {
      path: '/login',
      name: 'login',
      component: (resolve) => require(['@/components/Login.vue'], resolve)
    },
    {
      path: '/updatePassword',
      name: 'updatePassword',
      component: (resolve) => require(['@/components/UpdatePassword.vue'], resolve)
    }
  ]
})
