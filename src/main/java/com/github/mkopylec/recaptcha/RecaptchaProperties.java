package com.github.mkopylec.recaptcha;

import com.github.mkopylec.recaptcha.validation.ErrorCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * reCAPTCHA configuration properties.
 */
@ConfigurationProperties("recaptcha")
public class RecaptchaProperties {

    /**
     * Properties responsible for reCAPTCHA validation on Google's servers.
     */
    private Validation validation = new Validation();
    /**
     * Properties responsible for integration with Spring Security.
     */
    private Security security = new Security();
    /**
     * Properties responsible for testing mode behaviour.
     */
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

        /**
         * reCAPTCHA secret key.
         */
        private String secretKey;
        /**
         * HTTP request parameter name containing user reCAPTCHA response.
         */
        private String responseParameter = "g-recaptcha-response";
        /**
         * reCAPTCHA validation endpoint.
         */
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

        /**
         * Fixed URL to redirect to when reCAPTCHA validation fails.
         */
        private String failureUrl;
        /**
         * Login form processing URL from Spring Security configuration.
         */
        private String loginProcessingUrl = "/login";

        public String getFailureUrl() {
            return failureUrl;
        }

        public void setFailureUrl(String failureUrl) {
            this.failureUrl = failureUrl;
        }

        public String getLoginProcessingUrl() {
            return loginProcessingUrl;
        }

        public void setLoginProcessingUrl(String loginProcessingUrl) {
            this.loginProcessingUrl = loginProcessingUrl;
        }
    }

    public static class Testing {

        /**
         * Flag for enabling and disabling testing mode.
         */
        private boolean enabled = false;
        /**
         * Defines successful or unsuccessful validation result, can be changed during tests.
         */
        private boolean successResult = true;
        /**
         * Fixed errors in validation result, can be changed during tests.
         */
        private List<ErrorCode> resultErrorCodes = new ArrayList<>();

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
