package miniProject.board.controller.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.controller.constant.LogConst;
import miniProject.board.controller.constant.SessionConst;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 관리자 로그인 여부를 체크하는 인터셉터입니다.
 */
@Slf4j
public class AdminLoginCheckInterceptor implements HandlerInterceptor {
    /**
     * <pre>
     * 세션에 관리자 정보가 있는지 확인하여 로그인 한 관리자의 경우 체이닝을 진행합니다.
     * 만약 로그인 하지 않은 관리자라면 로그인 페이지로 리다이렉트,
     * 다음 체이닝을 진행하지 않고 종료합니다.
     * </pre>
     *
     * @param request 현재 HTTP 요청
     * @param handler 요청한 핸들러(컨트롤러) 정보
     * @return 로그인한 관리자인 경우 true, 로그인 하지 않은 경우 false
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        log.debug("[{}] 관리자 로그인 체크 인터셉터 실행", MDC.get(LogConst.TRACE_ID));

        // 세션에 Admin 값이 없다면 미인증 관리자
        if (session == null || session.getAttribute(SessionConst.LOGIN_ADMIN) == null) {
            log.info("[{}] 미인증 사용자 요청", MDC.get(LogConst.TRACE_ID));

            // 관리자 로그인 페이지로 Redirect
            response.sendRedirect("/admin/login");

            // 다음 체이닝 진행 안함
            return false;
        }

        // 로그인 한 관리자의 요청
        return true;
    }
}
