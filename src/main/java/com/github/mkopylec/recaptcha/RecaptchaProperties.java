package com.github.mkopylec.recaptcha;

import com.github.mkopylec.recaptcha.validation.ErrorCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

import static com.github.mkopylec.recaptcha.validation.ErrorCode.MISSING_USER_CAPTCHA_RESPONSE;
import static java.util.Collections.singletonList;

@ConfigurationProperties("spring.recaptcha")
public class RecaptchaProperties {

    private Validation validation = new Validation();
    private Security security = new Security();
    private Testing testing = new Testing();

    public Validation getValidation() {
        return validation;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public Testing getTesting() {
        return testing;
    }

    public void setTesting(Testing testing) {
        this.testing = testing;
    }

    public static class Validation {

        private String secretKey;
        private String responseParameter = "g-recaptcha-response";
        private String verificationUrl = "https://www.google.com/recaptcha/api/siteverify";

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getResponseParameter() {
            return responseParameter;
        }

        public void setResponseParameter(String responseParameter) {
            this.responseParameter = responseParameter;
        }

        public String getVerificationUrl() {
            return verificationUrl;
        }

        public void setVerificationUrl(String verificationUrl) {
            this.verificationUrl = verificationUrl;
        }
    }

    public static class Security {

        private String failureUrl = "/login?recaptchaError";
        private List<String> securedPaths = singletonList("/login");

        public String getFailureUrl() {
            return failureUrl;
        }

        public void setFailureUrl(String failureUrl) {
            this.failureUrl = failureUrl;
        }

        public List<String> getSecuredPaths() {
            return securedPaths;
        }

        public void setSecuredPaths(List<String> securedPaths) {
            this.securedPaths = securedPaths;
        }
    }

    public static class Testing {

        private boolean enabled = false;
        private boolean successResult = true;
        private List<ErrorCode> resultErrorCodes = singletonList(MISSING_USER_CAPTCHA_RESPONSE);

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isSuccessResult() {
            return successResult;
        }

        public void setSuccessResult(boolean successResult) {
            this.successResult = successResult;
        }

        public List<ErrorCode> getResultErrorCodes() {
            return resultErrorCodes;
        }

        public void setResultErrorCodes(List<ErrorCode> resultErrorCodes) {
            this.resultErrorCodes = resultErrorCodes;
        }
    }
}
