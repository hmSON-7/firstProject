package miniProject.board.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import miniProject.board.entity.Report;
import miniProject.board.entity.ReportStatus;

import java.time.LocalDateTime;


public class ReportDto {

    @Data
    public static class Info {
        private Long reportId;
        private String description;
        private Long articleId;
        private Long commentId;
        private String username;
        private ReportStatus reportStatus;
        private LocalDateTime createdAt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReportArticle{
        private String description;
        private Long articleId;
        private Long memberId;

        public static ReportArticle create(Report report) {
            return new ReportArticle(report.getDescription(),
                    report.getArticle().getArticleId(),
                    report.getMember().getId());
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReportComment{
        private String description;
        private Long commentId;
        private Long memberId;

        public static ReportComment create(Report report) {
            return new ReportComment(report.getDescription(),
                    report.getComment().getCommentId(),
                    report.getMember().getId());
        }
    }

    @Data
    @AllArgsConstructor
    public static class ProcessReport{
        private Long reportId;
        private ReportStatus reportStatus;
    }



}
