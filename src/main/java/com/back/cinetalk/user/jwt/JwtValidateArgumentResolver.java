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
        boolean hasStringType = String.class.isAssignableFrom(parameter.getParameterType());
        return hasJwtValidationAnnotation && hasStringType;
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String accessToken = request.getHeader("access");

        if (accessToken == null){
            throw new RestApiException(CommonErrorCode.INVALID_ACCESS_TOKEN);
        }

        return jwtUtil.getEmail(accessToken);
    }
}
