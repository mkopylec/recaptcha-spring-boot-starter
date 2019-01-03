package com.github.mkopylec.recaptcha.webmvc.test.testing;

import com.github.mkopylec.recaptcha.commons.validation.ValidationResult;
import com.github.mkopylec.recaptcha.webmvc.validation.RecaptchaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "testTesting")
public class TestingController {

    @Autowired
    private RecaptchaValidator recaptchaValidator;

    @PostMapping("validate")
    public ValidationResult validate(HttpServletRequest request) {
        return recaptchaValidator.validate(request);
    }
}
