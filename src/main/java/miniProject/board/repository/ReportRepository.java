package miniProject.board.repository;

import miniProject.board.entity.Member;
import miniProject.board.entity.report.Report;
import miniProject.board.entity.report.ReportArticle;
import miniProject.board.entity.report.ReportComment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByMember(Member member);

    @EntityGraph(attributePaths = {"member"})
    @Query("select r from ReportArticle r where r.reportStatus = 'PENDING'")
    List<ReportArticle> findAllReportArticles();

    @EntityGraph(attributePaths = {"member"})
    @Query("select r from ReportComment r where r.reportStatus = 'PENDING'")
    List<ReportComment> findAllReportComments();

    Optional<Report> findByIdAndMemberId(Long id, Long memberId);

    @Query("select r from ReportArticle r where r.id = :id")
    Optional<ReportArticle> findReportArticleById(@Param("id") Long id);

    @Query("select r from ReportComment r where r.id = :id")
    Optional<ReportComment> findReportCommentById(@Param("id") Long id);
}
