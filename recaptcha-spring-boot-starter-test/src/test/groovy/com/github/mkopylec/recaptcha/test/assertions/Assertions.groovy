package com.github.mkopylec.recaptcha.test.assertions

import com.github.mkopylec.recaptcha.test.utils.Response

class Assertions {

    static ResponseAssert assertThat(Response response) {
        return new ResponseAssert(response)
    }
}
