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

@Slf4j
@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    // 1. 게시글 작성 페이지로 이동
    @GetMapping
    public String writeForm(Model model) {
        model.addAttribute("ArticleRequest", new ArticleDto.Create());

        return "/articles/writeForm";
    }

    // 2. 게시글 작성
    @PostMapping
    public String write(@Validated @ModelAttribute ArticleDto.Create articleCreateDto,
                        @Login MemberDto.Session memberSessionDto,
                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/articles/writeForm";
        }

        if(memberSessionDto == null) {
            return "redirect:/login";
        }

        try {
            Article written = articleService.create(articleCreateDto, memberSessionDto.getId());
            log.info("게시글 작성 완료: {}", written);
            log.info("작성된 게시글 ID: {}", written.getArticleId());
            return "redirect:/articles/" + written.getArticleId();
        } catch (Exception e) {
            log.error("게시글 작성 중 오류 발생", e);
            return "redirect:/error"; // 또는 오류 페이지로 리디렉션
        }
    }

    // 3. 게시글 단일 조회
    @GetMapping("/{articleId}")
    public String show(@PathVariable("articleId") Long articleId, Model model) {
        ArticleDto.Info article = articleService.read(articleId);
        log.debug("DB 조회 확인");
        model.addAttribute("ArticleInfo", article);
        log.debug("모델 확인");

        return "articles/articleView";
    }

    // 4. 게시글 리스트 조회
    @GetMapping("/articles")
    public String index(@RequestParam(defaultValue = "1") int page, Model model) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<ArticleDto.ArticlesList> articles = articleService.index(pageable);

        model.addAttribute("articles", articles.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articles.getTotalPages());

        return "articles/article-list";
    }

    // 5. 게시글 수정 페이지로 이동
    @GetMapping("/{articleId}/edit")
    public String editForm(@PathVariable Long articleId, Model model) {
        ArticleDto.Info article = articleService.read(articleId);

        model.addAttribute("ArticleRequest", new ArticleDto.Create(
                article.getTitle(),
                article.getContent()
        ));
        model.addAttribute("articleId", article.getArticleId());

        return "articles/editForm";
    }

    // 6. 게시글 수정
    @PatchMapping("/{articleId}/edit")
    public String edit(@Validated @ModelAttribute ArticleDto.Create articleEditDto,
                       @PathVariable Long articleId,
                       @Login MemberDto.Session memberSessionDto,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/articles/editForm";
        }

        if(memberSessionDto == null) {
            return "redirect:/login";
        }

        Article article = articleService.update(articleEditDto, articleId, memberSessionDto.getId());

        // 수정된 글을 바로 볼 수 있도록 해당 게시글 페이지로 이동
        return "redirect:/articles/" + article.getArticleId();
    }

    // 7. 게시글 삭제
    @DeleteMapping("/{articleId}")
    public String delete(@Login MemberDto.Session memberSessionDto,
                         @PathVariable Long articleId,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/articles/" + articleId;
        }

        if(memberSessionDto == null) {
            return "redirect:/login";
        }

        articleService.delete(articleId, memberSessionDto.getId());

        return "/articles";
    }
}