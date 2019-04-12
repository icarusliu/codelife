package com.liuqi.tools.codelife.config;

import com.liuqi.tools.codelife.config.security.AppVoter;
import com.liuqi.tools.codelife.config.security.RealAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

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
                    .accessDecisionManager(getAccessDecisionManager())
                    .antMatchers("/*", "/css/**/*", "/js/**/*", "/icons/**/*", "/upload/*",
                            "/h2-console/", "/h2-console/**/*", "/topic/**/*").permitAll()
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/iLogin")
                    .permitAll()
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/")
//                .and().sessionManagement().invalidSessionUrl("/timeout")
                .and().csrf().disable();
        http.headers().frameOptions().sameOrigin().httpStrictTransportSecurity().disable();
    }
    
    private AccessDecisionManager getAccessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> voterList = new ArrayList<>();
        voterList.add(new RoleVoter());
        voterList.add(new AuthenticatedVoter());
        voterList.add(new AppVoter());
    
        UnanimousBased affirmativeBased = new UnanimousBased(voterList);
        return affirmativeBased;
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
}
