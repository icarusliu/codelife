<template>
    <div class="container-fluid pr-0 pl-0">
        <main-nav class="row ml-0 mr-0" style="z-index: 999;"></main-nav>

        <router-view class="row pr-0 ml-1 mr-1"></router-view>
    </div>
</template>

<script>
import MainNav from '@/components/MainNav'

export default {
  data () {
    return {
      isNewArticle: false
    }
  },
  components: {
    MainNav
  },
  created () {
    this.isNewArticle = this.$route.path.indexOf('newArticle') > 0
  }
}
</script>

<style>
@import 'static/js/bootstrap-table/bootstrap-table.min.css';
@import 'static/css/common.css';
@import 'static/css/index.css';
</style>
