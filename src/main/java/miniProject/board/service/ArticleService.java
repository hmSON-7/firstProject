package miniProject.board.service;

import miniProject.board.dto.ArticleDto;
import miniProject.board.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface ArticleService {
    Page<ArticleDto.ArticlesList> index(Pageable pageable);

    Page<ArticleDto.ArticlesList> getArticlesByMember(Long memberId, Pageable pageable);

    ArticleDto.Info read(Long articleId, Boolean keepHits);

    Article create(ArticleDto.Create articleAddDto, Long memberId) throws IOException;

    Article update(ArticleDto.Create articleEditDto, Long articleId, Long memberId) throws IOException;

    void delete(Long articleId, Long memberId);

    void changeLikes(Long articleId, Long memberId);
}
