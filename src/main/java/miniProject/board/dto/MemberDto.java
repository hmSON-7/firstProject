package miniProject.board.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import miniProject.board.entity.Member;

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
        @Size(min = 8, max = 15)
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
        @Size(min = 8, max = 15)
        private String password;

        @NotEmpty
        @Size(min = 8, max = 15)
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
        @Size(min = 8, max = 15)
        private String password;
    }

    public static class EmailRequest {

        @Getter @Setter
        @NoArgsConstructor
        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        public static class forUsername {
            @NotEmpty
            @Email
            private String email;
        }

        @Getter @Setter
        @NoArgsConstructor
        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        public static class forPW {
            @NotEmpty
            private String username;

            @NotEmpty
            @Email
            private String email;
        }
    }

    public static class EmailCheck {

        @Getter @Setter
        @NoArgsConstructor
        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        public static class forUsername {
            @NotEmpty
            @Email
            private String email;

            @NotEmpty
            private String authCode;
        }

        @Getter @Setter
        @NoArgsConstructor
        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        public static class forPW {
            @NotEmpty
            private String username;

            @NotEmpty
            @Email
            private String email;

            @NotEmpty
            private String authCode;
        }
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
