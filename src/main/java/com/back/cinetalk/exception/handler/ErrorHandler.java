package com.back.cinetalk.exception.handler;

import com.back.cinetalk.exception.errorCode.ErrorCode;
import com.back.cinetalk.exception.exception.RestApiException;

public class ErrorHandler extends RestApiException {

    public ErrorHandler(ErrorCode errorCode) {
        super(errorCode);
    }
}
