package com.github.mkopylec.recaptcha.test.utils

import com.github.mkopylec.recaptcha.test.webmvc.security.ResponseData
import com.github.mkopylec.recaptcha.commons.validation.ValidationResult

interface HttpClient {

    Response<ValidationResult> validateRecaptcha(String userResponse)

    Response<ValidationResult> validateRecaptchaWithIp(String userResponse)

    Response<ValidationResult> validateRecaptchaInTestingMode()

    Response<ResponseData> getSecuredData()

    Response<Void> logIn(String username, String password, String recaptchaResponse)

    void clearCookies();
}