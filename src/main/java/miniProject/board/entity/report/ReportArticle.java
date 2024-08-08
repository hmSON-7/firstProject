package miniProject.board.entity.report;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import miniProject.board.entity.Article;
import miniProject.board.entity.Member;
import miniProject.board.entity.Report;
import miniProject.board.entity.ReportStatus;

@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue("ARTICLE")
public class ReportArticle extends Report {

    // 1개의 article과 여러개의 신고 연결
    @ManyToOne
    @JoinColumn (name = "article_id", nullable = false)
    private Article article;

    public ReportArticle(String description, Article article, Member member, ReportStatus status) {
        super(description, member, status);
        this.article = article;
    }
    public static ReportArticle reportArticle (String description, Article article, Member member, ReportStatus status) {
        return new ReportArticle(description, article, member, status);
    }
}
