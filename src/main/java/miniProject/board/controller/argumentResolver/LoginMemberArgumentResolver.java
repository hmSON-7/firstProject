package miniProject.board.controller.argumentResolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.controller.constant.LogConst;
import miniProject.board.dto.MemberDto;
import miniProject.board.entity.Member;
import miniProject.board.repository.MemberRepository;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Login Annotation을 가지고 있는 MemberSessionDto 파라미터에 적용 하는 ArgumentResolver
 */
@Slf4j
@RequiredArgsConstructor
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

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

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.debug("[{}] MemberSessionDto Resolve Argument 실행", MDC.get(LogConst.TRACE_ID));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Member member = memberRepository.findByUsername(username).orElse(null);

        if (member == null) {
            return null;
        }

        return MemberDto.Session.fromMember(member);
    }
}