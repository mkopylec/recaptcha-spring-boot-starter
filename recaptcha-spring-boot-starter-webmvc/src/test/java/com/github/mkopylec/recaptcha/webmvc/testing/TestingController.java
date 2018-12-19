package com.github.mkopylec.recaptcha.webmvc.testing;

import com.github.mkopylec.recaptcha.webmvc.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.webmvc.validation.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "testTesting")
public class TestingController {

    @Autowired
    private RecaptchaValidator recaptchaValidator;

    @RequestMapping(value = "validate", method = POST)
    public ValidationResult validate(HttpServletRequest request) {
        return recaptchaValidator.validate(request);
    }
}
