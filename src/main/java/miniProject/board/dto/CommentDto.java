package miniProject.board.dto;

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
        private Article article;
        private Member member;

        private Long commentId;
        private String content;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Comment toEntity(){
            return Comment.builder()
                    .article(article)
                    .member(member)
                    .commentId(commentId)
                    .content(content)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .build();
        }
    }

    @Getter
    public static class CommentResponse {
        private Long commentId;
        private Long articleId;
        private String username;
        private String content;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        // entitu -> dto
        public CommentResponse(Comment comment){
            this.commentId = comment.getCommentId();
            this.articleId = comment.getArticle().getArticleId();
            this.username = comment.getMember().getUsername();
            this.content = comment.getContent();
            this.createdAt = comment.getCreatedAt();
            this.updatedAt = comment.getUpdatedAt();
        }
    }

}
