package miniProject.board.repository;

import miniProject.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(Long id);

    Optional<Comment> findByIdAndMemberId(Long commentId, Long memberId);
}
