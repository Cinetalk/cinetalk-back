package com.back.cinetalk.exception.handler;

import com.back.cinetalk.exception.errorCode.ErrorCode;
import com.back.cinetalk.exception.exception.RestApiException;

public class ReviewHandler extends RestApiException {

    public ReviewHandler(ErrorCode errorCode) {
        super(errorCode);
    }
}
