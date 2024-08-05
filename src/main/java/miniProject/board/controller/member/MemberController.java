package miniProject.board.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.controller.argumentResolver.Login;
import miniProject.board.dto.ArticleDto;
import miniProject.board.dto.CommentDto;
import miniProject.board.dto.MemberDto;
import miniProject.board.service.ArticleService;
import miniProject.board.service.comment.CommentService;
import miniProject.board.service.member.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ArticleService articleService;
    private final CommentService commentService;

    @GetMapping("/me")
    public String showMemberDetails(@Login MemberDto.Session memberSessionDto, Model model) {
        MemberDto.Info member = memberService.findMember(memberSessionDto.getId());

        model.addAttribute("member", member);

        return "member/detail";
    }

    @GetMapping("/me/articles")
    public String showArticles(@Login MemberDto.Session memberSessionDto,
                               @RequestParam(defaultValue = "1") int page,
                               Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10,
                Sort.by(Sort.Direction.DESC, "updatedAt"));

        Page<ArticleDto.ArticlesList> articles = articleService
                .getArticlesByMember(memberSessionDto.getId(), pageRequest);

        model.addAttribute("articles", articles.getContent());
        model.addAttribute("hasNextPage", articles.hasNext());
        model.addAttribute("hasPreviousPage", articles.hasPrevious());
        model.addAttribute("currentPage", articles.getNumber() + 1);

        return "member/articles";
    }

    @GetMapping("/me/comments")
    public String showComments(@Login MemberDto.Session memberSessionDto,
                               @RequestParam(defaultValue = "1") int page,
                               Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10,
                Sort.by(Sort.Direction.DESC, "updatedAt"));

        Page<CommentDto.Response> comments = commentService
                .getCommentsByMember(memberSessionDto.getId(), pageRequest);

        model.addAttribute("comments", comments.getContent());
        model.addAttribute("hasNextPage", comments.hasNext());
        model.addAttribute("hasPreviousPage", comments.hasPrevious());
        model.addAttribute("currentPage", comments.getNumber() + 1);


        return "member/comments";
    }
}