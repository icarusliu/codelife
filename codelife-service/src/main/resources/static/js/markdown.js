//代码高亮功能
hljs.initHighlightingOnLoad();

var converter = new showdown.Converter({extensions: function() {
  function htmlunencode(text) {
    return (
      text
        .replace(/&amp;/g, '&')
        .replace(/&lt;/g, '<')
        .replace(/&gt;/g, '>')
      );
  }
  return [
    {
      type: 'output',
      filter: function (text, converter, options) {
        // use new shodown's regexp engine to conditionally parse codeblocks
        var left  = '<pre><code\\b[^>]*>',
            right = '</code></pre>',
            flags = 'g',
            replacement = function (wholeMatch, match, left, right) {
              // unescape match to prevent double escaping
              match = htmlunencode(match);
              return left + hljs.highlightAuto(match).value + right;
            };
        return showdown.helper.replaceRecursiveRegExp(text, replacement, left, right, flags);
      }
    }
  ];
}()});

/**
 * 判断指定行是否是目录
 * 以<h数字>开头且</h数字>结尾的行
 **/
function isCatalog(line) {
    //至少15个字符才是标题行
    if (15 > line) {
        return false;
    }

    //至少要包含id=""这样的字符才是标题行
    if (-1 == line.indexOf('id="')) {
        return false;
    }

    return /<h\d.*>.*<\/h\d+>/.test(line);
}

/**
 * 获取标题行的ID及标题内容
 **/
function processCatalog(line) {
    var result = new Object();

    //获取标题的层级，主要是h后的数字，如<h6 id="test">...</h6>
    //取第三位的数字就可以了
    var level = line.substring(2, 3);
    result.level = level;

    //先去掉第一个引号前的内容
    line = line.substring(line.indexOf('"') + 1);

    //获取剩下的第一个引号前的内容为ID
    result.id = line.substring(0, line.indexOf('"'));

    //获取>与<之间的内容
    line = />.*</.exec(line);
    line = line.toString();
    line = line.substring(1, line.length - 1);

    result.title = line;

    return result;
}

/**
 * 处理文章中的表格等，将其转换成HTML中的表格
 * Markdown中表格一般是：|aaa|aaa|这样的方式。
 * 要求一定要以|开头，并且下面一行是|--|---|这种的只含有这两个字符行
 **/
function processHtml(content) {
    //1. 章节转换 #开头的部分 其中#转换成h1，##转换成h2等；
    var lines = content.split("\n");
    var result = new Object();
    var tableContent = "";
    var tableLineCount = 0;

    var catalogHtml = ""; //存储目录
    var html = "";

    for (var i in lines) {
        var line = lines[i];

        //如果是目录
        if (isCatalog(line)) {
            //是目录时生成目录的HTML
            var titleObject = processCatalog(line);
            for (var i = 1; i < titleObject.level; i++) {
                catalogHtml += "&nbsp; &nbsp; ";
            }
            catalogHtml += "<a href='#" + titleObject.id + "'>" + titleObject.title + "</a></br>";
        }

        //如果表格前有空行时，会添加<p>这个标签，需要去掉
        if (4 <= line.length && "<p>|" == line.substring(0, 4)) {
            line = line.substring(3);
        }

        //判断是否以|开头
        if (0 != line.indexOf("|") || 1 == line.length) {
            //不是以|开头，如果上面已经|开头的行了
            if (1 == tableLineCount || 2 == tableLineCount) {
                //如果当前行不是|开头，上面只有一行或者二行是|开头时，不是表格；
                //必须要三行以上才是表格
                html += tableContent;
            } else if (3 <= tableLineCount) {
                //上面已经有至少三行是|开头了，将其转换成表格内容
                html += toHtmlTable(tableContent);
            }

            html += line + "\n";

            tableContent = "";
            tableLineCount = 0;
        } else {
            //是以|开头，又分两种情况：一是如果上面有一行，当前行不是|-组成时，不是表格；
            //二是如果上面有一行，当前行是-|组成时，当成表格处理
            if (1 == tableLineCount && !expected(line)){
                //上一行是|开头，而当前行不是-|组成的，不是表格
                html += tableContent;
                html += line + "\n";

                tableLineCount = 0;
                tableContent = "";
                continue;
            } else {
                tableLineCount++;
                tableContent += line + "\n";
            }
        }
    }

    result.html = html;
    result.catalog = catalogHtml;

    return result;
}

//是否一行是-或者|组成
function expected(line) {
    return /(-|\|)*/.test(line);
}

/**
 *  将Markdown的Table转换成Html的Table
 **/
function toHtmlTable(content) {
    var lines = content.split("\n");
    var result = "<table><thead><tr>";

    for (var i in lines) {
        var line = lines[i];
        line = line.substring(1); //去掉第一个|
        //如果最后一个也是|那么也去掉
        if ("|" == line.substring(line.length - 1)) {
            line = line.substring(0, line.length - 1);
        }

        if (1 == i) {
            //第二行不用处理
            continue;
        } else if (0 == i) {
            //第一行处理成标题
            var ths = line.split("|");
            for (var j in ths) {
                if ("" == ths[j]) {
                    continue;
                }

                result += "<th>" + ths[j] + "</th>";
            }
            result += "</tr></thead><tbody>";
        } else {
            result += "<tr>";

            var tds = line.split("|");
            for (var k in tds) {
                if ("" == tds[k]) {
                    continue;
                }

                result += "<td>" + tds[k] + "</td>";
            }

            result += "</tr>";
        }
    }

    result += "</tbody></table>";

    return result;
}

/**
 * Markdown风格的代码转换成HTML
 **/
function mdToHtml(content) {
    var result = new Object();
    var html = converter.makeHtml(content);

    //这样处理后还存在以下问题，一是表格未处理；二是目录未解析； 在以下函数中处理
    result = processHtml(html);

    return result;
}