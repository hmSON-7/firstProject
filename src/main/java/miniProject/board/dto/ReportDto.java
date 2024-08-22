package miniProject.board.dto;


import lombok.*;
import miniProject.board.entity.ReportStatus;
import miniProject.board.entity.report.ReportArticle;
import miniProject.board.entity.report.ReportComment;

import java.time.LocalDateTime;


public class ReportDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArticleRequest{
        private String description;
        private Long articleId;
        private Long memberId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private String description;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageResponse {
        private Long id;
        private String reporter;
        private String description;
        private LocalDateTime updateAtReport;
        private ReportStatus reportStatus;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentResponse {
        private Long id;
        private String reporter;
        private String description;
        private LocalDateTime updateAtReport;
        private String author;
        private String content;
        private LocalDateTime updatedAtComment;


        public static CommentResponse fromReportComment(ReportComment reportComment) {
            return new CommentResponse(
                    reportComment.getId(),
                    reportComment.getMember().getUsername(),
                    reportComment.getDescription(),
                    reportComment.getUpdatedAt(),
                    reportComment.getComment().getMember().getUsername(),
                    reportComment.getComment().getContent(),
                    reportComment.getComment().getUpdatedAt());
        }
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentListResponse {
        private Long id;
        private String reporter;
        private String description;
        private LocalDateTime updateAtReport;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArticleResponse {
        private Long id;
        private String reporter;
        private String description;
        private LocalDateTime updateAtReport;

        private String author;
        private String title;
        private String content;
        private LocalDateTime updatedAtArticle;

        public static ArticleResponse fromReportArticle(ReportArticle reportArticle, String content) {
            return new ArticleResponse(reportArticle.getId(),
                    reportArticle.getMember().getUsername(),
                    reportArticle.getDescription(),
                    reportArticle.getUpdatedAt(),
                    reportArticle.getArticle().getMember().getUsername(),
                    reportArticle.getArticle().getTitle(),
                    content,
                    reportArticle.getArticle().getUpdatedAt());
        }

    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArticleListResponse {
        private Long id;
        private String reporter;
        private String description;
        private LocalDateTime updateAtReport;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProcessRequest {
        private Long date;
    }
}
