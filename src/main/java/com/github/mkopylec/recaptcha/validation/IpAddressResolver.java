package com.github.mkopylec.recaptcha.validation;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.util.StringUtils.hasLength;

public class IpAddressResolver {

    public static final String X_FORWARDED_FOR_HEADER = "X-Forwarded-For";

    public String resolveClientIp(HttpServletRequest request) {
        String ipAddresses = request.getHeader(X_FORWARDED_FOR_HEADER);
        if (hasLength(ipAddresses)) {
            return ipAddresses.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
