package miniProject.board.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import miniProject.board.entity.Member;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {
    @NotEmpty
    private Long articleId;

    @NotEmpty
    private String title;

    @NotEmpty
    @Size(min = 5)
    private String content;

    @NotEmpty
    private Member member;

    @NotEmpty
    private LocalDateTime createdAt;

    @NotEmpty
    private LocalDateTime updatedAt;
}