package com.xw.config;


import com.xw.customize.authenticationHandler.MyAuthenticationFailureHandler;
import com.xw.customize.authenticationHandler.MyAuthenticationSuccessHandler;
import com.xw.customize.filter.KaptchaFilter;
import com.xw.customize.logoutHandler.MyLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public KaptchaFilter kaptchaFilter() throws Exception {
        KaptchaFilter kaptchaFilter = new KaptchaFilter();
        //指定接收验证码请求参数名
        kaptchaFilter.setKaptcha("kaptcha");
        //指定认证管理器
        kaptchaFilter.setAuthenticationManager(authenticationManagerBean());
        //指定认证成功和失败处理
        kaptchaFilter.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler());
        kaptchaFilter.setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());
        //指定处理登录
        kaptchaFilter.setFilterProcessesUrl("/doLogin");
        kaptchaFilter.setUsernameParameter("uname");
        kaptchaFilter.setPasswordParameter("passwd");
        return kaptchaFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .mvcMatchers("/vc.png").permitAll()
                .mvcMatchers("/login.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .and()
                .logout()
                .logoutUrl("/logout")
              	.invalidateHttpSession(true)
                .clearAuthentication(true)
                // .logoutSuccessUrl("/login.html")
                .logoutSuccessHandler(new MyLogoutSuccessHandler())
                .and()
                .csrf().disable();
        http.addFilterAt(kaptchaFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
