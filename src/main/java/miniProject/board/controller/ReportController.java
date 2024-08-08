package miniProject.board.controller;


import lombok.RequiredArgsConstructor;
import miniProject.board.controller.argumentResolver.Login;
import miniProject.board.dto.MemberDto;
import miniProject.board.dto.ReportDto;
import miniProject.board.entity.Report;
import miniProject.board.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    // 관리자에게 모든 신고 목록을 보여줄 뷰 페이지
    @GetMapping("/report/all")
    public String getAllReports(Model model) {

        List<Report> reports = reportService.getAllReports();
        model.addAttribute("reports", reports);
        return "report/reportList";
    }

    // 관리자가 댓글 처리
    @PostMapping("/report/process")
    public String processReport(@ModelAttribute ReportDto.ProcessReport processReportDto, Model model) {
        reportService.processReport(processReportDto);
        model.addAttribute("report", processReportDto);
        model.addAttribute("message", "신고가 처리되었습니다.");
        return "report/processReport";
    }

    // 특정 멤버가 신고한 목록을 보여줄 뷰 페이지
    @GetMapping("report/member/{memberId}")
    public String getReportsByMemberId(@Login MemberDto.Session memberSessionDto, Model model, @PathVariable Long memberId) {

        if(!memberSessionDto.getId().equals(memberId)){
            return "redirect:/";
        }
        List<Report> reports = reportService.getReportsByMemberId(memberSessionDto.getId());
        model.addAttribute("reports", reports);
        return "report/reportList";
    }

    // 신고 삭제
    @DeleteMapping("report/delete")
    public String deleteReport(@PathVariable Long reportId, @Login MemberDto.Session memberSessionDto, Model model) {
        reportService.delete(reportId, memberSessionDto.getId());
        model.addAttribute("message", "신고가 삭제되었습니다.");
        return "redirect:/report/all";
    }

    //신고 폼 작성
    @GetMapping("/articles/{articleId}/report")
    public String showReportForm(Model model, @PathVariable Long articleId, @Login MemberDto.Session memberSessionDto) {

        ReportDto.ReportArticle reportArticle = new ReportDto.ReportArticle();
        ReportDto.ReportComment reportComment = new ReportDto.ReportComment();

        if (articleId != null) {
            reportArticle.setArticleId(articleId);
        }
        model.addAttribute("memberSession", memberSessionDto);
        model.addAttribute("reportArticle", reportArticle);
        model.addAttribute("reportComment", reportComment);
        return "report/reportForm";
    }

    // 게시글 신고
    @PostMapping("/report/article")
    public String reportArticle(@ModelAttribute ReportDto.ReportArticle reportArticle) {

        if (reportArticle.getArticleId() == null || reportArticle.getMemberId() == null) {
            // ID가 null인 경우 처리
            return "redirect:/";
        }
        reportService.reportArticle(reportArticle);
        return "redirect:/articles/" + reportArticle.getArticleId();
    }

    // 댓글 신고
    @PostMapping("/report/comment")
    public String reportComment(@ModelAttribute ReportDto.ReportComment reportComment) {
        reportService.reportComment(reportComment);
        if (reportComment.getCommentId() == null || reportComment.getMemberId() == null) {
            // ID가 null인 경우 처리
            return "redirect:/";
        }

        return "redirect:/articles/comments/" + reportComment.getCommentId();
    }


}
