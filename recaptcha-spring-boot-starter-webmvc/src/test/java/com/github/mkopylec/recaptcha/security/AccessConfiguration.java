package com.github.mkopylec.recaptcha.security;

import com.github.mkopylec.recaptcha.security.login.FormLoginConfigurerEnhancer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class AccessConfiguration extends WebSecurityConfigurerAdapter {

    private final FormLoginConfigurerEnhancer enhancer;

    public AccessConfiguration(FormLoginConfigurerEnhancer enhancer) {
        this.enhancer = enhancer;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        enhancer.addRecaptchaSupport(http.formLogin())
                .and()
                .csrf().disable()
                .authorizeRequests().antMatchers("/testSecurity/**").authenticated();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/testValidation/**", "/testTesting/**");
    }
}
