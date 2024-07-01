package miniProject.board.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /*
     * 댓 업데이트 시간
     * 댓 수정시 갱신되는 속성*/
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 1개의 article과 여러개의 댓글의 연결
    @ManyToOne
    @JoinColumn (name = "article_id")
    private Article article;

    // 1명의 유저와 여러개의 댓글 연결
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
