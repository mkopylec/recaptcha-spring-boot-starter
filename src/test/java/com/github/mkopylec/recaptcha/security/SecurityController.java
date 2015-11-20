package com.github.mkopylec.recaptcha.security;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.mkopylec.recaptcha.Strings.RESPONSE_DATA_MESSAGE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "testSecurity")
public class SecurityController {

    @RequestMapping(value = "getResponse", method = POST)
    public ResponseData getResponseData() {
        return new ResponseData(RESPONSE_DATA_MESSAGE);
    }
}
