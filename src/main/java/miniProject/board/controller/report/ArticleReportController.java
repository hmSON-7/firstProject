package miniProject.board.controller.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.controller.argumentResolver.Login;
import miniProject.board.dto.MemberDto;
import miniProject.board.dto.ReportDto;
import miniProject.board.service.report.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleReportController {

    private final ReportService reportService;

    @PostMapping("/{articleId}/reports")
    public String reportArticle(@Login MemberDto.Session memberSessionDto,
                                @PathVariable Long articleId,
                                @ModelAttribute("articleDto") ReportDto.Request articleReportRequest) {

        reportService.saveReportArticle(memberSessionDto.getId(), articleId, articleReportRequest);

        return "redirect:/";
    }
}
