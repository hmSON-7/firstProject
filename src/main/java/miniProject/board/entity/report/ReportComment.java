package miniProject.board.entity.report;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import miniProject.board.entity.Comment;
import miniProject.board.entity.Member;
import miniProject.board.entity.ReportStatus;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@DiscriminatorValue("COMMENT")
public class ReportComment extends Report {

    // 1개의 comment와 여러개의 신고 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private ReportComment(String description, Comment comment, Member member) {
        super(description, member);
        this.comment = comment;
    }
    public static ReportComment createReportComment(String description, Comment comment, Member member) {
        return new ReportComment(description, comment, member);
    }
}
