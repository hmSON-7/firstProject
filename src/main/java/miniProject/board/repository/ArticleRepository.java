package miniProject.board.repository;

import miniProject.board.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAllByOrderByUpdatedAtDesc(Pageable pageable);
    Page<Article> findArticlesByMemberId(Long memberId, Pageable pageable);
}
