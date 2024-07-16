package miniProject.board.service.comment;

import miniProject.board.dto.CommentDto;

import java.util.List;

public interface CommentService {
    Long createComment(Long memberId, Long articleId, CommentDto.CreateRequest createCommentRequest);
    List<CommentDto.Response> findCommentsByArticleId(Long articleId);
    Long deleteComment(Long commentId, Long memberId, String deleteFrom);
    Long updateComment(Long commentId, Long memberId, String updateFrom, CommentDto.UpdateRequest updateCommentRequest);
}
