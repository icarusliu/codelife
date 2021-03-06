package com.liuqi.tools.codelife.config;

import com.liuqi.tools.codelife.config.security.RealAuthenticationProvider;
import com.liuqi.tools.codelife.util.MapBuilder;
import com.liuqi.tools.codelife.util.exceptions.RealAuthenticationException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;

import javax.annotation.Resource;

/**
 * .
 *
 * @author LiuQI 2019/4/28 16:14
 * @version V1.0
 **/
@Configuration
public class AuthenticationConfiguration extends AuthorizationServerConfigurerAdapter {
    @Resource
    private RealAuthenticationProvider realAuthenticationProvider;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("codelife")
                .resourceIds("testResource")
                .authorizedGrantTypes("password")
                .authorities("ROLE_CLIENT")
                .scopes("read", "write")
                .secret("secret")
                .redirectUris("http://localhost:8080");
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("hasAuthority('ROLE_CLIENT')")
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 后端接口默认添加/api前缀，以方便在前后端分离模式下nginx区分前后端请求
        endpoints.pathMapping("/oauth/token", "/api/oauth/token");

        // 登录时的异常特殊处理
        endpoints.exceptionTranslator(new DefaultWebResponseExceptionTranslator() {
            @Override
            public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
                if (e instanceof RealAuthenticationException) {
                    return new ResponseEntity<>(new OAuth2Exception(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
                }

                return super.translate(e);
            }
        });
        endpoints.authenticationManager(authentication -> realAuthenticationProvider.authenticate(authentication));
    }

}
