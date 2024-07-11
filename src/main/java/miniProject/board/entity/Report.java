package miniProject.board.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 1개의 article과 여러개의 신고 연결
    @ManyToOne
    @JoinColumn (name = "article_id")
    private Article article;

    // 1개의 comment와 여러개의 신고 연결
    @ManyToOne
    @JoinColumn (name = "comment_id")
    private Comment comment;

    // 1개의 comment와 여러개의 신고 연결
    @ManyToOne
    @JoinColumn (name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private Report (String description, Article article, Comment comment, Member member, ReportStatus status) {
        this.description = description;
        this.article = article;
        this.comment = comment;
        this.member = member;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public static Report reportArticle (String description, Article article, Member member, ReportStatus status) {
        return new Report (description, article, null, member, status);
    }

    public static Report reportComment (String description, Comment comment, Member member, ReportStatus status) {
        return new Report (description, null, comment, member, status);
    }

    public void updateStatus(ReportStatus newStatus) {
        this.status = newStatus;
    }

}
