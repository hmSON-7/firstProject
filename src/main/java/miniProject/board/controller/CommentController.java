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
    public ResponseEntity<Long> save(@PathVariable Long articleId,
                               @RequestBody CommentDto.CommentRequest request,
                               @Login MemberDto.Info memberSessionDto){

        Long commentId = commentService.save(memberSessionDto.getUsername(), articleId, request);

        return ResponseEntity.ok(commentId);
    }

    // 모든 댓글 조회
    @GetMapping("/comments")
    public List<Comment> read(@PathVariable long articleId) {
        return commentService.read(articleId);
    }

    //댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Long> delete(@PathVariable long articleId, @PathVariable Long commentId) {
        commentService.delete(articleId, commentId);
        return ResponseEntity.ok(commentId);
    }

    //댓글 수정
    @PutMapping({"/comments/{commentId}"})
    public ResponseEntity<Long> update(@PathVariable long articleId, @PathVariable Long commentId,
                                       @Login MemberDto.Session memberSessionDto,
                                       @RequestBody CommentDto.CommentRequest commentRequest) {
        commentService.update(articleId, commentId, memberSessionDto.getId(), commentRequest);
        return ResponseEntity.ok(commentId);
    }

}
