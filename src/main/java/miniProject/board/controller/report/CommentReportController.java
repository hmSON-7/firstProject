package miniProject.board.controller.report;

import lombok.RequiredArgsConstructor;
import miniProject.board.controller.argumentResolver.Login;
import miniProject.board.dto.MemberDto;
import miniProject.board.dto.ReportDto;
import miniProject.board.service.report.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentReportController {

    private final ReportService reportService;

    @PostMapping("/{commentId}/reports")
    public String reportComment(@Login MemberDto.Session memberSessionDto,
                                @PathVariable Long commentId,
                                @ModelAttribute("commentDto") ReportDto.Request commentReportRequest) {
        reportService.saveReportComment(memberSessionDto.getId(), commentId, commentReportRequest);

        return "redirect:/";
    }


}