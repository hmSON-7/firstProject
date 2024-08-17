package miniProject.board.service.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.dto.ArticleDto;
import miniProject.board.dto.ReportDto;
import miniProject.board.entity.*;
import miniProject.board.entity.report.Report;
import miniProject.board.entity.report.ReportArticle;
import miniProject.board.entity.report.ReportComment;
import miniProject.board.exception.ArticleNotFoundException;
import miniProject.board.exception.CommentNotFoundException;
import miniProject.board.exception.MemberNotFoundException;
import miniProject.board.exception.report.MemberReportNotFoundException;
import miniProject.board.exception.report.ReportNotFoundException;
import miniProject.board.repository.ArticleRepository;
import miniProject.board.repository.CommentRepository;
import miniProject.board.repository.MemberRepository;
import miniProject.board.repository.ReportRepository;
import miniProject.board.service.ArticleService;
import miniProject.board.service.constants.ReportAction;
import miniProject.board.service.member.MemberSuspensionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;

    private final ArticleService articleService;
    private final MemberSuspensionService memberSuspensionService;

    @Override
    public void saveReportComment(Long memberId, Long commentId, ReportDto.Request commentReportRequest) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() ->
                        new MemberNotFoundException("멤버가 존재하지 않습니다."));

        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() ->
                        new CommentNotFoundException("댓글이 존재하지 않습니다."));

        ReportComment reportComment = ReportComment.createReportComment(commentReportRequest.getDescription(), comment, member);

        reportRepository.save(reportComment);
    }

    @Override
    public void saveReportArticle(Long memberId, Long articleId, ReportDto.Request articleReportRequest) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버가 존재하지 않습니다."));

        Article article = articleRepository
                .findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("게시글이 존재하지 않습니다."));

        ReportArticle reportArticle = ReportArticle.createReportArticle(articleReportRequest.getDescription(), article, member);

        reportRepository.save(reportArticle);
    }

    public List<ReportDto.CommentListResponse> getReportComments() {
        return reportRepository
                .findAllReportComments()
                .stream()
                .map(reportComment ->
                        new ReportDto.CommentListResponse(reportComment.getId(),
                                reportComment.getMember().getUsername(),
                                reportComment.getDescription(),
                                reportComment.getUpdatedAt()))
                .toList();
    }

    @Override
    public ReportDto.CommentResponse getReportComment(Long reportId) {
        ReportComment reportComment = reportRepository
                .findReportCommentById(reportId)
                .orElseThrow(ReportNotFoundException::new);

        return ReportDto.CommentResponse.fromReportComment(reportComment);
    }

    @Override
    public List<ReportDto.ArticleListResponse> getReportArticles() {
        return reportRepository.
                findAllReportArticles()
                .stream()
                .map(reportArticle ->
                        new ReportDto.ArticleListResponse(reportArticle.getId(),
                                reportArticle.getMember().getUsername(),
                                reportArticle.getDescription(),
                                reportArticle.getUpdatedAt()))
                .toList();
    }

    @Override
    public ReportDto.ArticleResponse getReportArticle(Long reportId) {
        ReportArticle reportArticle = reportRepository
                .findReportArticleById(reportId)
                .orElseThrow(ReportNotFoundException::new);


        return ReportDto.ArticleResponse
                .fromReportArticle(reportArticle, articleService.read(reportArticle.getId()).getContent());
    }

    public void processReport(Long reportId,
                              ReportAction reportAction,
                              ReportDto.ProcessRequest processReportRequest) {

        Report report = reportRepository
                .findById(reportId)
                .orElseThrow(() ->
                        new ReportNotFoundException("신고를 찾을 수 없습니다."));

        Long memberId;

        if (report instanceof ReportArticle) {
            memberId = ((ReportArticle) report).getArticle().getMember().getId();

        } else if (report instanceof ReportComment) {
            memberId = ((ReportComment) report).getComment().getMember().getId();

        } else {
            throw new IllegalStateException();
        }

        switch (reportAction) {
            case BAN:
                memberSuspensionService.applyPermanentSuspension(memberId);
                report.updateStatus(ReportStatus.APPROVED);
                break;

            case SUSPEND:
                memberSuspensionService.applyTemporarySuspension(memberId, processReportRequest.getDate());
                report.updateStatus(ReportStatus.APPROVED);
                break;

            case REJECT:
                report.updateStatus(ReportStatus.REJECTED);
                break;

            default:
                throw new IllegalStateException();
        }
    }

    public void delete(Long reportId, Long memberId) {
        Report report = reportRepository
                .findByIdAndMemberId(reportId, memberId)
                .orElseThrow(MemberReportNotFoundException::new);

        reportRepository.delete(report);
    }
}
