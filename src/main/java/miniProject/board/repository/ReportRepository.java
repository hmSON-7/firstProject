package miniProject.board.repository;

import miniProject.board.entity.Member;
import miniProject.board.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByMember(Member member);
}
