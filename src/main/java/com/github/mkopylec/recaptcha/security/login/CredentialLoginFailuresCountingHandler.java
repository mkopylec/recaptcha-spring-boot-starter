package com.github.mkopylec.recaptcha.security.login;

import com.github.mkopylec.recaptcha.RecaptchaProperties.Security;

public class CredentialLoginFailuresCountingHandler extends LoginFailuresCountingHandler {

    public static final String LOGIN_ERROR_QUERY_PARAM = "error";

    public CredentialLoginFailuresCountingHandler(LoginFailuresManager failuresManager, Security security) {
        super(failuresManager, security, LOGIN_ERROR_QUERY_PARAM);
    }
}
