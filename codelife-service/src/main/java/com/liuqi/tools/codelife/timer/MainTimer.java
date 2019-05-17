package com.liuqi.tools.codelife.timer;

import com.liuqi.tools.codelife.service.ArticleTypeService;
import com.liuqi.tools.codelife.service.UserService;
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
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Resource
    private ArticleTypeService articleTypeService;

    @Resource
    private UserService userService;

    @Scheduled(cron = "0 0 * * * ?")
    public void updateTypeArticleCounts() {
        articleTypeService.updateArticleCounts();
    }

    @Scheduled(initialDelay = 2000, fixedDelay = 3600000)
    public void restUserErrorCount() {
        logger.info("开始更新用户密码错误次数");
        userService.resetErrorCount();
    }
}
