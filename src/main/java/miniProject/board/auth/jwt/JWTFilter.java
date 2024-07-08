package miniProject.board.auth.jwt;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.auth.constants.JwtConstants;
import miniProject.board.auth.constants.Role;
import miniProject.board.auth.utils.CookieUtil;
import miniProject.board.dto.CustomMemberDetails;
import miniProject.board.entity.Member;
import miniProject.board.service.auth.RefreshService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final RefreshService refreshService;
    private final CookieUtil cookieUtil;
    private final JWTUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (request.getDispatcherType() == DispatcherType.ERROR) {
            filterChain.doFilter(request, response);

            return;
        }


        // 로그 아웃 쿠키가 존재하면 accessToken 무시
        if (cookieUtil.findLogoutCookie(request, response)) {
            filterChain.doFilter(request, response);

            return;
        }

        String accessToken = cookieUtil.getAccessToken(request);

        // accessToken 없는 경우
        if (accessToken == null) {
            filterChain.doFilter(request, response);

            return;
        }

        // 토큰 시간 만료 확인
        try {
            jwtUtil.isExpired(accessToken);

        } catch (Exception e) {

            try {
                refreshService.reissueToken(request, response);
                return;

            } catch (IllegalArgumentException ex) {
                cookieUtil.clearAllCookie(request, response);

                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

                return;
            }
        }

        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals(JwtConstants.CATEGORY_ACCESS)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return;
        }

        String username = jwtUtil.getUsername(accessToken);
        Role role = Role.fromString(jwtUtil.getRole(accessToken));

        Member member = Member.createMemberWithoutPasswordAndEmail(username, role);
        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(customMemberDetails,
                        null, customMemberDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
