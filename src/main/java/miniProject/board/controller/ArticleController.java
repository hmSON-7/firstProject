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

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    // 1. 게시글 작성 페이지로 이동
    @GetMapping
    public String writeForm(Model model) {
        model.addAttribute("articleDto", new ArticleDto.Create());
        return "/articles/writeForm";
    }

    // 2. 게시글 작성
    @PostMapping
    public String write(@Validated @ModelAttribute("articleDto") ArticleDto.Create articleCreateDto,
                        @Login MemberDto.Session memberSessionDto,
                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/articles/writeForm";
        }

        if (memberSessionDto == null) {
            return "redirect:/login";
        }

        try {
            Article written = articleService.create(articleCreateDto, memberSessionDto.getId());
            log.info("게시글 작성 완료: {}", written);
            log.info("작성된 게시글 ID: {}", written.getArticleId());
            return "redirect:/articles/" + written.getArticleId();
        } catch (IOException e) {
            log.error("파일 처리 중 오류 발생", e);
            return "redirect:/error"; // 또는 오류 페이지로 리디렉션
        } catch (Exception e) {
            log.error("게시글 작성 중 오류 발생", e);
            return "redirect:/error"; // 또는 오류 페이지로 리디렉션
        }
    }

    // 3. 게시글 단일 조회
    @GetMapping("/{articleId}")
    public String show(@PathVariable("articleId") Long articleId,
                       @Login MemberDto.Session memberSessionDto,
                       Model model) {
        ArticleDto.Info article = articleService.read(articleId);
        log.debug("DB 조회 확인");
        model.addAttribute("ArticleInfo", article);
        log.debug("모델 확인");

        // 해당 게시글의 작성자가 조회하려는 것인지 확인
        boolean isAuthor = article.getAuthorId().equals(memberSessionDto.getId());
        model.addAttribute("isAuthor", isAuthor);

        return "articles/articleView";
    }

    // 4. 게시글 리스트 조회
    @GetMapping("/list")
    public String index(@RequestParam(defaultValue = "1", name="page") int page, Model model) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<ArticleDto.ArticlesList> articles = articleService.index(pageable);

        model.addAttribute("articles", articles.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articles.getTotalPages());

        return "articles/article-list";
    }

    // 5. 게시글 수정 페이지로 이동
    @GetMapping("/{articleId}/edit")
    public String editForm(@PathVariable("articleId") Long articleId, Model model) {
        ArticleDto.Info article = articleService.read(articleId);
        log.debug("article 읽기 성공");

        model.addAttribute("ArticleRequest", new ArticleDto.Load(
                article.getTitle(),
                article.getContent() // 수정 시 기존의 내용을 불러오기 위해 문자열로 설정합니다.
        ));
        model.addAttribute("articleId", article.getArticleId());
        log.debug("데이터 전송중...");

        return "articles/editForm";
    }

    // 6. 게시글 수정
    @PatchMapping("/{articleId}/edit")
    public String edit(@Validated @ModelAttribute ArticleDto.Create articleEditDto,
                       @PathVariable("articleId") Long articleId,
                       @Login MemberDto.Session memberSessionDto,
                       BindingResult bindingResult) {
        log.debug("접근");
        if (bindingResult.hasErrors()) {
            return "/articles/editForm";
        }

        log.debug("오류 없음");
        if(memberSessionDto == null) {
            return "redirect:/login";
        }

        try {
            Article article = articleService.update(articleEditDto, articleId, memberSessionDto.getId());

            log.debug("로직 문제 없음");
            // 수정된 글을 바로 볼 수 있도록 해당 게시글 페이지로 이동
            return "redirect:/articles/" + article.getArticleId();
        } catch (IOException e) {
            log.error("파일 처리 중 오류 발생", e);
            return "redirect:/error"; // 또는 오류 페이지로 리디렉션
        }
    }

    // 7. 게시글 삭제
    @DeleteMapping("/{articleId}/delete")
    public String delete(@PathVariable("articleId") Long articleId,
                         @Login MemberDto.Session memberSessionDto) {
        log.debug("접근");

        if(memberSessionDto == null) {
            return "redirect:/login";
        }
        log.debug("로그인 문제 없음");

        articleService.delete(articleId, memberSessionDto.getId());
        log.debug("로직 문제 없음");

        return "redirect:/articles/list";
    }
}
