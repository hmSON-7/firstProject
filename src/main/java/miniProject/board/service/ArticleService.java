package miniProject.board.service;

import miniProject.board.dto.ArticleAddDto;
import miniProject.board.dto.ArticleDto;
import miniProject.board.entity.Article;

import java.util.List;

public interface ArticleService {
    List<Article> index();

    Article show(Long articleId);

    Article create(ArticleAddDto articleAddDto, Long memberId);

    Article update(ArticleDto articleDto, Long memberId);

    boolean delete(Long articleId);
}
