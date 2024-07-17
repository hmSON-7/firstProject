package miniProject.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.controller.argumentResolver.Login;
import miniProject.board.controller.constant.LogConst;
import miniProject.board.dto.CommentDto;
import miniProject.board.dto.MemberDto;
import miniProject.board.service.comment.CommentService;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 저장
    @PostMapping("/articles/{articleId}/comments")
    public String addComment(@PathVariable Long articleId,
                             CommentDto.CreateRequest createCommentRequest,
                             @Login MemberDto.Session memberSessionDto){

        log.info("[{}] 사용자 {}가 게시글 {}에 댓글을 생성합니다.",
                MDC.get(LogConst.TRACE_ID), memberSessionDto.getId(), articleId);

        commentService.createComment(memberSessionDto.getId(), articleId, createCommentRequest);

        log.info("[{}] 댓글 생성이 완료되었습니다.", MDC.get(LogConst.TRACE_ID));

        return "redirect:/articles/" + articleId;
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public String removeComment(@PathVariable Long commentId,
                                @RequestParam(value = "deleteFrom", required = false) String deleteFrom,
                                @Login MemberDto.Session memberSessionDto) {

        log.info("[{}] 사용자 {}가 댓글 {}를 삭제합니다.",
                MDC.get(LogConst.TRACE_ID), memberSessionDto.getId(), commentId);

        if (deleteFrom.equals("articles")) {
            Long source = commentService.deleteComment(commentId, memberSessionDto.getId(), deleteFrom);

            log.info("[{}] 게시글 페이지에서 댓글 삭제가 완료되었습니다.", MDC.get(LogConst.TRACE_ID));

            return "redirect:/articles/" + source;
        }

        else if (deleteFrom.equals("member")) {
            Long source = commentService.deleteComment(commentId, memberSessionDto.getId(), deleteFrom);

            log.info("[{}] 멤버 상세 페이지에서 댓글 삭제가 완료되었습니다.", MDC.get(LogConst.TRACE_ID));

            return "redirect:/";
        }

        else {
            log.warn("[{}] 잘못된 댓글 삭제 요청", MDC.get(LogConst.TRACE_ID));

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    // 댓글 수정
    @PatchMapping("/comments/{commentId}")
    public String editComment(@PathVariable Long commentId,
                              @RequestParam(value = "updateFrom", required = false) String updateFrom,
                              @Login MemberDto.Session memberSessionDto,
                              CommentDto.UpdateRequest commentUpdateRequest) {

        log.info("[{}] 사용자 {}가 댓글 {}를 수정합니다.",
                MDC.get(LogConst.TRACE_ID), memberSessionDto.getId(), commentId);

        if (updateFrom.equals("articles")) {
            Long source =  commentService
                    .updateComment(commentId, memberSessionDto.getId(), updateFrom, commentUpdateRequest);

            log.info("[{}] 게시글 페이지에서 댓글 수정이 완료되었습니다.", MDC.get(LogConst.TRACE_ID));

            return "redirect:/articles/" + source;
        }

        else if (updateFrom.equals("member")) {
            Long source =  commentService
                    .updateComment(commentId, memberSessionDto.getId(), updateFrom, commentUpdateRequest);

            log.info("[{}] 멤버 상세 페이지에서 댓글 수정이 완료되었습니다.", MDC.get(LogConst.TRACE_ID));
            
            return "redirect:/";
        }

        else {
            log.warn("[{}] 잘못된 댓글 수정 요청", MDC.get(LogConst.TRACE_ID));

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}
