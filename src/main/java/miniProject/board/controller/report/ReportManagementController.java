package miniProject.board.controller.report;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.dto.ReportDto;
import miniProject.board.service.constants.ReportAction;
import miniProject.board.service.report.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ReportManagementController {
    private final ReportService reportService;

    @PatchMapping("/reports/{reportId}/ban")
    public String banMember(@PathVariable Long reportId) {
        reportService.processReport(reportId, ReportAction.BAN, null);

        return "redirect:/admin";
    }

    @PatchMapping("/reports/{reportId}/suspend")
    public String suspendMember(@PathVariable Long reportId,
                              @ModelAttribute ReportDto.ProcessRequest processReportRequest) {
        reportService.processReport(reportId, ReportAction.SUSPEND, processReportRequest);

        return "redirect:/admin";

    }

    @PostMapping("/reports/{reportId}/reject")
    public String rejectReport(@PathVariable Long reportId) {
        reportService.processReport(reportId, ReportAction.REJECT, null);

        return "redirect:/admin";

    }
}
