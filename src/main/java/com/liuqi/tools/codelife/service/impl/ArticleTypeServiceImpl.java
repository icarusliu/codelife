package com.liuqi.tools.codelife.service.impl;

import com.liuqi.tools.codelife.db.dao.ArticleDao;
import com.liuqi.tools.codelife.db.dao.ArticleTypeDao;
import com.liuqi.tools.codelife.entity.ArticleType;
import com.liuqi.tools.codelife.entity.User;
import com.liuqi.tools.codelife.exceptions.RestException;
import com.liuqi.tools.codelife.service.ArticleTypeService;
import com.liuqi.tools.codelife.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: LiuQI
 * @Created: 2018/4/16 12:51
 * @Version: V1.0
 **/
@Service
public class ArticleTypeServiceImpl implements ArticleTypeService {
    @Autowired
    private ArticleTypeDao articleTypeDao;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Override
    public List<ArticleType> findByUser(User user) {
        return articleTypeDao.findByUser(user);
    }
    
    @Override
    public List<ArticleType> findSystemTypes() {
        return articleTypeDao.findAllSystemTypes();
    }
    
    @Override
    public void saveType(Integer id, String name) throws RestException {
        articleTypeDao.rename(id, name);
    }
    
    @Override
    public void saveType(String name) throws RestException {
        //先检查同名的Type是否存在，如果存在则抛出异常
        ArticleType type = articleTypeDao.findByName(name);
        if (null != type) {
            logger.error("Article type with the same name exists already, name: " + name);
            throw new RestException("相同名称的分类已经存在，请确认！");
        }
    
        //不存在时增加
        type = new ArticleType();
        type.setName(name);
        type.setUserId(authenticationService.getLoginUser().getId());
        articleTypeDao.add(type);
    }
    
    @Override
    public ArticleType findById(int id) throws RestException {
        ArticleType type = articleTypeDao.findById(id);
        if (null == type) {
            logger.error("Type does not exist, id: {}!", id);
            throw new RestException("类型不存在，类型ID：" + id);
        }
    
        return type;
    }
    
    @Override
    public void addArticleCount(Integer id) {
        if (null == id) {
            logger.error("Type id is null!");
            return;
        }
        articleTypeDao.addArticleCount(id);
    }
    
    @Override
    public void deduceArticleCount(Integer id) {
        if (null == id) {
            logger.error("Type id is null!");
            return;
        }
        
        articleTypeDao.deduceArticleCount(id);
    }
    
    
    private static final Logger logger = LoggerFactory.getLogger(ArticleTypeServiceImpl.class);
}
