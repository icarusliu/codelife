//绑定Form的提交事件，不使用Form的默认提交功能
$.extend({
    'formSubmit': function(formId, callback) {
        $("#" + formId).bind('submit', function() {
            callback();
            return false;
        });
    }
});

/*获取页面请求参数*/
$.extend({
    'getUrlVars': function(href){
        if (undefined == href) {
            href = window.location.href;
        }

        var vars = [], hash;
        var hashes = href.slice(href.indexOf('?') + 1).split('&');
        for(var i = 0; i < hashes.length; i++)
        {
         hash = hashes[i].split('=');
         vars.push(hash[0]);
         vars[hash[0]] = hash[1];
        }
        return vars;
    },
    'getUrlVar': function(name, url){
        return $.getUrlVars(url)[name];
    }
});

/*获取页面名称*/
$.extend({
    'getUrlPage': function(href) {
        if (undefined == href) {
            href = window.location.href;
        }

        //去掉前缀http://
        if (href.startsWith('http://')) {
            href = href.substring(7);
        }

        //获取第一个/后面到参数前面的串，包含/
        //去掉参数
        var varIndex = href.indexOf('?');
        if (-1 != varIndex) {
            href = href.substring(0, varIndex);
        }
        return href.substring(href.indexOf('/'));
    }
});

/**
 * 提交POST请求
 **/
function post(url, data, success, options) {
    var obj = new Object();
    obj.url = url;
    obj.data = data;
    obj.success = function(data) {
        ajaxSuccess(data, success);
    }
    obj.error = ajaxError;

    if (options) {
        for (var i in options) {
            obj[i] = options[i];
        }
    }

    $.post(obj);
}

//POST提交FormData
function postFormData(url, formData, success) {
    $.post({
        url: url,
        data: formData,
        processData: false,
        contentType: false,
        success: function(data) {
            ajaxSuccess(data, success);
        },
        error: ajaxError
    });
}

//Ajax请求失败时回调处理
function ajaxError(resp) {
    var status = resp.status;
    alert("操作失败，错误码：" + status);
}

//Ajax请求成功时回调处理
function ajaxSuccess(data, success) {
    var errorMessage = data.errorMessage;

    if (undefined != errorMessage && null != errorMessage) {
        alert("操作失败，错误信息：" + errorMessage);
    } else if ("timeout" == data) {
        alert("登录超时，请重新登录后再操作！");
        window.location.replace("/index?lastUrl=" + window.location.href);
    } else if (undefined != success) {
        success(data);
    }
}


//数据表格的使用
function DataTable(tableId, dataUrl, toolbarId) {
    this.tableId = tableId;
    this.dataUrl = dataUrl;
    this.toolbarId = toolbarId;

    //初始化表格
    $("#" + tableId).bootstrapTable({
        url: dataUrl,
        search: true,
        smartDisplay: true,
        rowStyle: rowStyle,
        toolbar: "#" + toolbarId
    });

    //刷新表格
    this.refresh = function() {
        $("#" + tableId).bootstrapTable("refresh");
    };

    //获取表格中的数据
    this.getData = function() {
        return $("#" + tableId).bootstrapTable("getData");
    };
}

//表格的样式
function rowStyle(row, index) {
    return {
         classes: 'text-primary another-class',
         css: {"color": "blue", "font-size": "1em"}
    };
}
