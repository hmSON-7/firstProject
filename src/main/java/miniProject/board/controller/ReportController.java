package miniProject.board.controller;


import lombok.RequiredArgsConstructor;
import miniProject.board.dto.ReportDto;
import miniProject.board.entity.Report;
import miniProject.board.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/report/all")
    public String getAllReports(Model model) {
        List<Report> reports = reportService.getAllReports();
        model.addAttribute("reports", reports);
        return "report/reportList"; // 모든 신고 목록을 보여줄 뷰 페이지
    }

    @GetMapping("report/{reportId}")
    public String getReportDetails(@PathVariable Long reportId, Model model) {
        Report report = reportService.getReportById(reportId);
        model.addAttribute("report", report);
        return "report/reportDetails"; // 신고 상세 내용을 보여줄 뷰 페이지
    }

    @DeleteMapping("report/{reportId}")
    public String deleteReport(@PathVariable Long reportId, @RequestParam Long memberId, Model model) {
        reportService.delete(reportId, memberId);
        model.addAttribute("message", "신고가 삭제되었습니다.");
        return "redirect:/reports";
    }

    @GetMapping("report/member/{memberId}")
    public String getReportsByMemberId(@PathVariable Long memberId, Model model) {
        List<Report> reports = reportService.getReportsByMemberId(memberId);
        model.addAttribute("reports", reports);
        return "report/reportList"; // 특정 멤버가 신고한 목록을 보여줄 뷰 페이지
    }

    @PostMapping("report/article") // 게시글 신고
    public String reportArticle(@ModelAttribute ReportDto.ReportArticle reportArticle, Model model) {
        reportService.reportArticle(reportArticle);
        model.addAttribute("report", reportArticle);
        return "redirect:/articles/" + reportArticle.getArticleId();
    }

    @PostMapping("report/comment") // 댓글 신고
    public String reportComment(@ModelAttribute ReportDto.ReportComment reportComment, Model model) {
        reportService.reportComment(reportComment);
        model.addAttribute("report", reportComment);
        return "redirect:/articles/comments/" + reportComment.getCommentId();
    }

    @PostMapping("/report/process")
    public String processReport(@ModelAttribute ReportDto.ProcessReport processReportDto, Model model) {
        reportService.processReport(processReportDto);
        model.addAttribute("message", "신고가 처리되었습니다.");
        return "redirect:/reports";
    }

}
