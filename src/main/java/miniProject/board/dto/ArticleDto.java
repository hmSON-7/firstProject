package miniProject.board.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

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
        private String nickname;

        @NotEmpty
        private LocalDateTime createdAt;

        @NotEmpty
        private LocalDateTime updatedAt;

        @NotEmpty
        private int hits;

        @NotEmpty
        private int likes;
    }

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Create {
        @NotEmpty
        private String title;

        @NotEmpty
        @Size(min = 5)
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public static class ArticlesList {
        private Long articleId;
        private String title;
        private String nickname;
        private LocalDateTime updatedAt;
        private int likes;
        private boolean checkUpdate;

        public ArticlesList(Long articleId, String title, String nickname, LocalDateTime updatedAt, int likes, LocalDateTime createdAt) {
            this.articleId = articleId;
            this.title = title;
            this.nickname = nickname;
            this.updatedAt = updatedAt;
            this.likes = likes;
            this.checkUpdate = !createdAt.equals(updatedAt);
        }
    }
}