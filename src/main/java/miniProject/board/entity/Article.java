package miniProject.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    @Column
    private String title;

    @Column
    private String filePath;

    @Column
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column
    private int likes;

    public Article(String title, String filePath, UUID uuid, Member member) {
        this.title = title;
        this.filePath = filePath;
        this.uuid = uuid;
        this.member = member;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.likes = 0;
    }

    // Article 업데이트 메서드, 내용은 파일에 저장하므로 filePath를 따로 변경하지 않음
    public void update(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }

    // 조회수 증가 메서드. 누르면 조회수 1씩 증가
    public void checkLikes() {
        this.likes++;
    }
}
