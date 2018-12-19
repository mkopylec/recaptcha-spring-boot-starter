package com.github.mkopylec.recaptcha.webmvc.security;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationRequestParser {

    AuthenticationParameters parse(HttpServletRequest request);
}
