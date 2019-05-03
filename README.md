# reCAPTCHA Spring Boot Starter
[![Build Status](https://travis-ci.org/mkopylec/recaptcha-spring-boot-starter.svg?branch=master)](https://travis-ci.org/mkopylec/recaptcha-spring-boot-starter)
[![Coverage Status](https://coveralls.io/repos/mkopylec/recaptcha-spring-boot-starter/badge.svg?branch=master&service=github)](https://coveralls.io/github/mkopylec/recaptcha-spring-boot-starter?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mkopylec/recaptcha-spring-boot-starter/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.github.mkopylec/recaptcha-spring-boot-starter)

To use the starter you will need a reCAPTCHA site key and a secret key.
To get them go to the [reCAPTCHA Home Page](https://www.google.com/recaptcha/intro/index.html) and set up your reCAPTCHA.

## Installing

```gradle
repositories {
    mavenCentral()
}
dependencies {
    compile group: 'com.github.mkopylec', name: 'recaptcha-spring-boot-starter', version: '2.2.0'
}
```

## How to use
The starter can be used in 3 different modes:

### Normal web application usage
Embed reCAPTCHA in HTML web page:

```html
<html>
<head>
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
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

Inject `RecaptchaValidator` into your controller and validate user reCAPTCHA response:

```java
@Controller
public class MainController {

    @Autowired
    private RecaptchaValidator recaptchaValidator;

    @PostMapping("/")
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
recaptcha.validation.secret-key: <your_secret_key>
```

##### Additional info
`RecaptchaValidator` provides couple of useful methods to validate reCAPTCHA response.

### Spring Security web application usage
Add Spring Security dependency:

```gradle
dependencies {
    compile group: 'org.springframework.boot', name: 'spring-boot-starter-security', version: '2.0.4.RELEASE'
}
```

Embed reCAPTCHA in HTML **login** web page:

```html
<html>
<head>
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    ...
</head>
<body>

<form action="/login" method="post">
    User: <input name="username" type="text" value="" />
    Password: <input name="password" type="password" value="" />
    <!--<if request has 'showRecaptcha' query param>-->
    <div class="g-recaptcha" data-sitekey="<your_site_key>"></div>
    <!--</if>-->
    <input type="submit" value="Log in" />
</form>

</body>
</html>
```

Add reCAPTCHA support to your form login security configuration using `FormLoginConfigurerEnhancer`.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private FormLoginConfigurerEnhancer enhancer;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        enhancer.addRecaptchaSupport(http.formLogin()).loginPage("/login")
                .and()
                .csrf().disable()
                ...
    }
}
```

Create custom login failures manager bean by extending `LoginFailuresManager`:

```java
@Component
public class CustomLoginFailuresManager extends LoginFailuresManager {

    public CustomLoginFailuresManager(RecaptchaProperties recaptcha) {
        super(recaptcha);
    }

    ...
}
```

Set your secret key in _application.yml_ file:

```yaml
recaptcha.validation.secret-key: <your_secret_key>
```

##### Additional info
After adding reCAPTCHA support to form login configuration you can only add `AuthenticationSuccessHandler` that extends
`LoginFailuresClearingHandler` and `AuthenticationFailureHandler` that extends `LoginFailuresCountingHandler`.

There can be 4 different query parameters in redirect to login page:
 - _error_ - credentials authentication error
 - _recaptchaError_ - reCAPTCHA authentication error
 - _showRecaptcha_ - reCAPTCHA must be displayed on login page
 - _logout_ - user has been successfully logged out

There is a default `LoginFailuresManager` implementation in the starter which is `InMemoryLoginFailuresManager`.
It is recommended to create your own `LoginFailuresManager` implementation that persists login failures in some storage.

### Integration testing mode usage
Enable testing mode:

```yaml
recaptcha.testing.enabled: true
```

Configure testing mode:

```yaml
recaptcha.testing:
  success-result: false
  result-error-codes: INVALID_SECRET_KEY, INVALID_USER_CAPTCHA_RESPONSE
```

##### Additional info
In testing mode no remote reCAPTCHA validation is fired, the validation process is offline.

## Configuration properties list

```yaml
recaptcha:
  validation:
    secret-key: # reCAPTCHA secret key.
    response-parameter: g-recaptcha-response # HTTP request parameter name containing user reCAPTCHA response.
    verification-url: https://www.google.com/recaptcha/api/siteverify # reCAPTCHA validation endpoint.
    timeout:
      connect: 500ms # reCAPTCHA validation request connect timeout.
      read: 1000ms # reCAPTCHA validation request read timeout.
      write: 1000ms # reCAPTCHA validation request write timeout.
  security:
    failure-url: /login # URL to redirect to when user authentication fails.
    login-failures-threshold: 5 # Number of allowed login failures before reCAPTCHA must be displayed.
    continue-on-validation-http-error: true # Permits or denies continuing user authentication process after reCAPTCHA validation fails because of HTTP error.
    proxy: false # Indicates whether the application is behind a proxy (thus a request's remote address will be the proxy's ip).
    forwardHeader: X-Forwarded-For # The header from which to read the client's ip, used when proxy is set to true.
  testing:
    enabled: false # Flag for enabling and disabling testing mode.
    success-result: true # Defines successful or unsuccessful validation result, can be changed during tests.
    result-error-codes: # Errors in validation result, can be changed during tests.
```

## Examples
Go to [reCAPTCHA Spring Boot Starter samples](https://github.com/mkopylec/recaptcha-spring-boot-starter-samples) to view example applications.

## License
reCAPTCHA Spring Boot Starter is published under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
