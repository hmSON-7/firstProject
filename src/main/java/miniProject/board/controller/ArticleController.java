package miniProject.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.controller.argumentResolver.Login;
import miniProject.board.dto.ArticleDto;
import miniProject.board.dto.MemberDto;
import miniProject.board.entity.Article;
import miniProject.board.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    // 필요한 메서드 : Article 리스트 제공, 수정 페이지로 이동, 수정 내용 저장

    // 게시글 단일 조회
    @GetMapping("/{articleId}")
    public Article show(@PathVariable Long articleId) {
        return articleService.read(articleId);
    }

    // 게시글 작성 페이지로 이동
    @GetMapping
    public String writeForm(Model model) {
        model.addAttribute("ArticleRequest", new ArticleDto.Create());

        return "/article/writeForm";
    }

    // 게시글 작성
    @PostMapping
    public String write(@Validated @ModelAttribute ArticleDto.Create articleCreateDto,
                         @Login MemberDto.Session memberSessionDto,
                         BindingResult bindingResult,
                         @RequestParam(defaultValue = "/") String redirectURL) {
        if (bindingResult.hasErrors()) {
            return "/article/writeForm";
        }

        Article written = articleService.create(articleCreateDto, memberSessionDto.getId());

        // 작성된 글을 바로 볼 수 있도록 해당 게시글 페이지로 이동
        return "redirect:/article/" + written.getArticleId();
    }

    // 게시글 삭제
    @PostMapping("/{articleId}/delete")
    public String delete(@Login MemberDto.Session memberSessionDto,
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
