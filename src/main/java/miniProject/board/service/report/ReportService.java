package miniProject.board.service.report;

import miniProject.board.dto.ReportDto;
import miniProject.board.service.constants.ReportAction;

import java.util.List;

public interface ReportService {
    void saveReportComment(Long memberId, Long commentId, ReportDto.Request commentReportRequest);

    void saveReportArticle(Long memberId, Long articleId, ReportDto.Request articleReportRequest);

    List<ReportDto.ArticleListResponse> getReportArticles();

    List<ReportDto.CommentListResponse> getReportComments();

    void processReport(Long reportId, ReportAction reportAction, ReportDto.ProcessRequest processReportRequest);
}
