package com.github.mkopylec.recaptcha.commons.validation;

import java.time.Duration;

public class Utils {

    public static int toMilliseconds(Duration duration) {
        return (int) duration.toMillis();
    }

    private Utils() {
    }
}
