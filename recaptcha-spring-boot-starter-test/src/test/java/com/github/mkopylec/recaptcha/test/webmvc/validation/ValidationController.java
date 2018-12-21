package com.github.mkopylec.recaptcha.test.webmvc.validation;

import com.github.mkopylec.recaptcha.webmvc.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.webmvc.validation.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.github.mkopylec.recaptcha.test.Strings.REMOTE_IP_ADDRESS;
import static com.github.mkopylec.recaptcha.test.Strings.WEBMVC_SPRING_PROFILE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Profile(WEBMVC_SPRING_PROFILE)
@RestController
@RequestMapping(value = "testValidation")
public class ValidationController {

    @Autowired
    private RecaptchaValidator recaptchaValidator;

    @RequestMapping(value = "userResponse", method = POST)
    public ValidationResult validateUsingUserResponse(HttpServletRequest request) {
        return recaptchaValidator.validate(request);
    }

    @RequestMapping(value = "userResponseAndIp", method = POST)
    public ValidationResult validateUsingUserResponseAndIp(HttpServletRequest request) {
        return recaptchaValidator.validate(request, REMOTE_IP_ADDRESS);
    }
}
