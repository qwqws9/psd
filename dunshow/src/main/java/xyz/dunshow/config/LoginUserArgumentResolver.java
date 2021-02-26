package xyz.dunshow.config;

import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.annotation.LoginUser;
import xyz.dunshow.constants.SessionKey;
import xyz.dunshow.dto.UserSession;

@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isContainAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isEqualClass = UserSession.class.equals(parameter.getParameterType());
        return isContainAnnotation && isEqualClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        return httpSession.getAttribute(SessionKey.LOGIN.getValue());
    }

}
