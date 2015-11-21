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
    compile 'com.github.mkopylec:recaptcha-spring-boot-starter:1.0.0'
}
```

## How to use
The starter can be used in 3 different modes:

### Normal web application usage
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

### Spring Security web application usage
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
When user enters wrong reCAPTCHA response he will be redirected to `spring.recaptcha.security.loginProcessingUrl` with _recaptchaError_ query parameter by default.
You can customize the failure redirect using `spring.recaptcha.security.failureUrl` property.

### Integration testing mode usage
Enable testing mode:

```yaml
spring.recaptcha.testing.enabled: true
```

Configure testing mode:

```yaml
spring.recaptcha.testing:
    successResult: false
    resultErrorCodes: INVALID_SECRET_KEY, INVALID_USER_CAPTCHA_RESPONSE
```

##### Additional info
In testing mode no remote reCAPTCHA validation is fired, the validation process is offline.

## Configuration properties list

```yaml
spring.recaptcha:
    validation:
        secretKey: # reCAPTCHA secret key
        responseParameter: g-recaptcha-response # User reCAPTCHA response HTTP request parameter
        verificationUrl: https://www.google.com/recaptcha/api/siteverify # reCAPTCHA validation endpoint
    security:
        loginProcessingUrl: /login # Login form processing URL from Spring Security configuration
        failureUrl: # Fixed URL to redirect to when reCAPTCHA validation fails
    testing:
        enabled: false # Flag for enabling and disabling testing mode
        successResult: true # Defines successful or unsuccessful validation result, can be changed during tests
        resultErrorCodes: # Errors in validation result, can be changed during tests
```

## Examples
Go to [reCAPTCHA Spring Boot Starter samples](https://github.com/mkopylec/recaptcha-spring-boot-starter-samples) to view example applications.

## License
reCAPTCHA Spring Boot Starter is published under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
