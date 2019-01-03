package com.github.mkopylec.recaptcha.test.stubs

import com.fasterxml.jackson.databind.ObjectMapper
import org.mockserver.integration.ClientAndServer

import static com.github.mkopylec.recaptcha.test.specification.BasicSpec.INVALID_CAPTCHA_RESPONSE
import static com.github.mkopylec.recaptcha.test.specification.BasicSpec.INVALID_SECRET
import static com.github.mkopylec.recaptcha.test.specification.BasicSpec.REMOTE_IP_ADDRESS
import static com.github.mkopylec.recaptcha.test.specification.BasicSpec.VALID_CAPTCHA_RESPONSE
import static com.github.mkopylec.recaptcha.test.specification.BasicSpec.VALID_SECRET
import static org.mockserver.integration.ClientAndServer.startClientAndServer
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response
import static org.mockserver.model.Parameter.param
import static org.mockserver.model.ParameterBody.params

class RecaptchaServerMock {

    private static final ObjectMapper jsonMapper = new ObjectMapper()

    private ClientAndServer server

    RecaptchaServerMock() {
        server = startClientAndServer(8081)
    }

    void reset() {
        server.reset()
    }

    void stubSuccessfulRecaptchaValidation() {
        stubRecaptchaValidation(VALID_CAPTCHA_RESPONSE, VALID_SECRET, '', true, [])
    }

    void stubCustomIpSuccessfulRecaptchaValidation() {
        stubRecaptchaValidation(VALID_CAPTCHA_RESPONSE, VALID_SECRET, REMOTE_IP_ADDRESS, true, [])
    }

    void stubInvalidSecretRecaptchaValidation() {
        stubRecaptchaValidation(VALID_CAPTCHA_RESPONSE, INVALID_SECRET, '', false, ['invalid-input-secret'])
    }

    void stubMissingResponseRecaptchaValidation() {
        stubRecaptchaValidation(null, VALID_SECRET, '', false, ['missing-input-response'])
    }

    void stubInvalidResponseRecaptchaValidation() {
        stubRecaptchaValidation(INVALID_CAPTCHA_RESPONSE, VALID_SECRET, '', false, ['invalid-input-response'])
    }

    private void stubRecaptchaValidation(
            String userResponse, String secretKey, String remoteIp, boolean success, List<String> errors
    ) {
        def body = params(param('response', userResponse), param('secret', secretKey), param('remoteip', remoteIp))
        def result = jsonMapper.writeValueAsString(['success': success, 'error-codes': errors])
        server.when(request('/recaptcha/api/siteverify')
                .withMethod('POST')
                .withBody(body))
                .respond(response(result)
                .withStatusCode(200)
                .withHeader('Content-Type', 'application/json'))
    }
}
