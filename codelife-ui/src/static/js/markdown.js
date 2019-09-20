/**
 * 判断指定行是否是目录
 * 以<h数字>开头且</h数字>结尾的行
 **/
function isCatalog (line) {
  // 至少15个字符才是标题行
  if (line < 15) {
    return false
  }

  // 至少要包含id=""这样的字符才是标题行
  if (line.indexOf('id="') === -1) {
    return false
  }

  return /<h\d.*>.*<\/h\d+>/.test(line)
}

/**
 * 获取标题行的ID及标题内容
 **/
function processCatalog (line) {
  var result = {}

  // 获取标题的层级，主要是h后的数字，如<h6 id="test">...</h6>
  // 取第三位的数字就可以了
  var level = line.substring(2, 3)
  result.level = level

  // 先去掉第一个引号前的内容
  line = line.substring(line.indexOf('"') + 1)

  // 获取剩下的第一个引号前的内容为ID
  result.id = line.substring(0, line.indexOf('"'))

  // 获取>与<之间的内容
  line = />.*</.exec(line)
  line = line.toString()
  line = line.substring(1, line.length - 1)

  result.title = line

  return result
}

/**
 * 处理文章中的表格等，将其转换成HTML中的表格
 * Markdown中表格一般是：|aaa|aaa|这样的方式。
 * 要求一定要以|开头，并且下面一行是|--|---|这种的只含有这两个字符行
 **/
function processHtml (content) {
  // 1. 章节转换 #开头的部分 其中#转换成h1，##转换成h2等；
  var lines = content.split('\n')
  var result = {}
  var tableContent = ''
  var tableLineCount = 0

  var catalogHtml = '' // 存储目录
  var html = ''

  for (var i in lines) {
    var line = lines[i]

    // 如果是目录
    if (isCatalog(line)) {
      // 是目录时生成目录的HTML
      var titleObject = processCatalog(line)
      catalogHtml += '<a href="#' + titleObject.id + '" class="title' + titleObject.level + '">' + titleObject.title + '</a></br>'
    }

    // 如果表格前有空行时，会添加<p>这个标签，需要去掉
    if (line.length >= 4 && line.substring(0, 4) === '<p>|') {
      line = line.substring(3)
    }

    // 判断是否以|开头
    if (line.indexOf('|') !== 0 || line.length === 1) {
      // 不是以|开头，如果上面已经|开头的行了
      if (tableLineCount === 1 || tableLineCount === 2) {
        // 如果当前行不是|开头，上面只有一行或者二行是|开头时，不是表格；
        // 必须要三行以上才是表格
        html += tableContent
      } else if (tableLineCount >= 3) {
        // 上面已经有至少三行是|开头了，将其转换成表格内容
        html += toHtmlTable(tableContent)
      }

      html += line + '\n'

      tableContent = ''
      tableLineCount = 0
    } else {
      // 是以|开头，又分两种情况：一是如果上面有一行，当前行不是|-组成时，不是表格；
      // 二是如果上面有一行，当前行是-|组成时，当成表格处理
      if (tableLineCount === 1 && !expected(line)) {
        // 上一行是|开头，而当前行不是-|组成的，不是表格
        html += tableContent
        html += line + '\n'

        tableLineCount = 0
        tableContent = ''
        continue
      } else {
        tableLineCount++
        tableContent += line + '\n'
      }
    }
  }

  result.html = html
  result.catalog = catalogHtml

  return result
}

// 是否一行是-或者|组成
function expected (line) {
  return /(-|\|)*/.test(line)
}

/**
 *  将Markdown的Table转换成Html的Table
 **/
function toHtmlTable (content) {
  var lines = content.split('\n')
  var result = '<table><thead><tr>'

  for (var i in lines) {
    var line = lines[i]
    line = line.substring(1) // 去掉第一个|
    // 如果最后一个也是|那么也去掉
    if (line.substring(line.length - 1) === '|') {
      line = line.substring(0, line.length - 1)
    }

    if (i === 1) {
      // 第二行不用处理
      continue
    } else if (i === 0) {
      // 第一行处理成标题
      var ths = line.split('|')
      for (var j in ths) {
        if (ths[j] === '') {
          continue
        }

        result += '<th>' + ths[j] + '</th>'
      }
      result += '</tr></thead><tbody>'
    } else {
      result += '<tr>'

      var tds = line.split('|')
      for (var k in tds) {
        if (tds[k] === '') {
          continue
        }

        result += '<td>' + tds[k] + '</td>'
      }

      result += '</tr>'
    }
  }

  result += '</tbody></table>'

  return result
}

export {
  processHtml
}
