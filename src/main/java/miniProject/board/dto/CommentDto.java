package miniProject.board.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import miniProject.board.entity.Article;
import miniProject.board.entity.Comment;
import miniProject.board.entity.Member;

import java.time.LocalDateTime;
import java.util.Optional;


public class CommentDto {

    @Data //getter, setter, requiredargs 등등 포함된 종합 어노테이션
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CommentRequest {
        @NotEmpty
        private Long articleId;
        @NotEmpty
        private String username;
        @NotEmpty
        private String content;

        public Comment toEntity(Member member, Article article){
            return Comment.builder()
                    .member(member)
                    .article(article)
                    .content(content)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }
    }

    @Getter
    public static class CommentResponse {
        @NotEmpty
        private Long commentId;
        @NotEmpty
        private String username;
        @NotEmpty
        private String content;
        @NotEmpty
        private LocalDateTime createdAt;
        @NotEmpty
        private LocalDateTime updatedAt;

        // entitu -> dto
        public CommentResponse(Comment comment){
            this.commentId = comment.getCommentId();
            this.username = comment.getMember().getUsername();
            this.content = comment.getContent();
            this.createdAt = comment.getCreatedAt();
            this.updatedAt = comment.getUpdatedAt();
        }
    }

}
