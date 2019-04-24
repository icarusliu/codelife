<!--
 使用：
       <my-dialog  @close="showDialog=false"
        v-if="showDialog"
        :submit="addArticle"
        title="test" class="container-fluid">
        <div class="row">
          test
        </div>
      </my-dialog>
-->
<template>
  <div class="dialog" id="mask">
    <div class="row justify-content-center" id="container">
      <div class="content col-sm-4 align-self-center">
        <div class="header row pb-1">
          <b class="col-sm text-left">{{ title }}</b>
        </div>
        <div class="row p-4">
          <slot class="col"></slot>
        </div>
        <div class="row justify-content-end mr-2 mb-2">
          <input type='button' class='btn mr-2' value='关闭' @click="close"/>
          <input type='button' class='btn btn-primary' value='确定' v-on:click="submit"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import $ from 'jquery'

export default {
  props: ['title', 'submit', 'show'],
  data () {
    return {
    }
  },
  watch: {
    show: function (newValue, oldValue) {
      alert(newValue)
    }
  },
  methods: {
    close: function () {
      this.$emit('close')
    },
    init: function () {
      let height = $('#container').height()
      let maskHeight = $('#mask').height()
      $('#container').css('margin-top', (maskHeight - height) / 2)
    }
  },
  mounted () {
    this.init()
  }
}
</script>

<style>
.dialog {
  background-color: rgba(0, 0, 0, .5);
  transition: opacity .3s ease;
  display: table;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 9998;
}

.content {
  background: #fff;
  border: 1px solid #bbb;
  box-shadow: 0px 0px 8px #fff;
}

.header {
  border-bottom: 1px solid #bbb;
  line-height: 2em;
}
</style>
