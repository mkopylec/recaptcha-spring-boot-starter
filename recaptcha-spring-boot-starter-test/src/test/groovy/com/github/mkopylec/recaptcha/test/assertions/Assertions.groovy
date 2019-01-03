package com.github.mkopylec.recaptcha.test.assertions

import com.github.mkopylec.recaptcha.test.utils.HttpResponse

class Assertions {

    static ResponseAssert assertThat(HttpResponse response) {
        return new ResponseAssert(response)
    }
}
