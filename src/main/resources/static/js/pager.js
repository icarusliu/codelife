//分页工具栏
function Pager(ulId, url, nowPage, pages, pageSize) {
    this.ulId = ulId;
    this.url = url;
    this.nowPage = (undefined == nowPage || null == nowPage) ? 1 : nowPage;
    this.pages = pages;
    this.pageSize = (undefined == pageSize || null == pageSize) ? 20 : pageSize;

    { //初始化
        this.url += (-1 == this.url.indexOf("?")) ? "?" : "&";
        this.url += "pageSize=" + this.pageSize;

        $("#" + ulId).html("");
        var html = "<li class='" + (1 == this.nowPage ? "page-item disabled" : "page-item")
            + "' id='firstPage'><a class='page-link' href="
            + this.url + ">首页</a>";
        html += "<li class='" + (1 == this.nowPage ? "page-item disabled" : "page-item")
            + "' id='lastPage'><a class='page-link' href="
            + this.url + "&nowPage=" + (this.nowPage - 1)  + ">上一页</a>";
        html += "<li class='" + (this.nowPage == this.pages || 0 >= this.pages ? "page-item disabled" : "page-item")
            + "' id='nextPage'><a class='page-link' href="
            + this.url + "&nowPage=" + (this.nowPage + 1)  + ">下一页</a>";

        $("#" + this.ulId).html(html);
    }
}