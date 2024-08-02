package miniProject.board.controller.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import miniProject.board.auth.utils.CookieUtil;
import miniProject.board.repository.RefreshRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LogoutController {

    private final RefreshRepository refreshRepository;
    private final CookieUtil cookieUtil;


    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieUtil.getRefreshToken(request);
        refreshRepository.deleteByRefreshToken(refreshToken);

        cookieUtil.clearAllCookie(request, response);

        return "redirect:/";
    }
}
