package miniProject.board.controller.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.controller.constant.LogConst;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

/**
 * HTTP 요청과 응답을 로그로 출력하는 인터셉터입니다.
 */
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    /**
     * HTTP 요청에 대한 로그 출력, 추적을 위한 ID 설정
     *
     * @param request 현재 HTTP 요청
     * @param handler 요청한 핸들러(컨트롤러) 정보
     * @return 항상 진행하기 위해 true를 반환
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        // MDC에 추적을 위한 ID를 설정합니다.
        MDC.put(LogConst.TRACE_ID, uuid);

        log.debug("REQUEST [{}][{}][{}]", uuid, requestURI, handler);

        return true;
    }

    /**
     * 추적을 위한 ID, modelAndView 값 출력
     *
     * @param modelAndView 핸들러 호출 후 뷰와 모델 값
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {

        // MDC에서 TRACE_ID를 통해 uuid를 가져옵니다.
        log.debug("postHandle [{}][{}]", MDC.get(LogConst.TRACE_ID), modelAndView);
    }

    /**
     * HTTP 응답에 대한 로그를 출력, 예외가 발생한 경우 ERROR 레벨의 로그를 출력
     *
     * @param request 현재 request 요청
     * @param handler 호출된 핸들러(컨트롤러)
     * @param ex 예외가 발생했으면 값이 존재, 예외가 없으면 null
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();

        // MDC에서 TRACE_ID를 통해 uuid를 가져옵니다.
        log.debug("RESPONSE [{}][{}][{}]", MDC.get(LogConst.TRACE_ID), requestURI, handler);

        // 예외가 발생한 경우 ERROR 레벨의 로그를 출력합니다.
        if (ex != null) {
            log.error("afterCompletion error !! [{}]", MDC.get(LogConst.TRACE_ID), ex);
        }

        // MDC에서 TRACE_ID를 제거합니다.
        MDC.remove(LogConst.TRACE_ID);
    }
}
