package miniProject.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.dto.ArticleDto;
import miniProject.board.entity.Article;
import miniProject.board.entity.Member;
import miniProject.board.repository.ArticleRepository;
import miniProject.board.service.file.FileStorageService;
import miniProject.board.service.member.MemberServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberServiceImpl memberService;
    private final FileStorageService fileStorageService;

    @Override
    public Article create(ArticleDto.Create articleAddDto, Long memberId) {
        /*
         * Article 엔티티에 정보를 반환하기 위해 매개변수로 member 객체를 요구하므로
         * 세션으로 memberId를 받아온 후 해당 Member 객체를 찾아 매개변수로 넘김
         */
        Member member = memberService.findMemberDao(memberId);
        if(member == null) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        // 난수 생성
        UUID uuid = UUID.randomUUID();

        // 파일 저장 및 경로 반환
        String filePath = fileStorageService.createFile(
                member.getUsername(),
                articleAddDto.getContent(),
                uuid
        );

        Article article = new Article(
                articleAddDto.getTitle(),
                filePath,
                uuid,
                member
        );

        return articleRepository.save(article);
    }

    // 2. 게시글 리스트 조회 서비스
    @Override
    public Page<ArticleDto.ArticlesList> index(Pageable pageable) {
        Page<Article> articlesPage = articleRepository.findAllByOrderByUpdatedAtDesc(pageable);
        List<ArticleDto.ArticlesList> articlesList = articlesPage.stream().map(article -> new ArticleDto.ArticlesList(
                article.getArticleId(),
                article.getTitle(),
                article.getMember().getNickname(),
                article.getUpdatedAt(),
                article.getLikes(),
                article.getCreatedAt() // 수정 여부 확인을 위한 매개변수
        )).toList();

        return new PageImpl<>(articlesList, pageable, articlesPage.getTotalElements());
    }

    @Override
    @Transactional
    public ArticleDto.Info read(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        String content = fileStorageService.readFile(article.getFilePath());
        if(content == null) {
            throw new RuntimeException("파일을 찾을 수 없습니다.");
        }

        article.checkHits();
        articleRepository.save(article);

        return new ArticleDto.Info(
                articleId,
                article.getTitle(),
                content,
                article.getMember().getNickname(),
                article.getCreatedAt(),
                article.getUpdatedAt(),
                article.getHits(),
                article.getLikes()
        );
    }

    @Override
    public Article update(ArticleDto.Info articleDto, Long articleId, Long memberId) {
        /*
        * 수정 요청을 받은 Article의 id를 받아온다. 존재하지 않는 id이면 수정 불가
        * 수정을 요청한 Member의 세션으로부터 MemberId를 받아온다. 로그아웃 상태인 유저가 게시글을 수정하는 것을 방지
        * 게시글의 작성자 id와 수정을 요청한 Member의 id를 비교. 같은 경우에만 게시글 수정 허용
        */
        Article article = articleRepository.findById(articleId).orElse(null);
        if(article == null) {
            return null;
        }

        Member member = memberService.findMemberDao(memberId);
        if(member == null) {
            return null;
        }

        if(Objects.equals(member.getId(), article.getMember().getId())) {
            article.update(articleDto.getTitle());
            return articleRepository.save(article);
        }

        return null;
    }

    @Override
    public boolean delete(Long articleId, Long memberId) {
        /*
         *삭제 요청을 받은 Article의 id를 받아온다. 존재하지 않는 id이면 삭제 불가
         *삭제를 요청한 Member의 세션으로부터 MemberId를 받아온다. 로그아웃 상태인 유저가 삭제 요청하는 것을 방지
         *게시글의 작성자 id와 삭제를 요청한 Member의 id를 비교. 같은 경우에만 게시글 삭제 허용
         */
        Article article = articleRepository.findById(articleId).orElse(null);
        if(article == null) {
            return false;
        }

        Member member = memberService.findMemberDao(memberId);
        if(member == null) {
            return false;
        }

        if(Objects.equals(member.getId(), article.getMember().getId())) {
            articleRepository.delete(article);
            return true;
        }

        return false;
    }
}
