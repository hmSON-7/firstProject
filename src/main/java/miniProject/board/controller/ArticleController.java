package miniProject.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.controller.argumentResolver.Login;
import miniProject.board.dto.ArticleDto;
import miniProject.board.dto.MemberDto;
import miniProject.board.entity.Article;
import miniProject.board.service.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    // 1. 게시글 작성 페이지로 이동
    @GetMapping
    public String writeForm(Model model) {
        model.addAttribute("ArticleRequest", new ArticleDto.Create());

        return "/article/writeForm";
    }

    // 2. 게시글 작성
    @PostMapping
    public String write(@Validated @ModelAttribute ArticleDto.Create articleCreateDto,
                         @Login MemberDto.Session memberSessionDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/article/writeForm";
        }

        Article written = articleService.create(articleCreateDto, memberSessionDto.getId());

        // 작성된 글을 바로 볼 수 있도록 해당 게시글 페이지로 이동
        return "redirect:/article/" + written.getArticleId();
    }

    // 3. 게시글 단일 조회
    @GetMapping("/{articleId}")
    public ArticleDto.Info show(@PathVariable Long articleId) {
        return articleService.read(articleId);
    }

    // 4. 게시글 리스트 조회
    @GetMapping("/articles")
    public String index(@RequestParam(defaultValue = "1") int page, Model model) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<ArticleDto.ArticlesList> articles = articleService.index(pageable);

        model.addAttribute("articles", articles.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articles.getTotalPages());

        return "article/list";
    }

    // 5. 게시글 수정 페이지로 이동

    // 6. 게시글 수정

    // 7. 게시글 삭제
    @DeleteMapping("/{articleId}")
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
