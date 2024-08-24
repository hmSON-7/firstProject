package miniProject.board.service;

import miniProject.board.auth.constants.Role;
import miniProject.board.dto.ArticleDto;
import miniProject.board.entity.Article;
import miniProject.board.entity.Member;
import miniProject.board.repository.ArticleRepository;
import miniProject.board.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class ArticleServiceImplTest {

    @Autowired
    ArticleService articleService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Test
    @DisplayName("1. 게시글 생성 성공 테스트")
    void create() {
        // given
        Member member = memberRepository.save(Member.createMember(
                "testUser", "password", "nickname", Role.ROLE_USER
        ));
        ArticleDto.Create articleCreateDto = new ArticleDto.Create("Test Title", "Test Content");

        // when
        Article createdArticle = articleService.create(articleCreateDto, member.getId());

        // then
        Assertions.assertNotNull(createdArticle);
        Assertions.assertEquals("Test Title", createdArticle.getTitle());
        Assertions.assertEquals("testUser", createdArticle.getMember().getUsername());
    }

    @Test
    @DisplayName("2. 게시글 리스트 조회 성공 테스트")
    void index() {
        // given
        Member member = memberRepository.save(Member.createMember(
                "testUser", "password", "nickname", Role.ROLE_USER
        ));
        articleService.create(new ArticleDto.Create("Test Title 1", "Test Content 1"), member.getId());
        articleService.create(new ArticleDto.Create("Test Title 2", "Test Content 2"), member.getId());

        // when
        List<Article> articles = articleRepository.findAll();

        // then
        Assertions.assertEquals(2, articles.size());
    }

    @Test
    @DisplayName("3. 게시글 단일 조회 성공 테스트")
    void read() {
        // given
        Member member = memberRepository.save(Member.createMember(
                "testUser", "password", "nickname", Role.ROLE_USER
        ));
        Article createdArticle = articleService.create(new ArticleDto.Create("Test Title", "Test Content"), member.getId());

        // when
        ArticleDto.Info articleInfo = articleService.read(createdArticle.getArticleId(), false);

        // then
        Assertions.assertNotNull(articleInfo);
        Assertions.assertEquals("Test Title", articleInfo.getTitle());
        Assertions.assertEquals("Test Content", articleInfo.getContent());
    }

    @Test
    @DisplayName("4. 게시글 수정 성공 테스트")
    void update() {
        // given
        Member member = memberRepository.save(Member.createMember(
                "testUser", "password", "nickname", Role.ROLE_USER
        ));
        Article createdArticle = articleService.create(new ArticleDto.Create("Test Title", "Test Content"), member.getId());

        // when
        ArticleDto.Create updatedArticleDto = new ArticleDto.Create("Updated Title", "Updated Content");
        Article updatedArticle = articleService.update(updatedArticleDto, createdArticle.getArticleId(), member.getId());

        // then
        Assertions.assertNotNull(updatedArticle);
        Assertions.assertEquals("Updated Title", updatedArticle.getTitle());
        String updatedContent = articleService.read(updatedArticle.getArticleId(), false).getContent();
        Assertions.assertEquals("Updated Content", updatedContent);
    }

    @Test
    @DisplayName("5. 게시글 삭제 성공 테스트")
    void delete() {
        // given
        Member member = memberRepository.save(Member.createMember(
                "testUser", "password", "nickname", Role.ROLE_USER
        ));
        Article createdArticle = articleService.create(new ArticleDto.Create("Test Title", "Test Content"), member.getId());

        // when
        articleService.delete(createdArticle.getArticleId(), member.getId());

        // then
        Assertions.assertTrue(articleRepository.findById(createdArticle.getArticleId()).isEmpty());
    }
}