package com.github.mkopylec.recaptcha.webmvc.test.security;

import com.github.mkopylec.recaptcha.test.utils.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.mkopylec.recaptcha.test.specification.BasicSpec.RESPONSE_BODY_MESSAGE;

@RestController
@RequestMapping(value = "testSecurity")
public class SecurityController {

    @PostMapping("getResponse")
    public ResponseBody getResponseData() {
        return new ResponseBody(RESPONSE_BODY_MESSAGE);
    }
}
