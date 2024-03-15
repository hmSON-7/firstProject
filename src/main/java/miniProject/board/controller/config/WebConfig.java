package miniProject.board.controller.config;

import miniProject.board.controller.argumentResolver.LoginMemberArgumentResolver;
import miniProject.board.controller.interceptor.LogInterceptor;
import miniProject.board.controller.interceptor.MemberLoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Web MVC 설정을 담당하는 클래스입니다.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 인터셉터 등록
     *
     * <pre>
     * LogInterceptor
     * 해당 인터셉터 호출 순서는 1번입니다.
     * 모든 경로에 인터셉터 적용
     * 단 제외 경로로 CSS 파일, *.ico 파일, 에러 페이지는 인터셉터 적용을 제외합니다.
     * </pre>
     *
     * <pre>
     * MemberLoginCheckInterceptor
     * 해당 인터셉터의 호출 순서는 2번입니다.
     * 모든 경로에 인터셉터 적용
     * 단 제외 경로로 CSS 파일, *.ico 파일, 에러 페이지, 회원가입 페이지, 로그인 페이지,
     * 홈페이지는 인터셉터 적용을 제외합니다.
     * </pre>
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 로그 출력 인터셉터 등록
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        // 로그인 확인 인터셉터 등록
        registry.addInterceptor(new MemberLoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/member/signUp",
                        "/member/login", "/");
    }

    /**
     * LoginMemberArgumentResolver를 등록합니다.
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }
}