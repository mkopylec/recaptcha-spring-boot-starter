package com.github.mkopylec.recaptcha.testing;

import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;
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
