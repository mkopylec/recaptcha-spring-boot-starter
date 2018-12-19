package com.github.mkopylec.recaptcha.webmvc.security;

public class AuthenticationParameters {

    protected final String username;
    protected final String password;
    protected final String recaptchaResponse;

    public AuthenticationParameters(String username, String password, String recaptchaResponse) {
        this.username = username;
        this.password = password;
        this.recaptchaResponse = recaptchaResponse;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRecaptchaResponse() {
        return recaptchaResponse;
    }
}
