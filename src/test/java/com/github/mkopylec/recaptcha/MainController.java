package com.github.mkopylec.recaptcha;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String showIndexView() {
        return "index";
    }

    @RequestMapping("/login")
    public String showLoginView() {
        return "login";
    }
}
