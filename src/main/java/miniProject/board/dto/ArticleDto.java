package miniProject.board.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import miniProject.board.entity.Article;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ArticleDto {

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Info {

        @NotEmpty
        private Long articleId;

        @NotEmpty
        private String title;

        @NotEmpty
        private String content;

        @NotEmpty
        private Long authorId;

        @NotEmpty
        private String nickname;

        @NotEmpty
        private String createdAt;

        @NotEmpty
        private String updatedAt;

        @NotEmpty
        private int hits;

        @NotEmpty
        private int likes;

        public Info(Long articleId, String title, String content, Long authorId, String nickname,
                    LocalDateTime createdAt, LocalDateTime updatedAt, int hits, int likes) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            this.articleId = articleId;
            this.title = title;
            this.content = content;
            this.authorId = authorId;
            this.nickname = nickname;
            this.createdAt = createdAt.format(formatter);
            this.updatedAt = updatedAt.format(formatter);
            this.hits = hits;
            this.likes = likes;
        }
    }

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Create {
        private String title;
        private MultipartFile content;
    }

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Load {
        @NotEmpty
        private String title;

        @NotEmpty
        private String content;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ArticlesList {
        private Long articleId;
        private String title;
        private String nickname;
        private String updatedAt;
        private int likes;
        private int hits;
        private boolean checkUpdate;

        public static ArticlesList fromArticle(Article article) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            return new ArticlesList(
                    article.getArticleId(),
                    article.getTitle(),
                    article.getMember().getNickname(),
                    article.getUpdatedAt().format(formatter),
                    article.getLikes(),
                    article.getHits(),
                    !article.getCreatedAt().equals(article.getUpdatedAt())
            );
        }
    }
}
