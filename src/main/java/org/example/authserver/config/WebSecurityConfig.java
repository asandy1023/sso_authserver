package org.example.authserver.config;

import org.example.authserver.config.provider.UserNameAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author JQ
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 授權用戶Service處理類
     */
    @Autowired
    UserDetailsService baseUserDetailService;

    /**
     * 安全路徑過濾
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/fonts/**", "/icon/**", "/images/**", "/favicon.ico");
    }

    /**
     * 放開部分認證授權入口服務的訪問限制
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers("/login", "/oauth/authorize", "/oauth/check_token").and().authorizeRequests()
                .anyRequest().authenticated().and().formLogin().loginPage("/login").failureUrl("/login-error")
                .permitAll();
        http.csrf().disable();
    }

    /**
     * 認證管理配置
     *
     * @param auth
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    /**
     * 授權用戶信息數據庫提供者對象配置
     *
     * @return
     */
    @Bean
    public AbstractUserDetailsAuthenticationProvider daoAuthenticationProvider() {
        UserNameAuthenticationProvider authProvider = new UserNameAuthenticationProvider();
        // 設置userDetailsService
        authProvider.setUserDetailsService(baseUserDetailService);
        // 禁止隱藏用戶未找到異常
        authProvider.setHideUserNotFoundExceptions(false);
        // 使用BCrypt進行密碼的hash
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(6));
        return authProvider;
    }
}
