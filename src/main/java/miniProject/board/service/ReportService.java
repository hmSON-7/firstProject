package miniProject.board.service;

import lombok.RequiredArgsConstructor;
import miniProject.board.dto.ReportDto;
import miniProject.board.entity.*;
import miniProject.board.repository.ArticleRepository;
import miniProject.board.repository.CommentRepository;
import miniProject.board.repository.MemberRepository;
import miniProject.board.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public void reportArticle(ReportDto.ReportArticle reportArticle) {
        Optional<Member> __member = memberRepository.findById(reportArticle.getMemberId());
        Member member;

        if (__member.isPresent()) { // Optional이 값으로 채워져 있는지 확인
            member = __member.get(); // User 객체 추출
        } else {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        Article article = articleRepository.findById(reportArticle.getArticleId())
                .orElseThrow(() ->
                        new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Report report = Report.reportArticle(reportArticle.getDescription(), article, member,ReportStatus.PENDING);

        reportRepository.save(report);
    }

    public void reportComment(ReportDto.ReportComment reportComment ) {
        Optional<Member> __member = memberRepository.findById(reportComment.getMemberId());
        Member member;

        if (__member.isPresent()) { // Optional이 값으로 채워져 있는지 확인
            member = __member.get(); // User 객체 추출
        } else {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        Comment comment = commentRepository.findById(reportComment.getCommentId())
                .orElseThrow(() ->
                        new IllegalArgumentException("댓글이 존재하지 않습니다."));

        Report report = Report.reportComment(reportComment.getDescription(), comment, member, ReportStatus.PENDING);

        reportRepository.save(report);
    }


}
