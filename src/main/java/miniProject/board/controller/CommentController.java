package miniProject.board.controller;

import lombok.RequiredArgsConstructor;
import miniProject.board.controller.argumentResolver.Login;
import miniProject.board.dto.CommentDto;
import miniProject.board.dto.MemberDto;
import miniProject.board.entity.Comment;
import miniProject.board.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/article/{articleId}")
@Controller
public class CommentController {
    private final CommentService commentService;

    // 댓글 저장
    @PostMapping("/comments")
    public String save(@PathVariable Long articleId,
                               @RequestBody CommentDto.CommentRequest request,
                               @Login MemberDto.Info memberSessionDto){

        Long commentId = commentService.save(memberSessionDto.getUsername(), articleId, request);

        return "redirect:/article/" + articleId + "/comments";
    }

    // 모든 댓글 조회
    @GetMapping("/comments")
    public String read(@PathVariable long articleId) {
        commentService.read(articleId);
        return "redirect:/article/" + articleId;
    }

    //댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public String delete(@PathVariable long articleId, @PathVariable Long commentId) {
        commentService.delete(articleId, commentId);
        return "redirect:/article/" + articleId + "/comments";
    }

    //댓글 수정
    @PutMapping({"/comments/{commentId}"})
    public String update(@PathVariable long articleId, @PathVariable Long commentId,
                                       @Login MemberDto.Info memberSessionDto,
                                       @RequestBody CommentDto.CommentRequest commentRequest) {
        commentService.update(articleId, commentId, memberSessionDto.getUsername(), commentRequest);
        return "redirect:/article/" + articleId + "/comments/" + commentId;
    }

}
