package miniProject.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userId;

    @Column
    private String nickname;

    @Column
    private String password;

    @Column
    private String description;

    @Column
    private String email;

    public Member(String userId, String password, String email) {
        this.userId = userId;
        this.nickname = userId;
        this.password = password;
        this.description = "";
        this.email = email;
    }
}