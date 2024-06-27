package miniProject.board.controller.config;

import lombok.RequiredArgsConstructor;
import miniProject.board.controller.argumentResolver.LoginAdminArgumentResolver;
import miniProject.board.controller.argumentResolver.LoginMemberArgumentResolver;
import miniProject.board.controller.interceptor.LogInterceptor;
import miniProject.board.repository.MemberRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Web MVC 설정을 담당하는 클래스입니다.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {


    private final MemberRepository memberRepository;

    /**
     * 인터셉터 등록
     *
     * <pre>
     * LogInterceptor
     * 해당 인터셉터 호출 순서는 1번입니다.
     * 모든 경로에 인터셉터 적용
     * 단 제외 경로로 CSS 파일, *.ico 파일, 에러 페이지는 인터셉터 적용을 제외합니다.
     * </pre>
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 로그 출력 인터셉터 등록
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");
    }

    /**
     * LoginMemberArgumentResolver를 등록합니다.
     * LoginAdminArgumentResolver를 등록합니다.
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberRepository));
        resolvers.add(new LoginAdminArgumentResolver());
    }
}
