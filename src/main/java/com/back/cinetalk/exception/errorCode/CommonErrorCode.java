package com.back.cinetalk.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "유효하지 않은 파라미터가 포함되어 있습니다"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리소스를 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생하였습니다"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청이 있습니다"),


    // 리뷰 관련 에러
    REVIEW_ALREADY_IN_WRITE(HttpStatus.BAD_REQUEST, "이미 작성한 리뷰입니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 답글을 찾을 수 없습니다"),
    GENRE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 장르를 찾을 수 없습니다"),
    REVIEW_NOT_ALLOWED(HttpStatus.FORBIDDEN, "리뷰를 수정할 권한이 없습니다"),


    // 키워드 관련 에러
    KEYWORD_ALREADY_IN_WRITE(HttpStatus.BAD_REQUEST, "이미 작성한 리뷰입니다."),
    KEYWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다"),
    KEYWORD_NOT_ALLOWED(HttpStatus.FORBIDDEN, "리뷰를 수정할 권한이 없습니다"),;


    private final HttpStatus httpStatus;
    private final String message;

}
