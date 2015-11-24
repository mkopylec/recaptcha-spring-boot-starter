package com.github.mkopylec.recaptcha.security.login;

import com.github.mkopylec.recaptcha.RecaptchaProperties.Security;

public class RecaptchaLoginFailuresCountingHandler extends LoginFailuresCountingHandler {

    public static final String RECAPTCHA_ERROR_QUERY_PARAM = "recaptchaError";

    public RecaptchaLoginFailuresCountingHandler(LoginFailuresManager failuresManager, Security security) {
        super(failuresManager, security, RECAPTCHA_ERROR_QUERY_PARAM);
    }
}
