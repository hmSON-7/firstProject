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

        public Response(Comment comment) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            this.id = comment.getId();
            this.content = comment.getContent();
            this.username = comment.getMember().getUsername();
            this.createdAt = comment.getCreatedAt().format(formatter);
            this.updatedAt = comment.getUpdatedAt().format(formatter);
        }
    }
}
