package com.github.mkopylec.recaptcha.webmvc.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class AuthenticationRequest extends HttpServletRequestWrapper {

    protected final AuthenticationParameters parameters;

    public AuthenticationRequest(HttpServletRequest request, AuthenticationRequestParser requestParser) {
        super(request);
        parameters = requestParser.parse(request);
    }

    public AuthenticationParameters getParameters() {
        return parameters;
    }
}
