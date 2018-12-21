//package com.github.mkopylec.recaptcha.test.webmvc.testing;
//
//import com.github.mkopylec.recaptcha.webmvc.validation.RecaptchaValidator;
//import com.github.mkopylec.recaptcha.webmvc.validation.ValidationResult;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Profile;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//
//import static com.github.mkopylec.recaptcha.test.Strings.WEBMVC_SPRING_PROFILE;
//import static org.springframework.web.bind.annotation.RequestMethod.POST;
//
//@Profile(WEBMVC_SPRING_PROFILE)
//@RestController
//@RequestMapping(value = "testTesting")
//public class TestingController {
//
//    @Autowired
//    private RecaptchaValidator recaptchaValidator;
//
//    @RequestMapping(value = "validate", method = POST)
//    public ValidationResult validate(HttpServletRequest request) {
//        return recaptchaValidator.validate(request);
//    }
//}
