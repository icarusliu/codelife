package com.liuqi.tools.codelife.util;/**
 * Created by icaru on 2019/3/4.
 */

import com.liuqi.tools.codelife.db.entity.Article;
import com.liuqi.tools.codelife.util.exceptions.RestException;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2019/3/4 22:38
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2019/3/4 22:38
 **/
public class ArticleUtils {
    /**
     * 设置文章的内容与概要
     *
     * @param article
     * @param content
     * @param contentFilePath
     * @throws RestException
     */
    public static void setRemark(Article article, String content) throws RestException {
        String html = FileUtils.htmlToText(content);
        article.setRemark(html.substring(0, Math.min(200, html.length())));
    }
}
