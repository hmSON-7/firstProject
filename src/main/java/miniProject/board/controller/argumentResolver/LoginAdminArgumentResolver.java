package miniProject.board.controller.argumentResolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.dto.MemberDto;
import miniProject.board.entity.Member;
import miniProject.board.repository.MemberRepository;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Login Annotation을 가지고 있는 AdminSessionDto 파라미터에 적용 하는 ArgumentResolver
 */
@Slf4j
@RequiredArgsConstructor
public class LoginAdminArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

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

        boolean hasAdminSessionDtoType = MemberDto.Session.class.isAssignableFrom(parameter.getParameterType());

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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Member member = memberRepository.findByUsername(username).orElse(null);

        if (member == null) {
            return null;
        }

        return MemberDto.Session.fromMember(member);
    }
}
