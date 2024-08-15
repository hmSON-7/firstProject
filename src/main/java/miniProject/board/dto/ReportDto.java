package miniProject.board.dto;


import lombok.*;
import miniProject.board.entity.ReportStatus;

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
    public static class CommentResponse {
        private String reporter;
        private String description;
        private LocalDateTime updateAtReport;
        private String author;
        private String content;
        private LocalDateTime updatedAtComment;
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
        private String reporter;
        private String description;
        private String author;
        private String content;
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
    public static class ProcessRequest {
        private Long date;
    }
}
