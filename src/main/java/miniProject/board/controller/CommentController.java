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

@RequiredArgsConstructor
@RequestMapping("/article/{articleId}")
@Controller
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity save(@PathVariable Long id, @RequestBody CommentDto.CommentRequest request,
                                        @Login MemberDto.Info memberSessionDto){
        return ResponseEntity.ok(commentService.commentSave(memberSessionDto.getUsername(), id, request));
    }
}
