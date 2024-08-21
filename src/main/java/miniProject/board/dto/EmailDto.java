package miniProject.board.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import static miniProject.board.dto.constants.MemberConst.PASSWORD_MAX_SIZE;
import static miniProject.board.dto.constants.MemberConst.PASSWORD_MIN_SIZE;

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
    public static class Auth {
        @NotEmpty
        private String Code;
    }
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ChangePW {

        @NotEmpty
        @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE)
        private String password;

        @NotEmpty
        @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE)
        private String confirmPassword;
    }
}