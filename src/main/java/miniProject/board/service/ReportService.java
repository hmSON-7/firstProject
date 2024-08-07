package miniProject.board.service;

import lombok.RequiredArgsConstructor;
import miniProject.board.dto.ReportDto;
import miniProject.board.entity.*;
import miniProject.board.entity.report.ReportArticle;
import miniProject.board.entity.report.ReportComment;
import miniProject.board.repository.ArticleRepository;
import miniProject.board.repository.CommentRepository;
import miniProject.board.repository.MemberRepository;
import miniProject.board.repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public Report reportArticle(ReportDto.ReportArticle reportArticle) {
        Member member = memberRepository.findById(reportArticle.getMemberId())
                .orElseThrow(() ->
                        new IllegalArgumentException("유저가 존재하지 않습니다."));

        Article article = articleRepository.findById(reportArticle.getArticleId())
                .orElseThrow(() ->
                        new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Report report = ReportArticle.reportArticle(reportArticle.getDescription(), article, member,ReportStatus.PENDING);



        return reportRepository.save(report);
    }

    public Report reportComment(ReportDto.ReportComment reportComment ) {
        Member member = memberRepository.findById(reportComment.getMemberId())
                .orElseThrow(() ->
                        new IllegalArgumentException("유저가 존재하지 않습니다."));

        Comment comment = commentRepository.findById(reportComment.getCommentId())
                .orElseThrow(() ->
                        new IllegalArgumentException("댓글이 존재하지 않습니다."));

        Report report = ReportComment.reportComment(reportComment.getDescription(), comment, member, ReportStatus.PENDING);

        return reportRepository.save(report);
    }


    @Transactional
    public void processReport(ReportDto.ProcessReport processReport) {
        Report report = reportRepository.findById(processReport.getReportId())
                .orElseThrow(() ->
                        new IllegalArgumentException("신고가 존재하지 않습니다."));

        ReportStatus newStatus = processReport.getReportStatus();
        report.updateStatus(newStatus);
        reportRepository.save(report);

        if (newStatus == ReportStatus.APPROVED) {
            if(report instanceof ReportArticle){
                ReportArticle reportArticle = (ReportArticle) report;
                reportArticle.getArticle().getMember().suspendTemporarily(30);
                articleRepository.delete(reportArticle.getArticle());

            }else if (report instanceof ReportComment){
                ReportComment reportComment = (ReportComment) report;
                reportComment.getComment().getMember().suspendTemporarily(30);
                commentRepository.delete(reportComment.getComment());
            }
        }else if (newStatus == ReportStatus.REJECTED) {
            reportRepository.delete(report);
        }
    }

    @Transactional
    public void delete(Long reportId, Long userId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() ->
                        new IllegalArgumentException("신고가 존재하지 않습니다."));

        if (!report.getMember().getId().equals(userId)) {
            throw new IllegalArgumentException("신고자만 신고를 삭제할 수 있습니다.");
        }

        reportRepository.delete(report);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReportById(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고가 존재하지 않습니다."));
    }

    public List<Report> getReportsByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return reportRepository.findByMember(member);
    }

    public String getReportType(Long reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() ->
                        new IllegalArgumentException("신고가 존재하지 않습니다."));

        if(report instanceof ReportArticle){
            return "ARTICLE";
        }
        else if(report instanceof ReportComment){
            return "COMMENT";
        }
        return "Unknown";
    }


}
