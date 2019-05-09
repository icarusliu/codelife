package com.liuqi.tools.codelife.timer;

import com.liuqi.tools.codelife.service.ArticleTypeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 主定时器
 *
 * @author LiuQI 2019/5/8 11:12
 * @version V1.0
 **/
@Component
public class MainTimer {
    @Resource
    private ArticleTypeService articleTypeService;

    @Scheduled(cron = "0 0 * * * ?")
    public void updateTypeArticleCounts() {
        articleTypeService.updateArticleCounts();
    }
}
