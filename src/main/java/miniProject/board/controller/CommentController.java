package miniProject.board.controller;

import lombok.RequiredArgsConstructor;
import miniProject.board.controller.argumentResolver.Login;
import miniProject.board.dto.CommentDto;
import miniProject.board.dto.MemberDto;
import miniProject.board.entity.Comment;
import miniProject.board.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
                       CommentDto.CommentRequest request,
                       @Login MemberDto.Session memberSessionDto){

        commentService.save(memberSessionDto.getId(), articleId, request);

        return "redirect:/article/" + articleId + "/comments/";
    }

    // 모든 댓글 조회
    @GetMapping("/comments")
    public String read(@PathVariable long articleId, Model model) {

        List<CommentDto.CommentResponse> comments = commentService.get(articleId);
        model.addAttribute("comments", comments);

        return "redirect:/article/" + articleId + "/comments";
    }

    //댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public String delete(@PathVariable long articleId, @PathVariable Long commentId) {

        commentService.delete(articleId, commentId);
        return "redirect:/article/" + articleId + "/comments";
    }

    //댓글 수정
    @PutMapping("/comments/{commentId}")
    public String update(@PathVariable long articleId, @PathVariable Long commentId,
                         @Login MemberDto.Session memberSessionDto,
                         CommentDto.CommentRequest commentRequest) {

        commentService.update(articleId, commentId, memberSessionDto.getId(), commentRequest);
        return "redirect:/article/" + articleId + "/comments/" + commentId;
    }

}
