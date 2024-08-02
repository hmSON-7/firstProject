package miniProject.board.service.comment;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.controller.constant.LogConst;
import miniProject.board.dto.CommentDto;
import miniProject.board.entity.Article;
import miniProject.board.entity.Comment;
import miniProject.board.entity.Member;
import miniProject.board.repository.ArticleRepository;
import miniProject.board.repository.CommentRepository;
import miniProject.board.repository.MemberRepository;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;


    @Override
    public Long createComment(Long memberId, Long articleId, CommentDto.CreateRequest createCommentRequest) {
        Member member = getMemberByIdOrThrow(memberId);

        Article article = getArticleByIdOrThrow(articleId);

        Comment comment = Comment.createComment(createCommentRequest.getContent(), article, member);

        Comment savedComment = commentRepository.save(comment);

        return savedComment.getId();
    }


    @Override
    @Transactional(readOnly = true)
    public Page<CommentDto.Response> findCommentsByArticleId(Long articleId, Pageable pageable) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 접근입니다"));
        Page<Comment> comments = commentRepository.findByArticle(article, pageable);
        return comments.map(CommentDto.Response::new);
    }

    public Page<CommentDto.Response> getCommentsByMember(Long memberId, Pageable pageable) {
        return commentRepository
                .findCommentsByMemberId(memberId, pageable)
                .map(CommentDto.Response::new);
    }

    @Override
    public Long deleteComment(Long commentId, Long memberId, String deleteFrom) {
        Comment comment = getCommentByIdAndMemberIdOrThrow(commentId, memberId);

        Long source = null;

        if ("articles".equals(deleteFrom)) {
            source = comment.getArticle().getArticleId();

        } else if ("member".equals(deleteFrom)) {
            source = comment.getMember().getId();
        }

        commentRepository.delete(comment);

        return source;
    }


    @Override
    public Long updateComment(Long commentId,
                              Long memberId,
                              String updateFrom,
                              CommentDto.UpdateRequest updateCommentRequest) {

        Comment comment = getCommentByIdAndMemberIdOrThrow(commentId, memberId);

        Long source = null;

        if ("articles".equals(updateFrom)) {
            source = comment.getArticle().getArticleId();

        } else if ("member".equals(updateFrom)) {
            source = comment.getMember().getId();
        }

        comment.update(updateCommentRequest.getContent());

        return source;
    }


    private Member getMemberByIdOrThrow(Long memberId) {
       Member member =  memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.warn("[{}] 멤버 조회에 실패했습니다. 멤버 ID : {}", MDC.get(LogConst.TRACE_ID), memberId);

                    return new IllegalArgumentException("사용자가 존재하지 않습니다");
                });

        log.debug("[{}] 멤버 조회에 성공했습니다.", MDC.get(LogConst.TRACE_ID));

        return member;
    }

    private Article getArticleByIdOrThrow(Long articleId) {
        Article article = articleRepository
                .findById(articleId)
                .orElseThrow(() -> {
                    log.warn("[{}] 게시글 조회에 실패했습니다. 게시글 ID : {}", MDC.get(LogConst.TRACE_ID), articleId);

                    return new IllegalArgumentException("게시글이 존재하지 않습니다");
                });

        log.debug("[{}] 게시글 조회에 성공했습니다.", MDC.get(LogConst.TRACE_ID));

        return article;
    }

    private Comment getCommentByIdAndMemberIdOrThrow(Long commentId, Long memberId) {
        Comment comment =  commentRepository.findByIdAndMemberId(commentId, memberId)
                .orElseThrow(() -> {
                    log.warn("[{}] 댓글 조회에 실패했습니다. 댓글 ID : {}", MDC.get(LogConst.TRACE_ID), commentId);

                    return new IllegalArgumentException("댓글이 존재하지 않습니다.");
                });

        log.debug("[{}] 댓글 조회에 성공했습니다.", MDC.get(LogConst.TRACE_ID));

        return comment;
    }
}
