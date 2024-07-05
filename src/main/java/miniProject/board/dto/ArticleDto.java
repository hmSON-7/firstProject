package miniProject.board.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import miniProject.board.entity.Article;
import miniProject.board.entity.Member;

import java.time.LocalDateTime;
import java.util.UUID;

public class ArticleDto {

    @Getter @Setter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Info {

        @NotEmpty
        private Long articleId;

        @NotEmpty
        private String title;

        @NotEmpty
        private String filePath;

        @NotEmpty
        private UUID uuid;

        @NotEmpty
        private Member member;

        @NotEmpty
        private LocalDateTime createdAt;

        @NotEmpty
        private LocalDateTime updatedAt;

        @NotEmpty
        private int likes;

        public static ArticleDto.Info fromArticle(Article article) {
            return new ArticleDto.Info(
                    article.getArticleId(),
                    article.getTitle(),
                    article.getFilePath(),
                    article.getUuid(),
                    article.getMember(),
                    article.getCreatedAt(),
                    article.getUpdatedAt(),
                    article.getLikes()
            );
        }
    }

    @Getter @Setter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class Create {
        @NotEmpty
        private String title;

        @NotEmpty
        @Size(min = 5)
        private String content;
    }
}