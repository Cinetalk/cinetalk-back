package com.back.cinetalk.exception.handler;

import com.back.cinetalk.exception.errorCode.ErrorCode;
import com.back.cinetalk.exception.exception.RestApiException;

public class KeywordHandler extends RestApiException {

    public KeywordHandler(ErrorCode errorCode) {
        super(errorCode);
    }
}
