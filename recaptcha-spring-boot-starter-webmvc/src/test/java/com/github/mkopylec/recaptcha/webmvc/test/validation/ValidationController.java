package com.github.mkopylec.recaptcha.webmvc.test.validation;

import com.github.mkopylec.recaptcha.commons.validation.ValidationResult;
import com.github.mkopylec.recaptcha.webmvc.validation.RecaptchaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.github.mkopylec.recaptcha.test.specification.BasicSpec.REMOTE_IP_ADDRESS;

@RestController
@RequestMapping(value = "testValidation")
public class ValidationController {

    @Autowired
    private RecaptchaValidator recaptchaValidator;

    @PostMapping("userResponse")
    public ValidationResult validateUsingUserResponse(HttpServletRequest request) {
        return recaptchaValidator.validate(request);
    }

    @PostMapping("userResponseAndIp")
    public ValidationResult validateUsingUserResponseAndIp(HttpServletRequest request) {
        return recaptchaValidator.validate(request, REMOTE_IP_ADDRESS);
    }
}
