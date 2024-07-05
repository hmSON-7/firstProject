package miniProject.board.repository;

import miniProject.board.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAllByOrderByUpdatedAtDesc(Pageable pageable);
}
