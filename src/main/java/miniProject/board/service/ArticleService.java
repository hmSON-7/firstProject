package miniProject.board.service;

import miniProject.board.dto.ArticleDto;
import miniProject.board.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleService {
    Page<ArticleDto.ArticlesList> index(Pageable pageable);

    ArticleDto.Info read(Long articleId);

    Article create(ArticleDto.Create articleAddDto, Long memberId);

    Article update(ArticleDto.Info articleDto, Long articleId, Long memberId);

    boolean delete(Long articleId, Long memberId);
}
