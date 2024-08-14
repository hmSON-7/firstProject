package miniProject.board.controller.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.dto.ReportDto;
import miniProject.board.service.report.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/comments")
    public String listReportedComments(Model model) {
        List<ReportDto.CommentListResponse> reportComments = reportService.getReportComments();

        model.addAttribute("reportComments", reportComments);

        return "report/commentReportList";
    }

    @GetMapping("/articles")
    public String listReportedArticles(Model model) {
        List<ReportDto.ArticleListResponse> reportArticles = reportService.getReportArticles();

        model.addAttribute("reportArticles", reportArticles);

        return "report/articleReportList";
    }
}

