package miniProject.board.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.auth.constants.CookieConstants;
import miniProject.board.auth.constants.JwtConstants;
import miniProject.board.auth.utils.CookieUtil;
import miniProject.board.repository.RefreshRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final RefreshRepository refreshRepository;
    private final CookieUtil cookieUtil;
    private final JWTUtil jwtUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String refreshToken = cookieUtil.getRefreshToken(request);

        // 로그 아웃 쿠키 설정
        Cookie logoutFlag = new Cookie(CookieConstants.LOGOUT_COOKIE_NAME, "true");
        logoutFlag.setHttpOnly(true);
        logoutFlag.setPath(CookieConstants.COOKIE_PATH);
        logoutFlag.setMaxAge(600);
        response.addCookie(logoutFlag);

        if (refreshToken != null) {
            try {
                if (!jwtUtil.isExpired(refreshToken) && JwtConstants.CATEGORY_REFRESH.equals(jwtUtil.getCategory(refreshToken))) {
                    refreshRepository.deleteByRefreshToken(refreshToken);
                }

            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
}
