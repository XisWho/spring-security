package com.xw.config;

import com.xw.customize.authenticationSuccessHandler.MyAuthenticationSuccessHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .mvcMatchers("/login.html").permitAll()
                .mvcMatchers("/index").permitAll()  // index请求是公共资源，可以随便访问
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/doLogin")
                .usernameParameter("uname")
                .passwordParameter("passwd")
                // .successForwardUrl("/index")     // 默认使用 `forward `跳转 注意:不会跳转到之前请求路径`
                // .defaultSuccessUrl("/index")    // 默认使用 `redirect` 跳转 注意:如果之前请求路径,会有优先跳转之前请求路径,可以传入第二个参数进行修改`
                .successHandler(new MyAuthenticationSuccessHandler())
                .failureUrl("/login.html")
                .and()
                .csrf().disable();
    }

}
