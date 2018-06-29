package com.hello.exception;

import com.hello.api.ApiCode;

public class ApiKeyWrongException extends BaseException{

    public ApiKeyWrongException() {
        super(ApiCode.WRONG_API_KEY.getReasonPhrase());
    }

}
