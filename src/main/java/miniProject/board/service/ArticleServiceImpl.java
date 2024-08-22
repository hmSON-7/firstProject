package miniProject.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.dto.ArticleDto;
import miniProject.board.entity.Article;
import miniProject.board.entity.Likes;
import miniProject.board.entity.Member;
import miniProject.board.repository.ArticleRepository;
import miniProject.board.repository.LikesRepository;
import miniProject.board.repository.MemberRepository;
import miniProject.board.service.file.FileStorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final FileStorageService fileStorageService;
    private final LikesRepository likesRepository;

    @Override
    public Article create(ArticleDto.Create articleAddDto, Long memberId) throws IOException {
        /*
         * Article 엔티티에 정보를 반환하기 위해 매개변수로 member 객체를 요구하므로
         * 세션으로 memberId를 받아온 후 해당 Member 객체를 찾아 매개변수로 넘김
         */
        Member member = memberRepository.findById(memberId).orElse(null);

        if(member == null) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        String contentHtml = new String(articleAddDto.getContent().getBytes(), StandardCharsets.UTF_8);

        // 난수 생성
        UUID uuid = UUID.randomUUID();

        // 파일 저장 및 경로 반환
        String filePath = fileStorageService.createFile(
                member.getUsername(),
                contentHtml,
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
        return articleRepository
                .findAllByOrderByUpdatedAtDesc(pageable)
                .map(ArticleDto.ArticlesList::fromArticle);
    }

    @Override
    public Page<ArticleDto.ArticlesList> getArticlesByMember(Long memberId, Pageable pageable) {
        return articleRepository
                .findArticlesByMemberId(memberId, pageable)
                .map(ArticleDto.ArticlesList::fromArticle);
    }

    @Override
    @Transactional
    public ArticleDto.Info read(Long articleId, Boolean keepHits) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        String content = fileStorageService.readFile(article.getFilePath());
        if(content == null) {
            throw new RuntimeException("파일을 찾을 수 없습니다.");
        }

        if(!keepHits) {
            article.checkHits();
        }
        articleRepository.save(article);
        log.debug("접근 확인");
        log.debug("article.getCreatedAt(): " + article.getCreatedAt()); // 디버깅 로그 추가

        return new ArticleDto.Info(
                articleId,
                article.getTitle(),
                content,
                article.getMember().getId(),
                article.getMember().getNickname(),
                article.getCreatedAt(),
                article.getUpdatedAt(),
                article.getHits(),
                article.getLikes()
        );
    }

    @Override
    @Transactional
    public Article update(ArticleDto.Create articleEditDto, Long articleId, Long memberId) throws IOException {
        /*
         * 수정 요청을 받은 Article의 id를 받아온다. 존재하지 않는 id이면 수정 불가
         * 수정을 요청한 Member의 세션으로부터 MemberId를 받아온다. 로그아웃 상태인 유저가 게시글을 수정하는 것을 방지
         * 게시글의 작성자 id와 수정을 요청한 Member의 id를 비교. 같은 경우에만 게시글 수정 허용
         */
        log.debug("서비스 계층 접근");
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        log.debug("article 조회");

        Member member = memberRepository.findById(memberId).orElse(null);

        if(member == null || !member.getId().equals(article.getMember().getId())) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }
        log.debug("member 조회");

        String contentHtml;

        if (articleEditDto.getContent() == null || articleEditDto.getContent().isEmpty()) {
            contentHtml = "<h1>신고 처리된 게시글입니다.</h1>";

        } else {
            contentHtml = new String(articleEditDto.getContent().getBytes(), StandardCharsets.UTF_8);
        }

        fileStorageService.updateFile(article.getFilePath(), contentHtml);
        log.debug("파일 업데이트 완료");

        article.update(articleEditDto.getTitle());
        log.debug("DB 업데이트 완료");
        return articleRepository.save(article);
    }

    @Override
    public void delete(Long articleId, Long memberId) {
        /*
         *삭제 요청을 받은 Article의 id를 받아온다. 존재하지 않는 id이면 삭제 불가
         *삭제를 요청한 Member의 세션으로부터 MemberId를 받아온다. 로그아웃 상태인 유저가 삭제 요청하는 것을 방지
         *게시글의 작성자 id와 삭제를 요청한 Member의 id를 비교. 같은 경우에만 게시글 삭제 허용
         */
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId).orElse(null);

        if(member == null || !member.getId().equals(article.getMember().getId())) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        fileStorageService.deleteFile(article.getFilePath());

        articleRepository.delete(article);
    }

    @Override
    @Transactional
    public void changeLikes(Long articleId, Long memberId) {
        Likes like = likesRepository.findByArticleArticleIdAndMemberId(articleId, memberId).orElse(null);

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (like == null) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
            Likes newLike = Likes.giveLike(article, member);
            likesRepository.save(newLike);
        } else {
            likesRepository.delete(like);
        }

        int changedLikes = likesRepository.countByArticle(article);
        article.checkLikes(changedLikes);
        articleRepository.save(article);
    }
}
