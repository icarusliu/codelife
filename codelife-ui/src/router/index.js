import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/components/Index'
import ArticleExplorer from '@/components/article/ArticleExplorer'
import UserIndex from '@/components/UserIndex'
import MyTopic from '@/components/topic/MyTopic'
import TopicExplorer from '@/components/topic/TopicExplorer'
import About from '@/components/About'
import SystemManager from '@/components/manager/SystemManager'
import SystemRoleManager from '@/components/manager/RoleManager'
import SystemUserManager from '@/components/manager/UserManager'
import SystemArticleManager from '@/components/manager/ArticleManager'
import SystemArticleTypeManager from '@/components/manager/ArticleTypeManager'
import SystemTopicManager from '@/components/manager/TopicManager'
import TopicEdit from '@/components/manager/TopicEdit'
import SystemIndexManager from '@/components/manager/IndexManager'
import ArticleDetail from '@/components/article/ArticleDetail'
import NewArticle from '@/components/article/NewArticle'
import Login from '@/components/Login'
import ToolManager from '@/components/manager/ToolManager'
import Tools from '@/components/Tools'
import UpdatePassword from '@/components/UpdatePassword'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'index',
      component: Index
    },
    {
      path: '/articles/:forumId/:title',
      name: 'articles',
      component: ArticleExplorer
    },
    {
      path: '/userIndex/:userId/:typeId/:nowPage/:pageSize/:url',
      name: 'userIndex',
      component: UserIndex
    },
    {
      path: '/topic/myTopics/:topicId',
      name: 'myTopics',
      component: MyTopic
    },
    {
      path: '/topic/topicExplorer',
      name: 'topicExplorer',
      component: TopicExplorer
    },
    {
      path: '/tools',
      name: 'tools',
      component: Tools
    },
    {
      path: '/systemManager',
      name: 'systemManager',
      component: SystemManager,
      children: [
        {
          name: 'systemRoleManager',
          path: 'roleManager',
          component: SystemRoleManager
        },
        {
          name: 'systemUserManager',
          path: 'userManager',
          component: SystemUserManager
        },
        {
          name: 'systemArticleManager',
          path: 'articleManager',
          component: SystemArticleManager
        },
        {
          name: 'systemArticleTypeManager',
          path: 'articleTypeManager',
          component: SystemArticleTypeManager
        },
        {
          name: 'systemTopicManager',
          path: 'topicManager',
          component: SystemTopicManager
        },
        {
          name: 'systemIndexManager',
          path: 'indexManager',
          component: SystemIndexManager
        },
        {
          path: '/topic/topicEdit/:topicId',
          name: 'topicEdit',
          component: TopicEdit
        },
        {
          path: 'toolManager',
          name: 'toolManager',
          component: ToolManager
        }
      ]
    },
    {
      path: '/about',
      name: 'about',
      component: About
    },
    {
      path: '/articleDetail/:id',
      name: 'articleDetail',
      component: ArticleDetail
    },
    {
      path: '/newArticle/:articleId',
      name: 'newArticle',
      component: NewArticle
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/updatePassword',
      name: 'updatePassword',
      component: UpdatePassword
    }
  ]
})
