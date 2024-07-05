package miniProject.board.service;

import miniProject.board.dto.ArticleDto;
import miniProject.board.entity.Article;

import java.util.List;

public interface ArticleService {
    List<Article> index();

    Article read(Long articleId);

    Article create(ArticleDto.Create articleAddDto, Long memberId);

    Article update(ArticleDto.Info articleDto, Long articleId, Long memberId);

    boolean delete(Long articleId, Long memberId);
}
