package com.liuqi.tools.codelife.config;

import com.liuqi.tools.codelife.config.security.AppVoter;
import com.liuqi.tools.codelife.config.security.RealAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdvice;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import javax.annotation.Resource;

/**
 * @Author: LiuQI
 * @Created: 2018/3/23 19:38
 * @Version: V1.0
 **/
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter implements ResourceServerConfigurer {
    @Autowired
    private RealAuthenticationProvider authenticationProvider;

    @Resource
    private AppVoter appVoter;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("testResource")
                .stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS, "**").permitAll()
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
                .and().cors();
        http.headers().frameOptions().sameOrigin().httpStrictTransportSecurity().disable();
        http.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<AffirmativeBased>() {
            @Override
            public <O extends AffirmativeBased> O postProcess(O object) {
//                object.getDecisionVoters().add(appVoter);
                return object;
            }
        });
    }

    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
}
