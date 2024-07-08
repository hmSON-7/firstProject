package miniProject.board.service;

import miniProject.board.dto.ArticleDto;
import miniProject.board.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {
    Page<ArticleDto.ArticlesList> index(Pageable pageable);

    ArticleDto.Info read(Long articleId);

    Article create(ArticleDto.Create articleAddDto, Long memberId);

    Article update(ArticleDto.Create articleEditDto, Long articleId, Long memberId);

    void delete(Long articleId, Long memberId);
}
