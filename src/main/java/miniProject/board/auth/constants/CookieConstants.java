package miniProject.board.auth.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieConstants {
    public static final String ACCESS_TOKEN_COOKIE_NAME = "access";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh";
    public static final String LOGOUT_COOKIE_NAME = "logout";
}
