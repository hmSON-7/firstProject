package miniProject.board.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.auth.constants.CookieConstants;
import miniProject.board.auth.constants.JwtConstants;
import miniProject.board.auth.constants.Role;
import miniProject.board.auth.utils.CookieUtil;
import miniProject.board.entity.Refresh;
import miniProject.board.repository.RefreshRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final RefreshRepository refreshRepository;
    private final CookieUtil cookieUtil;
    private final JWTUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        String username = authResult.getName();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        Role role = Role.fromString(auth.getAuthority());

        // 토큰 생성
        String access = jwtUtil.createJwt(JwtConstants.CATEGORY_ACCESS, username, role, JwtConstants.ACCESS_TOKEN_EXPIRATION_TIME);
        String refresh = jwtUtil.createJwt(JwtConstants.CATEGORY_REFRESH, username, role, JwtConstants.REFRESH_TOKEN_EXPIRATION_TIME);

        // Refresh 토큰 DB 저장
        Refresh refreshEntity = Refresh.createRefresh(username,
                refresh,
                new Date(System.currentTimeMillis() + JwtConstants.REFRESH_TOKEN_EXPIRATION_TIME));

        refreshRepository.save(refreshEntity);

        response.addCookie(cookieUtil.createCookie(CookieConstants.ACCESS_TOKEN_COOKIE_NAME, access));
        response.addCookie(cookieUtil.createCookie(CookieConstants.REFRESH_TOKEN_COOKIE_NAME, refresh));

        if (Role.ROLE_ADMIN == role) {
            response.sendRedirect("/admin/main");

        } else {
            response.sendRedirect("/");
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
