package com.github.mkopylec.recaptcha;

import com.github.mkopylec.recaptcha.validation.ErrorCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.time.Duration.ofMillis;
import static org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter.DEFAULT_LOGIN_PAGE_URL;

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
        /**
         * Properties responsible for reCAPTCHA validation request timeout.
         */
        private Timeout timeout = new Timeout();

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

        public Timeout getTimeout() {
            return timeout;
        }

        public void setTimeout(Timeout timeout) {
            this.timeout = timeout;
        }

        public static class Timeout {

            /**
             * reCAPTCHA validation request connect timeout.
             */
            private Duration connect = ofMillis(500);
            /**
             * reCAPTCHA validation request read timeout.
             */
            private Duration read = ofMillis(1000);
            /**
             * reCAPTCHA validation request write timeout.
             */
            private Duration write = ofMillis(1000);

            public Duration getConnect() {
                return connect;
            }

            public void setConnect(Duration connect) {
                this.connect = connect;
            }

            public Duration getRead() {
                return read;
            }

            public void setRead(Duration read) {
                this.read = read;
            }

            public Duration getWrite() {
                return write;
            }

            public void setWrite(Duration write) {
                this.write = write;
            }
        }
    }

    public static class Security {

        /**
         * URL to redirect to when user authentication fails.
         */
        private String failureUrl = DEFAULT_LOGIN_PAGE_URL;
        /**
         * Number of allowed login failures before reCAPTCHA must be displayed.
         */
        private int loginFailuresThreshold = 5;
        /**
         * Permits on denies continuing user authentication process after reCAPTCHA validation fails because of HTTP error.
         */
        private boolean continueOnValidationHttpError = true;

        /**
         * Indicates whether the application is behind a proxy
         * (thus a request's remote address will be the proxy's ip).
         */
        private boolean proxy=false;
        /**
         * The header from which to read the client's ip, used when proxy is set to true.
         */
        private String forwardHeader = "X-Forwarded-For";

        public String getFailureUrl() {
            return failureUrl;
        }

        public void setFailureUrl(String failureUrl) {
            this.failureUrl = failureUrl;
        }

        public int getLoginFailuresThreshold() {
            return loginFailuresThreshold;
        }

        public void setLoginFailuresThreshold(int loginFailuresThreshold) {
            this.loginFailuresThreshold = loginFailuresThreshold;
        }

        public boolean isContinueOnValidationHttpError() {
            return continueOnValidationHttpError;
        }

        public void setContinueOnValidationHttpError(boolean continueOnValidationHttpError) {
            this.continueOnValidationHttpError = continueOnValidationHttpError;
        }

        public String getForwardHeader() {
            return forwardHeader;
        }

        public void setForwardHeader(String forwardHeader) {
            this.forwardHeader = forwardHeader;
        }

        public boolean isProxy() {
            return proxy;
        }

        public void setProxy(boolean proxy) {
            this.proxy = proxy;
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
