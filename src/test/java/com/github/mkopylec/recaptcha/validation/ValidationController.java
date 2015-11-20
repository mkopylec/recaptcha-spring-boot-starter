package com.github.mkopylec.recaptcha.validation;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.github.mkopylec.recaptcha.Strings.REMOTE_IP_ADDRESS;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "testValidation")
@EnableConfigurationProperties(RecaptchaProperties.class)
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
