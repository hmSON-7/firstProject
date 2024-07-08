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
    public void save(String username, Long articleId, CommentDto.CommentRequest commentRequest) {

        Optional<Member> __member = memberRepository.findByUsername(username);
        Member member = null;

        if (__member.isPresent()) { // Optional이 값으로 채워져 있는지 확인
            member = __member.get(); // User 객체 추출
        } else {
            System.out.println("사용자가 존재하지 않습니다: " + username);
        }

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() ->
                        new IllegalArgumentException("게시글이 존재하지 않습니다.: "+ articleId));


        Comment comment = commentRequest.toEntity(member, article);
        commentRepository.save(comment);
    }

    /* 댓글 조회
    *  아티클 id 기준
    * */
    @Transactional(readOnly = true)
    public List<CommentDto.CommentResponse> get(Long articleId) {

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() ->
                        new IllegalArgumentException("게시글이 존재하지 않습니다."));

        List <Comment> comments = article.getComments();

        return comments.stream()
                .map(CommentDto.CommentResponse::new) // CommentResponse 객체로 변환
                .collect(Collectors.toList());
    }

    /*댓글 삭제
    * 아티클 id와 댓글 id로 삭제
    * */
    @Transactional
    public void delete(Long commentId, Long userId) {

        Comment comment = commentRepository.findByCommentId(commentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (!comment.getMember().getId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 댓글을 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

    /* 댓글 수정
    *
    * */
    @Transactional
    public void update(Long articleId,Long commentId, Long userId,
                       CommentDto.CommentRequest commentRequest) {

        articleRepository.findById(articleId).orElseThrow(() ->
                new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Comment comment = commentRepository.findByCommentId(commentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (!comment.getMember().getId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 댓글을 수정할 수 있습니다.");
        }

        comment.update(commentRequest.getContent());
    }

}

