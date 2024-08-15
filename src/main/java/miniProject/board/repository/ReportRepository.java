package miniProject.board.repository;

import miniProject.board.entity.Member;
import miniProject.board.entity.report.Report;
import miniProject.board.entity.report.ReportArticle;
import miniProject.board.entity.report.ReportComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByMember(Member member);

    @Query("select r from Report r where type(r) = ReportArticle")
    List<ReportArticle> findAllReportArticles();

    @Query("select r from Report r where type(r) = ReportComment")
    List<ReportComment> findAllReportComments();

    Optional<Report> findByIdAndMemberId(Long id, Long memberId);
}
