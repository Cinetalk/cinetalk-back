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
    INVALID_ACCESS_TOKEN(HttpStatus.FORBIDDEN, "유효하지 않은 토큰입니다."),

    //토큰 갱신 관련 오류

    REFRESH_TOKEN_NULL(HttpStatus.BAD_REQUEST,"refresh토큰이 존재하지 않습니다."),
    REFRESH_TOKEN_ISEXPIRED(HttpStatus.BAD_REQUEST,"refresh토큰이 만료되었습니다."),
    REFRESH_TOKEN_UNDIFINED(HttpStatus.BAD_REQUEST,"refresh토큰이 유효하지 않습니다."),
    REFRESH_TOKEN_NOT_SAVED(HttpStatus.BAD_REQUEST,"저장된 토큰이 존재하지 않습니다."),


    //유저 관련 에러
    NICKNAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    USER_CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "유효하지 않은 주소입니다."),
    USER_IMAGE_ERROR(HttpStatus.BAD_REQUEST, "이미지 등록 중 에러가 발생하였습니다."),

    // 리뷰 관련 에러
    REVIEW_ALREADY_IN_WRITE(HttpStatus.BAD_REQUEST, "이미 작성한 리뷰입니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 답글을 찾을 수 없습니다"),
    GENRE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 장르를 찾을 수 없습니다"),
    REVIEW_NOT_ALLOWED(HttpStatus.FORBIDDEN, "리뷰를 수정할 권한이 없습니다"),

    // 키워드 관련 에러
    KEYWORD_ALREADY_IN_WRITE(HttpStatus.BAD_REQUEST, "이미 작성한 키워드입니다."),
    KEYWORD_ALREADY_COUNT(HttpStatus.BAD_REQUEST, "해당 키워드를 이미 클릭하셨습니다."),
    KEYWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 키워드를 찾을 수 없습니다"),
    KEYWORD_NOT_ALLOWED(HttpStatus.FORBIDDEN, "키워드를 수정할 권한이 없습니다"),

    // 뱃지 관련 에러
    BADGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 뱃지를 찾을 수 없습니다"),

    // 검색 관련 에러
    FiND_NOT_FOUND(HttpStatus.BAD_REQUEST, "검색 도중 에러가 발생하였습니다."),

    // 신고 관련 에러
    REPORT_ALREADY_IN_WRITE(HttpStatus.BAD_REQUEST, "이미 신고한 리뷰입니다."),
    REPORT_NOT_FOUND(HttpStatus.BAD_REQUEST,"해당 신고를 찾을 수 없습니다."),


    // 피드백 관련 에러
    FEEDBACK_CONTENT_WRONG(HttpStatus.BAD_REQUEST,"피드백 내용이 옳바르지 않습니다."),

    //메인 페이지 관련 에러
    MENTIONKEYWORD_LESS(HttpStatus.BAD_REQUEST,"충분한 키워드가 모이지 않았습니다.")
    ;


    private final HttpStatus httpStatus;
    private final String message;

}
