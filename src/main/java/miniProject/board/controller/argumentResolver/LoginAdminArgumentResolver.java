package miniProject.board.controller.argumentResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.controller.constant.SessionConst;
import miniProject.board.dto.AdminSessionDto;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Login Annotation을 가지고 있는 AdminSessionDto 파라미터에 적용 하는 ArgumentResolver
 */
@Slf4j
public class LoginAdminArgumentResolver implements HandlerMethodArgumentResolver {
    /**
     * <pre>
     * 파라미터에 Login Annotation가 있는지 확인
     * 해당 파라미터가 AddminSessionDto타입인지 확인
     * </pre>
     *
     * @param parameter 검증할 파라미터
     * @return 파라미터에 Login Annotation가 있고 AddminSessionDto타입이면 true
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);

        boolean hasAdminSessionDtoType = AdminSessionDto.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasAdminSessionDtoType;
    }

    /**
     * <pre>
     * 세션에 있는 멤버 값을 가져와서 AddminSessionDto 객체에 주입
     * </pre>
     *
     * @return 세션에 멤버 값 있으면 해당 정보 반환, 없으면 null
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);

        // 세션에 값이 없음
        // 로그인 한 관리자의 요청이 아님
        if (session == null) {
            return null;
        }

        return session.getAttribute(SessionConst.LOGIN_ADMIN);
    }
}
