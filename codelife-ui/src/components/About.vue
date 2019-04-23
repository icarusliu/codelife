<template>
  <div class="container-fluid m-1 row">
    <div class="col">
        <h1 class="text-primary">关于</h1>
        <hr/>
          <h4>1. 概述</h4>
          这是我的个人网站，使用Java相关技术自己开发完成，主要用于自己在工作与学习中的一些总结，当成个人博客使用。
          <br/>目前包含的主要功能有：
          <ul>
              <li>文章管理功能：编辑、发布、修改、删除，文章编辑使用Markdown语法进行。</li>
              <li>文章分类管理功能</li>
              <li>文章浏览功能</li>
              <li>用户权限管理：登录、退出等</li>
          </ul>
            如评论功能等将逐步开发及上线。另外会不定期的新增新的功能与完善现有功能。以便个人所学技术能有地方总结与进步。
          <br/><br/>
          <h4>2. 后续计划</h4>
          后续会将它完善成一个开源的知识库，目前规划将包含以下功能：
          <br/>
          <img class="m-4" src="/static/icons/web-functions.png">

          <br/>
          <h4>3. 使用技术</h4>
          这个网站主要是使用Java语言开发的。针对其中使用的技术，我将会在后续的博文中进行总结，感兴趣的可以关注相关博文。
          <br/>使用到的技术清单如下：
          <br/><br/>
          <table border="1px" class="desc-table">
              <thead>
                  <tr>
                      <th>分类</th>
                      <th>技术</th>
                      <th>使用方面</th>
                  </tr>
              </thead>

              <tbody>
                  <tr>
                      <td  rowspan="5">后台</td>
                      <td>Java</td>
                      <td>基础开发语言</td>
                  </tr>

                  <tr>
                      <td>Spring/Spring Boot</td>
                      <td>应用框架，部署时也是直接使用Spring Boot打的Jar包直接进行部署</td>
                  </tr>

                  <tr>
                      <td>Spring Security</td>
                      <td>权限管理</td>
                  </tr>

                  <tr>
                      <td>MyBatis</td>
                      <td>用于数据库操作</td>
                  </tr>

                  <tr>
                      <td>MySql</td>
                      <td>数据存储</td>
                  </tr>

                  <tr>
                      <td rowspan="5">前台</td>
                      <td>Thymeleaf</td>
                      <td>用于前台界面的模板化，统一的导航栏、统一的底部区域； 同时每个不同的模块页面的统一布局也通过它的
                      模板进行控制</td>
                  </tr>

                  <tr>
                      <td>Bootstrap</td>
                      <td>页面样式的控制基本是基于Bootstrap来进行的，在其上增加了一些特殊的样式。
                          理论上界面在手机端查看时也会有较好的显示效果。</td>
                  </tr>

                  <tr>
                      <td>Bootstrap Table</td>
                      <td>数据列表的展示主要是使用Bootstrap Table来进行。</td>
                  </tr>

                  <tr>
                      <td>JQuery</td>
                      <td>前后台交互基本通过JQuery来进行</td>
                  </tr>

                  <tr>
                      <td>Markdown语法编辑器</td>
                      <td>使用simpleMDE来提供对编辑文章的Markdown语法支持；同时使用highlight来显示提供代码语法的显示；
                      在这两个基础上自己添加了一些额外的功能如表格的显示、目录的显示等； </td>
                  </tr>
              </tbody>
          </table>

          <br/>
          <h4>4. 其它</h4>
          网站的部署是很简单的，懂一点点技术的可以方便地使用它来部署一个新的个人网站。如果对此感兴趣或者是对这个网站
          有一些问题异或是建议的，可以邮件给我，我的邮箱：icarusliu@163.com。
    </div>
</div>
</template>

<script>
export default {
  name: 'About'
}
</script>

<style>
@import url('../static/css/index.css');
</style>
