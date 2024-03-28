package miniProject.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.controller.argumentResolver.Login;
import miniProject.board.dto.ArticleAddDto;
import miniProject.board.dto.MemberSessionDto;
import miniProject.board.entity.Article;
import miniProject.board.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    // 게시글 단일 조회
    @GetMapping("/{articleId}")
    public Article show(@PathVariable Long articleId) {
        return articleService.show(articleId);
    }

    // 게시글 작성 페이지로 이동
    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("ArticleAddDto", new ArticleAddDto());

        return "article/writeForm";
    }

    // 게시글 작성
    @PostMapping("/write")
    public String write(@Validated @ModelAttribute ArticleAddDto articleAddDto,
                         @Login MemberSessionDto memberSessionDto,
                         BindingResult bindingResult,
                         @RequestParam(defaultValue = "/") String redirectURL) {
        if (bindingResult.hasErrors()) {
            return "/article/writeForm";
        }

        Article writed = articleService.write(articleAddDto, memberSessionDto.getId());

        // 작성된 글을 바로 볼 수 있도록 해당 게시글 페이지로 이동
        return "redirect:/article/" + writed.getArticleId();
    }

    // 게시글 삭제
    @PostMapping("/{articleId}/delete")
    public String delete(@Login MemberSessionDto memberSessionDto,
                       @PathVariable Long articleId) {
        if(memberSessionDto == null) {
            return "redirect:/member/login";
        }

        boolean delete = articleService.delete(articleId, memberSessionDto.getId());

        if(!delete) {
            return "/member/login";
        }

        return "/article";
    }
}
