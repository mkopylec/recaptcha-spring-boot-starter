package com.github.mkopylec.recaptcha.test.webmvc.security;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.mkopylec.recaptcha.test.Strings.RESPONSE_DATA_MESSAGE;
import static com.github.mkopylec.recaptcha.test.Strings.WEBMVC_SPRING_PROFILE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Profile(WEBMVC_SPRING_PROFILE)
@RestController
@RequestMapping(value = "testSecurity")
public class SecurityController {

    @RequestMapping(value = "getResponse", method = POST)
    public ResponseData getResponseData() {
        return new ResponseData(RESPONSE_DATA_MESSAGE);
    }
}
