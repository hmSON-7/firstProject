package miniProject.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.controller.argumentResolver.Login;
import miniProject.board.controller.constant.LogConst;
import miniProject.board.dto.MemberDto;
import org.slf4j.MDC;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 홈페이지와 관련된 컨트롤러입니다.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    /**
     * <pre>
     * 세션에 로그인 정보가 없으면 home View 렌더링
     * 세션에 로그인한 정보가 있다면 loginHome 렌더링
     * </pre>
     *
     * @param memberSessionDto 세션을 통해 가져온 멤버 정보로 만약 로그인 하지 않았으면 null
     * @return 로그인한 사용자인 경우 loginHome, 로그인 하지 않은 사용자인 경우 home
     */
    @GetMapping("/")
    public String home(@Login MemberDto.Session memberSessionDto, Model model) {
        log.debug("[{}] 홈페이지", MDC.get(LogConst.TRACE_ID));

        if (memberSessionDto == null) {
            return "main/home";
        }

        model.addAttribute("memberSessionDto", memberSessionDto);

        return "main/loginHome";
    }
}