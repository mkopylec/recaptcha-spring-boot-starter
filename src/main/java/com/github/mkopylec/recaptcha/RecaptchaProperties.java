package com.github.mkopylec.recaptcha;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.recaptcha")
public class RecaptchaProperties {

    private String secretKey;
    private String responseParameter = "g-recaptcha-response";
    private String verificationUrl = "https://www.google.com/recaptcha/api/siteverify";
    private String remoteIpHeader;

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

    public String getRemoteIpHeader() {
        return remoteIpHeader;
    }

    public void setRemoteIpHeader(String remoteIpHeader) {
        this.remoteIpHeader = remoteIpHeader;
    }
}
