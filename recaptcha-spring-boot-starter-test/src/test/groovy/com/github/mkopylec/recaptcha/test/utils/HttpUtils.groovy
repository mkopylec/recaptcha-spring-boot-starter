package com.github.mkopylec.recaptcha.test.utils

import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

class HttpUtils {

    static MultiValueMap<String, Object> toFormData(Map<String, Object> parameters) {
        def formData = new LinkedMultiValueMap<>()
        for (def parameter : parameters.entrySet()) {
            formData.add(parameter.key, parameter.value)
        }
        return formData
    }
}
