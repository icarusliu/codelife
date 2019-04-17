function Praise(id, imgId, countId) {
    this.id = id;
    this.imgId = imgId;
    this.countId = countId;
    this.praiseUrl = "/article/praise";
    this.unpraiseUrl = "/article/unpraise";

    this.init = function() {
        //如果已经点过赞了，点赞按钮变成取消点赞按钮
         if (getOrSet(this.id)) {
            //已经点过赞了，变成红色
            this.reversePraiseImg(true);
         }
    }

    //点赞或者取消点赞
    this.praise = function() {
        var pThis = this;

        if (getOrSet(id)) {
            //已经点过赞了 再点就是取消点赞
            post(this.unpraiseUrl, {id: this.id}, function(data) {
                remove(pThis.id);
                $("#" + pThis.countId).html(pThis.getDispPraiseCount() - 1);
                pThis.reversePraiseImg(false);
            });
        } else {
            //否则是进行点赞
            post(this.praiseUrl, {id: this.id}, function(data) {
                getOrSet(pThis.id, pThis.id);
                $("#" + pThis.countId).html(pThis.getDispPraiseCount() + 1);
                pThis.reversePraiseImg(true);
            });
        }
    }

    //获取显示的点赞数 用于点赞或者取消点赞
    this.getDispPraiseCount = function() {
        var html = $("#" + this.countId).html();
        var praiseCount = parseInt(html);
        return praiseCount;
    }

    //将点赞显示的图标反转
    this.reversePraiseImg = function(hasPraised) {
        if (hasPraised) {
            $("#" + this.imgId).attr("src", "/icons/thumb-up-2x-1.png");
        } else {
            $("#" + this.imgId).attr("src", "/icons/thumb-up-2x.png");
        }
    }
}

//从sessionStorage中设置或者获取数据 如果第二个参数未传则是获取 否则是设置
function getOrSet(id, value) {
    if (value) {
        sessionStorage.setItem(getKey(id), value);
    } else {
        return sessionStorage.getItem(getKey(id));
    }
}

//从SessionStorage中移除数据
function remove(id) {
    sessionStorage.removeItem(getKey(id));
}

//获取关键字
function getKey(id) {
    return "articlePraiseCount-" + id;
}