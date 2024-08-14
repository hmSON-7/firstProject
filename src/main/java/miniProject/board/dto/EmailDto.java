package miniProject.board.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

public class EmailDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class RequestForUsername {
        @NotEmpty
        @Email
        private String email;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class RequestForPW {
        @NotEmpty
        private String username;

        @NotEmpty
        @Email
        private String email;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CheckForUsername {
        @NotEmpty
        @Email
        private String email;

        @NotEmpty
        private String sentCode;

        @NotEmpty
        private String authCode;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CheckForPW {
        @NotEmpty
        private String username;

        @NotEmpty
        @Email
        private String email;

        @NotEmpty
        private String sentCode;

        @NotEmpty
        private String authCode;
    }
}
