package miniProject.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private Member(String username, String password, String email) {
        this.username = username;
        this.nickname = username;
        this.password = password;
        this.description = "";
        this.email = email;
    }

    public static Member createMember(String username, String password, String email) {
        return new Member(username, password, email);
    }
}