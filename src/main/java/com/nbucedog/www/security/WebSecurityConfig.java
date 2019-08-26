package com.nbucedog.www.security;

import com.nbucedog.www.security.login.FailureAuthHandler;
import com.nbucedog.www.security.login.SuccessAuthHandler;
import com.nbucedog.www.security.logout.SuccessLogoutHandler;
import com.nbucedog.www.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsUtils;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private FailureAuthHandler failureAuthHandler;
    @Autowired
    private SuccessAuthHandler successAuthHandler;
    @Autowired
    private SuccessLogoutHandler successLogoutHandler;
    @Autowired
    private AuthAccessDeniedHandler authAccessDeniedHandler;
    @Autowired
    private UserService userService;
    @Autowired
    private DataSource dataSource;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean()throws Exception{
        return super.authenticationManagerBean();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
//        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)throws Exception{
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }
    @Override
    protected void configure(HttpSecurity http)throws Exception{
        http.headers().xssProtection().disable()
                .and()
                .formLogin()
                .loginPage("/noAuth")
                .loginProcessingUrl("/login")
                .failureHandler(failureAuthHandler)
                .successHandler(successAuthHandler)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(successLogoutHandler)
                .and()
                .cors()
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/noAuth","/login").permitAll()
//                .anyRequest().authenticated()
                .and()
                .rememberMe()
//                .useSecureCookie(false)
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60*60*24)
                .userDetailsService(userService)
                .and()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .disable()
                .exceptionHandling()
                .accessDeniedHandler(authAccessDeniedHandler)
        ;
    }
//    @Override
//    public void configure(WebSecurity web)throws Exception{
//
//    }
}
