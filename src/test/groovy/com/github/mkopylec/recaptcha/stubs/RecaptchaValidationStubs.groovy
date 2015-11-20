package com.github.mkopylec.recaptcha.stubs

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.ValueMatchingStrategy

import static com.github.mkopylec.recaptcha.Strings.INVALID_SECRET
import static com.github.mkopylec.recaptcha.Strings.REMOTE_IP_ADDRESS
import static com.github.mkopylec.recaptcha.Strings.VALID_CAPTCHA_RESPONSE
import static com.github.mkopylec.recaptcha.Strings.VALID_SECRET
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.containing
import static com.github.tomakehurst.wiremock.client.WireMock.post
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo

class RecaptchaValidationStubs {

    private static ObjectMapper mapper = new ObjectMapper();

    static void stubSuccessfulRecaptchaValidation() {
        stubRecaptchaValidation(VALID_CAPTCHA_RESPONSE, VALID_SECRET, '', true, [])
    }

    static void stubCustomIpSuccessfulRecaptchaValidation() {
        stubRecaptchaValidation(VALID_CAPTCHA_RESPONSE, VALID_SECRET, REMOTE_IP_ADDRESS, true, [])
    }

    static void stubInvalidSecretRecaptchaValidation() {
        stubRecaptchaValidation(VALID_CAPTCHA_RESPONSE, INVALID_SECRET, '', false, ['invalid-input-secret'])
    }

    static void stubMissingResponseRecaptchaValidation() {
        stubRecaptchaValidation(null, VALID_SECRET, '', false, ['missing-input-response'])
    }

    private static void stubRecaptchaValidation(
            String userResponse, String secretKey, String remoteIp, boolean success, List<String> errors
    ) {
        def result = mapper.writeValueAsString(['success': success, 'error-codes': errors])
        stubFor(post(urlEqualTo('/recaptcha/api/siteverify'))
                .withRequestBody(nullableContaining('response', userResponse))
                .withRequestBody(nullableContaining('secret', secretKey))
                .withRequestBody(nullableContaining('remoteip', remoteIp))
                .willReturn(
                aResponse()
                        .withStatus(200)
                        .withHeader('Content-Type', 'application/json')
                        .withBody(result)
        ));
    }

    private static ValueMatchingStrategy nullableContaining(String param, String value) {
        value != null ? containing("$param=$value") : containing('')
    }
}
