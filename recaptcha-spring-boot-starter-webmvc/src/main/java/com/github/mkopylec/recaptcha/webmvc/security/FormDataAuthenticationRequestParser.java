package com.github.mkopylec.recaptcha.webmvc.security;

import javax.servlet.http.HttpServletRequest;

// TODO Problem: username and password parameters can be set via SS configuration
public class FormDataAuthenticationRequestParser implements AuthenticationRequestParser {

    @Override
    public AuthenticationParameters parse(HttpServletRequest request) {
        return null;
    }
}
