# reCAPTCHA Spring Boot Starter
[![Build Status](https://travis-ci.org/mkopylec/recaptcha-spring-boot-starter.svg?branch=master)](https://travis-ci.org/mkopylec/recaptcha-spring-boot-starter)

To use the starter you will need a reCAPTCHA site key and a secret key.
To get them go to the [reCAPTCHA Home Page](https://www.google.com/recaptcha/intro/index.html) and set up your reCAPTCHA.

## Installing

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
    compile 'com.github.mkopylec:recaptcha-spring-boot-starter:0.1.0'
}
```

## How to use
The starter can be used in 3 different modes:

#### Normal web application usage:
Embed reCAPTCHA in HTML web page:

```html
<html>
<head>
    <script src="https://www.google.com/recaptcha/api.js"></script>
    ...
</head>
<body>

<form action="/" method="post">
    <div class="g-recaptcha" data-sitekey="<your_site_key>"></div>
    <input type="submit" value="Validate reCAPTCHA" />
</form>

</body>
</html>
```

Inject `RecaptchaValidator` into your controller:

```java
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class MainController {

    @Autowired
    private RecaptchaValidator recaptchaValidator;

    @RequestMapping(value = "/", method = POST)
    public void validateCaptcha(HttpServletRequest request) {
        ValidationResult result = recaptchaValidator.validate(request);
        if (result.isSuccess()) {
            ...
        }
    }
}
```

Set your secret key in _application.yml_ file:

```yaml
spring.recaptcha.validation.secretKey: <your_secret_key>
```

##### Additional info
`RecaptchaValidator` provides couple of useful methods to validate reCAPTCHA response.

#### Spring Security web application usage:
Add Spring Security dependency:

```gradle
dependencies {
    compile group: 'org.springframework.boot:spring-boot-starter-security:1.3.0.RELEASE'
}
```

Embed reCAPTCHA in HTML **login** web page:

```html
<html>
<head>
    <script src="https://www.google.com/recaptcha/api.js"></script>
    ...
</head>
<body>

<form action="/login" method="post">
    User: <input name="username" type="text" value="" />
    Password: <input name="password" type="password" value="" />
    <!--<if condition>-->
    <div class="g-recaptcha" data-sitekey="<your_site_key>"></div>
    <!--</if>-->
    <input type="submit" value="Log in" />
</form>

</body>
</html>
```

Add `RecaptchaAuthenticationFilter` **before** `UsernamePasswordAuthenticationFilter` in your security configuration

```java
import com.github.mkopylec.recaptcha.security.RecaptchaAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private RecaptchaAuthenticationFilter authenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().loginPage("/login")
                .and()
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                ...
    }
}
```

Set your secret key in _application.yml_ file:

```yaml
spring.recaptcha.validation.secretKey: <your_secret_key>
```

##### Additional info
The `RecaptchaAuthenticationFilter` must be fired on the **same URL** as the **login POST** request is processed.
The default filter URL is _/login_ but you can customize it using `spring.recaptcha.security.loginProcessingUrl` property.
When user enters wrong reCAPTCHA response he will be redirected to `spring.recaptcha.security.loginProcessingUrl` _recaptchaError_ query parameter by default.
You can customize the failure redirect using `spring.recaptcha.security.failureUrl` property.

#### Testing mode
To be done...

## Examples
Go to [reCAPTCHA Spring Boot Starter samples](https://github.com/mkopylec/recaptcha-spring-boot-starter-samples) to view example applications.

## License
reCAPTCHA Spring Boot Starter is published under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
