package miniProject.board.repository;

import miniProject.board.entity.Article;
import miniProject.board.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndMemberId(Long commentId, Long memberId);

    Page<Comment> findByArticle(Article article, Pageable pageable);

    @EntityGraph(attributePaths = {"article", "member"})
    Page<Comment> findCommentsByMemberId(Long memberId, Pageable pageable);
}
