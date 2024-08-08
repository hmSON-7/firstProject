package miniProject.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import miniProject.board.auth.constants.Role;
import miniProject.board.auth.constants.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime lockExpirationTime;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    private Member(String username, String password, String email, Role role) {
        this.username = username;
        this.nickname = username;
        this.password = password;
        this.description = "";
        this.email = email;
        this.role = role;

        this.status = Status.ACTIVE;
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


    /**
     * Member를 활성화합니다.
     *
     * <p>
     *     Member의 상태를 ACTIVE로 변경하고 잠금 만료 시간을 초기화합니다.
     * </p>
     */
    public void activate() {
        this.status = Status.ACTIVE;
        this.lockExpirationTime = null;
    }


    /**
     * Member를 영구 정지 처리합니다.
     *
     * <p>
     *     Member의 상태를 PERMANENT_SUSPENDED로 변경하고 잠금 만료 시간을 100년 후로 설정합니다.
     * </p>
     */
    public void suspendPermanently() {
        this.status = Status.PERMANENT_SUSPENDED;
        this.lockExpirationTime = LocalDateTime.now().plusYears(100);
    }


    /**
     * Member를 임시 정지 처리합니다.
     *
     * <p>
     *     Member의 상태를 TEMPORARY_SUSPENDED로 변경하고 잠금 만료 시간을 지정된 기간 후로 설정합니다.
     * </p>
     * @param days 정지 기간(일 단위)
     */
    public void suspendTemporarily(long days) {
        this.status = Status.TEMPORARY_SUSPENDED;
        this.lockExpirationTime = LocalDateTime.now().plusDays(days);
    }
}
