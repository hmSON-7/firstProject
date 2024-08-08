package miniProject.board.service.comment;

import miniProject.board.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 댓글 관련 서비스 인터페이스입니다.
 */
public interface CommentService {

    /**
     * 멤버 ID와 게시글 ID를 기반으로 댓글을 생성합니다.
     *
     * @param memberId 댓글을 작성하는 멤버 Id
     * @param articleId 댓글이 달릴 게시글의 ID
     * @param createCommentRequest 댓글 내용
     *
     * @return 생성된 댓글의 ID
     *
     * @throws IllegalArgumentException 멤버 조회에 실패한 경우 또는 게시글 조회에 실패한 경우
     */
    Long createComment(Long memberId, Long articleId, CommentDto.CreateRequest createCommentRequest);

    /**
     * 게시글 ID와 Pageable을 이용하여 현재 페이지에 해당하는 댓글들을 리스트로 제공합니다.
     *
     * @param articleId 조회한 게시글의 ID
     * @param pageable 페이지
     *
     * @return
     */
    Page<CommentDto.Response> findCommentsByArticleId(Long articleId, Pageable pageable);


    /**
     * 댓글 ID와 멤버 ID를 기반으로 댓글을 삭제합니다.
     *
     * @param commentId 삭제할 댓글 ID
     * @param memberId 멤버 ID
     * @param deleteFrom 삭제 요청 페이지 구분 문자열
     *
     * @return 게시글에서 삭제 시 게시글 ID 반환, 멤버에서 삭제 시 멤버 ID 반환
     *
     * @throws IllegalArgumentException 댓글 조회에 실패한 경우
     */
    Long deleteComment(Long commentId, Long memberId, String deleteFrom);


    /**
     * 댓글 ID와 멤버 ID를 기반으로 댓글을 수정합니다.
     *
     * @param commentId 수정할 댓글 ID
     * @param memberId 멤버 ID
     * @param updateFrom 업데이트 요청 페이지 구분 문자열
     * @param updateCommentRequest 업데이트할 댓글의 내용
     *
     * @return 게시글에서 수정 시 게시글 ID 반환, 멤버에서 수정 시 멤버 ID 반환
     *
     * @throws IllegalArgumentException 댓글 조회에 실패한 경우
     */
    Long updateComment(Long commentId, Long memberId, String updateFrom, CommentDto.UpdateRequest updateCommentRequest);

    Page<CommentDto.MyPageResponse> getCommentsByMember(Long MemberId, Pageable pageable);
}
