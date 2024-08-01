package miniProject.board.entity.report;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import miniProject.board.entity.Comment;
import miniProject.board.entity.Member;
import miniProject.board.entity.Report;
import miniProject.board.entity.ReportStatus;

@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue("COMMENT")
public class ReportComment extends Report {

    // 1개의 comment와 여러개의 신고 연결
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public ReportComment(String description, Comment comment, Member member, ReportStatus status) {
        super(description, member, status);
        this.comment = comment;
    }
    public static ReportComment reportComment(String description, Comment comment, Member member, ReportStatus status) {
        return new ReportComment(description, comment, member, status);
    }
}
