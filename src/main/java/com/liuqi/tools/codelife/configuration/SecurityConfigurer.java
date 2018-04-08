package com.liuqi.tools.codelife.configuration;

import com.liuqi.tools.codelife.configuration.security.RealAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

/**
 * @Author: LiuQI
 * @Created: 2018/3/23 19:38
 * @Version: V1.0
 **/
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Autowired
    private RealAuthenticationProvider authenticationProvider;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/*", "/css/**/*", "/js/**/*", "/icons/**/*",
                            "/h2-console/", "/h2-console/**/*", "/topic/**/*").permitAll()
                    //系统管理界面与分类管理目前只能由ADMIN访问
                    .antMatchers("/system/**/*").hasAuthority("ADMIN")
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/index")
                    .loginProcessingUrl("/login")
                    .permitAll()
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/")
//                .and().sessionManagement().invalidSessionUrl("/timeout")
                .and().csrf().disable();
    
        http.headers().frameOptions().sameOrigin().httpStrictTransportSecurity().disable();
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
}
