package miniProject.board.repository;

import miniProject.board.entity.Article;
import miniProject.board.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    // 1. 이 사람이 이 글에 추천을 누른 상태인가? -> 유저 ID와 게시글 ID로 튜플 찾기
    Optional<Likes> findByArticleArticleIdAndMemberId(Long articleId, Long memberId);

    int countByArticle(Article article);
}
