package miniProject.board.service;


import lombok.RequiredArgsConstructor;
import miniProject.board.dto.CommentDto;
import miniProject.board.entity.Article;
import miniProject.board.entity.Comment;
import miniProject.board.entity.Member;
import miniProject.board.repository.ArticleRepository;
import miniProject.board.repository.CommentRepository;
import miniProject.board.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service

public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    /* 댓글 저장하기
        멤버 확인 후 아티클 여부 확인
    * */
    @Transactional
    public Long commentSave(String username, Long id, CommentDto.CommentRequest commentRequest) {
        Optional<Member> __member = memberRepository.findByUsername(username);
        Member member;
        if (__member.isPresent()) { // Optional이 값으로 채워져 있는지 확인
            member = __member.get(); // User 객체 추출
        } else {
            System.out.println("사용자가 존재하지 않습니다: " + username);
            return null;
        }

        Article article = articleRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("게시글이 존재하지 않습니다."));

        commentRequest.setMember(member);
        commentRequest.setArticle(article);

        Comment comment = commentRequest.toEntity();
        commentRepository.save(comment);

        return commentRequest.getCommentId();
    }

    /* 댓글 조회
    *  아티클 id 기준
    * */
    @Transactional(readOnly = true)
    public List<Comment> findAll(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("게시글이 존재하지 않습니다."));
        List<Comment> comments = article.getComments();
        return comments;
    }

    /*댓글 삭제
    * 아티클 id와 댓글 id로 삭제
    * */
    @Transactional
    public void delete(Long articleId, Long commentId) {
        Comment comment = commentRepository.findByArticleIdAndCommentId(articleId, commentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("댓글이 존재하지 않습니다."));

        commentRepository.delete(comment);
    }

    /* 댓글 수정
    *
    * */
    @Transactional
    public void update(Long articleId, Long commentId, String username,
                       CommentDto.CommentRequest commentRequest) {
        Comment comment = commentRepository.findByArticleIdAndCommentId(articleId, commentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (!comment.getMember().getUsername().equals(username)) {
            throw new IllegalArgumentException("작성자만 댓글을 수정할 수 있습니다.");
        }

        comment.update(commentRequest.getContent(), commentRequest.getUpdatedAt());
    }

}

