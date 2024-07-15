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

    @GetMapping("report/member/{memberId}")
    public String getReportsByMemberId(@PathVariable Long memberId, Model model) {
        List<Report> reports = reportService.getReportsByMemberId(memberId);
        model.addAttribute("reports", reports);
        return "report/reportList"; // 특정 멤버가 신고한 목록을 보여줄 뷰 페이지
    }

    @DeleteMapping("report/{reportId}")
    public String deleteReport(@PathVariable Long reportId, @RequestParam Long memberId, Model model) {
        reportService.delete(reportId, memberId);
        model.addAttribute("message", "신고가 삭제되었습니다.");
        return "redirect:/report";
    }

    @GetMapping("/report/form")
    public String showReportForm(Model model) {
        model.addAttribute("reportArticle", new ReportDto.ReportArticle());
        model.addAttribute("reportComment", new ReportDto.ReportComment());
        return "report/reportForm";
    }

    @PostMapping("report/article") // 게시글 신고
    public String reportArticle(@ModelAttribute ReportDto.ReportArticle reportArticle) {
        reportService.reportArticle(reportArticle);
        if (reportArticle.getArticleId() == null || reportArticle.getMemberId() == null) {
            // ID가 null인 경우 처리
            return "redirect:/report/form";
        }
        return "redirect:/articles/" + reportArticle.getArticleId();
    }

    @PostMapping("report/comment") // 댓글 신고
    public String reportComment(@ModelAttribute ReportDto.ReportComment reportComment) {
        reportService.reportComment(reportComment);
        if (reportComment.getCommentId() == null || reportComment.getMemberId() == null) {
            // ID가 null인 경우 처리
            return "redirect:/report/form";
        }

        return "redirect:/articles/comments/" + reportComment.getCommentId();
    }


    @PostMapping("/report/process")
    public String processReport(@ModelAttribute ReportDto.ProcessReport processReportDto, Model model) {
        reportService.processReport(processReportDto);
        model.addAttribute("message", "신고가 처리되었습니다.");
        return "redirect:/report";
    }

}
