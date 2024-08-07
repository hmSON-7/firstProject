package miniProject.board.dto;

import lombok.*;
import miniProject.board.entity.Comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentDto {

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        private String content;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String content;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String content;
        private String username;
        private String createdAt;
        private String updatedAt;
        private Long authorId;

        public Response(Comment comment) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            this.id = comment.getId();
            this.content = comment.getContent();
            this.username = comment.getMember().getUsername();
            this.createdAt = comment.getCreatedAt().format(formatter);
            this.updatedAt = comment.getUpdatedAt().format(formatter);
            this.authorId = comment.getMember().getId();
        }
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageResponse {
        private Long id;
        private String content;
        private String username;
        private String updatedAt;
        private Long articleId;
        private String articleTitle;

        public MyPageResponse(Comment comment) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            this.id = comment.getId();
            this.content = comment.getContent();
            this.username = comment.getMember().getUsername();
            this.updatedAt = comment.getUpdatedAt().format(formatter);
            this.articleId = comment.getArticle().getArticleId();
            this.articleTitle = comment.getArticle().getTitle();
        }

    }
}
