package com.github.mkopylec.recaptcha.security;

import com.github.mkopylec.recaptcha.security.login.FormLoginConfigurerEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class AccessConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private FormLoginConfigurerEnhancer enhancer;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        enhancer.addRecaptchaSupport(http.formLogin())
                .and()
                .csrf().disable()
                .authorizeRequests().antMatchers("/testSecurity/**").authenticated()
        ;
    }
}
