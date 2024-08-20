package miniProject.board.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import miniProject.board.entity.Member;

import static miniProject.board.dto.constants.MemberConst.PASSWORD_MAX_SIZE;
import static miniProject.board.dto.constants.MemberConst.PASSWORD_MIN_SIZE;

public class MemberDto {

    @Getter @Setter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Info {
        @NotEmpty
        private Long id;

        @NotEmpty
        private String username;

        @NotEmpty
        private String nickname;

        @NotEmpty
        @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE)
        private String password;

        private String description;

        @Email
        private String email;

        public static Info fromMember(Member member) {
            return new Info(member.getId(),
                    member.getUsername(),
                    member.getNickname(),
                    member.getPassword(),
                    member.getDescription(),
                    member.getEmail());
        }
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Create {
        @NotEmpty
        private String username;

        @NotEmpty
        @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE)
        private String password;

        @NotEmpty
        @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE)
        private String confirmPassword;

        @Email
        private String email;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Login {
        @NotEmpty
        private String username;

        @NotEmpty
        @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE)
        private String password;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ChangePW {
        @NotEmpty
        private String username;

        @NotEmpty
        @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE)
        private String password;

        @NotEmpty
        @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE)
        private String confirmPassword;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangeNickname {
        @NotEmpty
        private String nickname;
    }

    @Getter @Setter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Session {
        private Long id;

        public static Session fromMember(Member member) {
            return new Session(member.getId());
        }
    }

}
