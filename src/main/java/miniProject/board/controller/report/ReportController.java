package miniProject.board.controller.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.controller.argumentResolver.Login;
import miniProject.board.dto.MemberDto;
import miniProject.board.dto.ReportDto;
import miniProject.board.service.report.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @ModelAttribute("memberSessionDto")
    public MemberDto.Session addMemberSession(@Login MemberDto.Session memberSessionDto) {
        return memberSessionDto;
    }

    @GetMapping("/comments")
    public String listReportedComments(Model model) {
        List<ReportDto.CommentListResponse> reportComments = reportService.getReportComments();

        model.addAttribute("reportComments", reportComments);

        return "report/commentReportList";
    }


    @GetMapping("/comments/{reportId}")
    public String showCommentReport(@PathVariable("reportId") Long reportId, Model model) {
        ReportDto.CommentResponse reportComment = reportService.getReportComment(reportId);

        model.addAttribute("reportComment", reportComment);
        model.addAttribute("processRequest", new ReportDto.ProcessRequest());

        return "report/commentReportDetail";
    }


    @GetMapping("/articles")
    public String listReportedArticles(Model model) {
        List<ReportDto.ArticleListResponse> reportArticles = reportService.getReportArticles();

        model.addAttribute("reportArticles", reportArticles);

        return "report/articleReportList";
    }

    @GetMapping("/articles/{reportId}")
    public String showArticleReport(@PathVariable("reportId") Long reportId, Model model) {
        ReportDto.ArticleResponse reportArticle = reportService.getReportArticle(reportId);

        model.addAttribute("reportArticle", reportArticle);
        model.addAttribute("processRequest", new ReportDto.ProcessRequest());

        return "report/articleReportDetail";
    }
}

