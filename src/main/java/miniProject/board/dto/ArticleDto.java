package miniProject.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {
    private Long id;
    private Long memberId;
    private String title;
    private String content;
}