package miniProject.board.service;

import miniProject.board.auth.constants.Role;
import miniProject.board.dto.ArticleDto;
import miniProject.board.dto.CommentDto;
import miniProject.board.dto.ReportDto;
import miniProject.board.entity.*;
import miniProject.board.repository.ArticleRepository;
import miniProject.board.repository.CommentRepository;
import miniProject.board.repository.MemberRepository;
import miniProject.board.repository.ReportRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional

class ReportServiceTest {

    @Autowired
    ReportService reportService;
    @Autowired
    ArticleService articleService;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;

    @Test
    @DisplayName("게시글 신고")
    void reportArticle() {
        //given
        Member member = memberRepository.save(Member.createMember(
                "testUser", "password", "nickname", Role.ROLE_USER
        ));
        ArticleDto.Create articleCreateDto = new ArticleDto.Create("Test Title", "Test Content");
        Article createdArticle = articleService.create(articleCreateDto, member.getId());

        ReportDto.ReportArticle createdReportArticle = new ReportDto.ReportArticle
                ("욕설", createdArticle.getArticleId(),member.getId());

        //when

        Report report = reportService.reportArticle(createdReportArticle);

        //then
        Assertions.assertNotNull(report);
        Assertions.assertEquals("욕설", report.getDescription());
        Assertions.assertEquals(ReportStatus.PENDING, report.getStatus());
        Assertions.assertEquals(member.getId(), report.getMember().getId());
        Assertions.assertEquals(createdArticle.getArticleId(), report.getArticle().getArticleId());
    }

    @Test
    @DisplayName("댓글 신고")
    void reportComment() {
        //given
        Member member = memberRepository.save(Member.createMember(
                "testUser", "password", "nickname", Role.ROLE_USER
        ));

        ArticleDto.Create articleCreateDto = new ArticleDto.Create("Test Title", "Test Content");
        Article createdArticle = articleService.create(articleCreateDto, member.getId());

        CommentDto.CommentRequest commentRequest = new CommentDto.CommentRequest(createdArticle.getArticleId(), member.getUsername(), "Test Comment");
        Comment createdComment = commentService.save(member.getId(), createdArticle.getArticleId(), commentRequest);

        ReportDto.ReportComment createdReportComment = new ReportDto.ReportComment
                ("욕설", createdComment.getCommentId(), member.getId());

        //when
        Report report = reportService.reportComment(createdReportComment);

        //then
        Assertions.assertNotNull(report);
        Assertions.assertEquals("욕설", report.getDescription());
        Assertions.assertEquals(ReportStatus.PENDING, report.getStatus());
        Assertions.assertEquals(member.getId(), report.getMember().getId());
        Assertions.assertEquals(createdComment.getCommentId(), report.getComment().getCommentId());

    }

    @Test
    @DisplayName("신고 처기 하기")
    void processReport() {


    }

    @Test
    @DisplayName("신고 삭제")
    void delete() {
    }
}