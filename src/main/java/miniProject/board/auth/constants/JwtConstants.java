package miniProject.board.auth.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtConstants {
    public static final String CATEGORY_ACCESS = "access";
    public static final String CATEGORY_REFRESH = "refresh";

    public static final Long ACCESS_TOKEN_EXPIRATION_TIME = 600000L;
    public static final Long REFRESH_TOKEN_EXPIRATION_TIME = 86400000L;
}
