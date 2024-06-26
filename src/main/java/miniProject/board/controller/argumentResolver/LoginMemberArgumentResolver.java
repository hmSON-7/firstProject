package miniProject.board.controller.argumentResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.controller.constant.LogConst;
import miniProject.board.controller.constant.SessionConst;
import miniProject.board.dto.MemberDto;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Login Annotation을 가지고 있는 MemberSessionDto 파라미터에 적용 하는 ArgumentResolver
 */
@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    /**
     * <pre>
     * 파라미터에 Login Annotation가 있는지 확인
     * 해당 파라미터가 MemberSessionDto타입인지 확인
     * </pre>
     *
     * @param parameter 검증할 파라미터
     * @return 파라미터에 Login Annotation가 있고 MemberSessionDto타입이면 true
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.debug("[{}] MemberSessionDto Support Parameter 실행", MDC.get(LogConst.TRACE_ID));

        // Login Annotation을 가지고 있는가?
        boolean hasLoginAnnotaion = parameter.hasParameterAnnotation(Login.class);

        // 파라미터 타입이 MemberSessionDto 인가?
        boolean hasMemberSessionDtoType = MemberDto.Session
                .class
                .isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotaion && hasMemberSessionDtoType;
    }

    /**
     * <pre>
     * 세션에 있는 멤버 값을 가져와서 MemberSessionDto 객체에 주입
     * </pre>
     * 
     * @return 세션에 멤버 값 있으면 해당 정보 반환, 없으면 null
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.debug("[{}] MemberSessionDto Resolve Argument 실행", MDC.get(LogConst.TRACE_ID));

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);

        // 세션에 값이 없음
        // 로그인 한 사용자의 요청이 아님
        if (session == null) {
            return null;
        }

        // 멤버 정보를 세션에서 추출 함
        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}