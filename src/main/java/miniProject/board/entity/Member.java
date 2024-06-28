package miniProject.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import miniProject.board.auth.constants.Role;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column
    private String nickname;

    @Column
    private String password;

    @Column
    private String description;

    @Column
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Member(String username, String password, String email, Role role) {
        this.username = username;
        this.nickname = username;
        this.password = password;
        this.description = "";
        this.email = email;
        this.role = role;
    }

    public static Member createMember(String username, String password, String email, Role role) {
        return new Member(username, password, email, role);
    }

    public static Member createMemberWithoutPasswordAndEmail(String username, Role role) {
        return new Member(username, "", "", role);
    }

    public static Member createMemberWithoutEmail(String username, String password, Role role) {
        return new Member(username, password, "", role);
    }
}