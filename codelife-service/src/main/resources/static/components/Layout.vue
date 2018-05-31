<template>
    <div id="root">
        <nav class="navbar navbar-toggleable-md sticky-top navbar-light mb-2" id="mainNav">
            <a class="navbar-brand" href="#"></a>
            <!-- 小页面上时导航栏按下拉的方式展现 -->
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarsExampleDefault"
                    aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarsExampleDefault">
                <ul class="navbar-nav mr-auto" id="mainNavUl">
                    <li class="nav-item text-center">
                        <a class="nav-link text-center font-weight-bold">首页<span class="sr-only"></span></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-center font-weight-bold">文章浏览<span class="sr-only"></span></a>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link text-center font-weight-bold">个人主页</a>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link text-center font-weight-bold">我的专题<span class="sr-only"></span></a>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link text-center font-weight-bold">专题浏览</a>
                    </li>

                    <!-- 只有管理员才能看到系统管理界面 -->
                    <li class="nav-item">
                        <a class="nav-link text-center font-weight-bold">系统管理</a>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link text-center font-weight-bold">关于</a>
                    </li>
                </ul>
                <form class="form-inline my-2 my-lg-0">
                    <div style="font-size: 0.8em; ">
                        登录用户：<font class="text-primary mr-4"></font>
                        <a th:href="#">注销</a>
                    </div>
                </form>
            </div>
        </nav>
    </div>
</template>