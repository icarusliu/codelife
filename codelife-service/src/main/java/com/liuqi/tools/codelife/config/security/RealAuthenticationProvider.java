package com.liuqi.tools.codelife.config.security;

import com.liuqi.tools.codelife.db.entity.User;
import com.liuqi.tools.codelife.db.entity.UserStatus;
import com.liuqi.tools.codelife.util.exceptions.RestException;
import com.liuqi.tools.codelife.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author: LiuQI
 * @Created: 2018/3/23 19:54
 * @Version: V1.0
 **/
@Component
public class RealAuthenticationProvider implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(RealAuthenticationProvider.class);
    
    @Autowired
    private UserService userService;
    
    @Value("${app.user.maxErrorCount}")
    private String maxErrorCount;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        Optional<Object> password = Optional.of(authentication.getCredentials());
        User user;
        
        //根据用户名查找用户
        try {
            user = userService.findByUsername(username);
        } catch (RestException e) {
            logger.error("Find user by username failed, user does not exist, username: {}!", username, e);
            throw new AuthenticationException(e.getMessage(), e){};
        }
    
        //判断用户是否被锁定或者注销，如果是则不让登录
        //需要在验证密码的前面完成，否则如下场景下会出异常：用户状态是注销时，密码输入超过3次错误被锁定，而第二天被重新置成正常！
        if (UserStatus.LOCKED.equals(user.getStatus())) {
            logger.error("User has been locked!");
            throw new AuthenticationException("用户被锁定，第二天会自动解锁，请谅解！"){};
        } else if (UserStatus.CANCEL.equals(user.getStatus())) {
            logger.error("User has been canceled!");
            throw new AuthenticationException("用户被注销，请联系管理员！"){};
        }
        
        //判断密码是否正确
        if (!user.getPassword().equals(password.orElse(""))) {
            logger.error("Password is wrong!");
            String errorMessage;
    
            //判断出错次数是否超过限制
            if (Integer.valueOf(maxErrorCount) - 1 == user.getErrorCount()) {
                //包括这次已经达到最大出错次数
                logger.error("Password wrong count has reached the max times({}), user({}) will be locked!"
                        , maxErrorCount, user.getUsername());
    
                errorMessage = "密码错误，已连续错误" + maxErrorCount + "次，用户被锁定，请联系管理员或者第二天再试！";
            } else {
                errorMessage = "密码错误，剩余尝试次数：" + (Integer.valueOf(maxErrorCount) - 1 - user.getErrorCount());
            }
    
            //更新出错次数
            userService.updateErrorCount(user, user.getErrorCount() + 1);
    
            throw new AuthenticationException(errorMessage) {
            };
        }
        
        //密码正确时修改出错次数
        if (0 != user.getErrorCount()) {
            userService.updateErrorCount(user, 0);
        }
        
        RealUserDetails userDetails = new RealUserDetails(user);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password.orElse("").toString(),
                userDetails.getAuthorities());
        
        token.setDetails(userDetails);
        
        //获取用户登录IP
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        userDetails.setRemoteIp(details.getRemoteAddress());
        
        return token;
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
