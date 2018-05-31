package com.liuqi.tools.codelife.service.impl;

import com.liuqi.tools.codelife.db.dao.SystemDao;
import com.liuqi.tools.codelife.exceptions.ExceptionTool;
import com.liuqi.tools.codelife.service.InitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * 初始化服务实现类
 *
 * @author LiuQI 2018/5/29 11:21
 * @version V1.0
 **/
@Service
public class InitServiceImpl implements InitService {
    @Autowired
    private SystemDao systemDao;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void init() throws IOException {
        initDB();
        ExceptionTool.init();
    }

    @Override
    public void initDB() {
        // 判断DB中的表是否存在，如果不存在的话则执行初始化的脚本
        if (1 == systemDao.dbInited()) {
            logger.info("DB has been initialized!");
            return;
        }

        logger.info("DB has not been initialized yet, begin to init it!");

        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:./db-init.sql");

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setContinueOnError(false);
        populator.setSeparator("; ");
        populator.setSqlScriptEncoding("utf-8");
        populator.addScript(resource);

        DatabasePopulatorUtils.execute(populator, dataSource);
    }

    private static final Logger logger = LoggerFactory.getLogger(InitServiceImpl.class);
}
