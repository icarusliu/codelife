//分页工具栏
function Pager(ulId, url, params, nowPage, pages, pageSize) {
    this.ulId = ulId;
    this.url = url;
    this.params = params; 
    this.nowPage = (undefined == nowPage || null == nowPage) ? 1 : nowPage;
    this.pages = pages;
    this.pageSize = (undefined == pageSize || null == pageSize) ? 20 : pageSize;

    { //初始化
        $("#" + ulId).html("");
        var html = "<li class='" + (1 == this.nowPage ? "page-item disabled" : "page-item")
            + "' id='firstPage'><route-link class='page-link' :to='" + getUrl (url, params, 1, pageSize) + "'>首页</route-link>";
        html += "<li class='" + (1 == this.nowPage ? "page-item disabled" : "page-item")
            + "' id='lastPage'><route-link class='page-link' :to='" 
            + getUrl(url, params, this.nowPage - 1, pageSize) 
            + "'>上一页</route-link>";
        html += "<li class='" + (this.nowPage == this.pages || 0 >= this.pages ? "page-item disabled" : "page-item")
            + "' id='nextPage'><route-link class='page-link' :to='"
            + getUrl(url, params, this.nowPage + 1, pageSize) 
            + "'>下一页</route-link>";

        $("#" + this.ulId).html(html);
    }
}

function getUrl(url, params, nowPage, pageSize) {
    var retUrl = "{name: '" + url + "', params: {nowPage: " + nowPage + ", pageSize: " + pageSize;  
    for (var i in params) {
        var param = params[i]; 
        retUrl += ", " + i + ": '" + param + "'";
    }

    retUrl += "}}"; 

    return retUrl; 
}