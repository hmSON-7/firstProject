package miniProject.board.controller.config;

import lombok.RequiredArgsConstructor;
import miniProject.board.auth.constants.CookieConstants;
import miniProject.board.auth.jwt.CustomLogoutHandler;
import miniProject.board.auth.jwt.JWTFilter;
import miniProject.board.auth.jwt.JWTUtil;
import miniProject.board.auth.jwt.LoginFilter;
import miniProject.board.auth.utils.CookieUtil;
import miniProject.board.repository.RefreshRepository;
import miniProject.board.service.RefreshService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomLogoutHandler customLogoutHandler;
    private final RefreshRepository refreshRepository;
    private final RefreshService refreshService;
    private final CookieUtil cookieUtil;
    private final JWTUtil jwtUtil;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // csrf disable
        http
                .csrf((auth) -> auth.disable());

        // form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        // http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        // 경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/member/signUp", "/error").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())

                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .invalidateHttpSession(true)
                        .addLogoutHandler(customLogoutHandler)
                        .deleteCookies(CookieConstants.ACCESS_TOKEN_COOKIE_NAME, CookieConstants.REFRESH_TOKEN_COOKIE_NAME)
                        .logoutSuccessHandler((request, response, authentication)-> {
                            response.sendRedirect("/");
                        }));

        // add filter
        http
                .addFilterBefore(new JWTFilter(refreshService, cookieUtil, jwtUtil), LoginFilter.class);

        http
                .addFilterAt(new LoginFilter(authenticationConfiguration.getAuthenticationManager(),
                                refreshRepository,
                                cookieUtil,
                                jwtUtil),
                        UsernamePasswordAuthenticationFilter.class);

        // session setting
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

