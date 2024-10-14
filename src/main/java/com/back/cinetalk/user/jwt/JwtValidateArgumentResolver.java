package com.back.cinetalk.user.jwt;

import com.back.cinetalk.exception.errorCode.CommonErrorCode;
import com.back.cinetalk.exception.exception.RestApiException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


@Component
@AllArgsConstructor
public class JwtValidateArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String ACCESS_TOKEN_HEADER = "AccessToken";

    private final JWTUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasJwtValidationAnnotation = parameter.hasParameterAnnotation(JwtValidation.class);
        boolean hasOptionalJwtValidationAnnotation = parameter.hasParameterAnnotation(OptionalJwtValidation.class);
        boolean hasStringType = String.class.isAssignableFrom(parameter.getParameterType());

        // JwtValidation이나 OptionalJwtValidation 어노테이션이 있으면 지원
        return (hasJwtValidationAnnotation || hasOptionalJwtValidationAnnotation) && hasStringType;
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String accessToken = request.getHeader("access");

        // OptionalJwtValidation 어노테이션이 있는 경우 토큰이 없어도 허용
        if (parameter.hasParameterAnnotation(OptionalJwtValidation.class)) {
            if (accessToken == null || accessToken.isEmpty()) {
                return null;  // 액세스 토큰이 없으면 null을 반환
            }
        } else {
            // JwtValidation 어노테이션이 있는 경우 토큰이 필수
            if (accessToken == null) {
                throw new RestApiException(CommonErrorCode.INVALID_ACCESS_TOKEN);
            }
        }

        return jwtUtil.getEmail(accessToken);
    }
}
