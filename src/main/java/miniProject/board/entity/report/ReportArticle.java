package miniProject.board.entity.report;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import miniProject.board.entity.Article;
import miniProject.board.entity.Member;
import miniProject.board.entity.ReportStatus;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@DiscriminatorValue("ARTICLE")
public class ReportArticle extends Report {

    // 1개의 article과 여러개의 신고 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "article_id", nullable = false)
    private Article article;

    private ReportArticle(String description, Article article, Member member) {
        super(description, member);
        this.article = article;
    }
    public static ReportArticle createReportArticle (String description, Article article, Member member) {
        return new ReportArticle(description, article, member);
    }
}
