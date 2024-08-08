package miniProject.board.service.auth;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import miniProject.board.auth.constants.CookieConstants;
import miniProject.board.auth.constants.JwtConstants;
import miniProject.board.auth.constants.Role;
import miniProject.board.auth.jwt.JWTUtil;
import miniProject.board.auth.utils.CookieUtil;
import miniProject.board.entity.Refresh;
import miniProject.board.repository.RefreshRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshService {

    private final RefreshRepository refreshRepository;
    private final CookieUtil cookieUtil;
    private final JWTUtil jwtUtil;

    public void reissueToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refresh = cookieUtil.getRefreshToken(request);

        if (refresh == null) {
            throw new IllegalArgumentException("Refresh token is null");
        }

        // 만료일 체크
        try {
            jwtUtil.isExpired(refresh);

        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("Refresh token is expired");
        }

        // 리프레시 토큰 확인
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals(JwtConstants.CATEGORY_REFRESH)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        // DB 체크
        Boolean isExists = refreshRepository.existsByRefreshToken(refresh);

        if (!isExists) {
            throw new IllegalArgumentException("Refresh token not found");
        }

        String username = jwtUtil.getUsername(refresh);
        Role role = Role.fromString(jwtUtil.getRole(refresh));

        // 재발급
        String newAccess = jwtUtil.createJwt(JwtConstants.CATEGORY_ACCESS, username, role, JwtConstants.ACCESS_TOKEN_EXPIRATION_TIME);
        String newRefresh = jwtUtil.createJwt(JwtConstants.CATEGORY_REFRESH, username, role, JwtConstants.REFRESH_TOKEN_EXPIRATION_TIME);

        // 기존 refresh 토큰 삭제
        refreshRepository.deleteByRefreshToken(refresh);

        refreshRepository.save(Refresh.createRefresh(username,
                newRefresh,
                new Date(System.currentTimeMillis() + JwtConstants.REFRESH_TOKEN_EXPIRATION_TIME)));

        response.addCookie(cookieUtil.createCookie(CookieConstants.ACCESS_TOKEN_COOKIE_NAME, newAccess));
        response.addCookie(cookieUtil.createCookie(CookieConstants.REFRESH_TOKEN_COOKIE_NAME, newRefresh));

        response.sendRedirect(request.getRequestURI());
    }

    public void removeRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieUtil.getRefreshToken(request);
        refreshRepository.deleteByRefreshToken(refreshToken);

        cookieUtil.clearAllCookie(request, response);
    }
}
