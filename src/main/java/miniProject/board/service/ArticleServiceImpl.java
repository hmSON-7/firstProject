package miniProject.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniProject.board.dto.ArticleAddDto;
import miniProject.board.dto.ArticleDto;
import miniProject.board.entity.Article;
import miniProject.board.entity.Member;
import miniProject.board.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberServiceImpl memberService;

    @Override
    public List<Article> index() {
        return articleRepository.findAll();
    }

    @Override
    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    @Override
    public Article create(ArticleAddDto articleAddDto, Long memberId) {
        Member member = memberService.findMemberDao(memberId);
        Article article = new Article(articleAddDto.getTitle(),
                articleAddDto.getContent(),
                member
        );

        return articleRepository.save(article);
    }

    @Override
    public Article update(ArticleDto articleDto, Long articleId) {
        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            Member currentMember = getCurrentMember();
            if (currentMember != null && article.getMember().getId().equals(currentMember.getId())) {
                article.update(articleDto.getTitle(), articleDto.getContent());
                return articleRepository.save(article);
            }
        }
        return null;
    }

    @Override
    public boolean delete(Long articleId) {
        Optional<Article> optionalArticle = articleRepository.findById(articleId);

        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            Member currentMember = getCurrentMember();

            if (currentMember != null && article.getMember().getId().equals(currentMember.getId())) {
                articleRepository.delete(article);
                return true;
            }
        }

        return false;
    }

    // 세션에서 현재 사용자의 정보를 가져오는 메서드
    private Member getCurrentMember() {
    }
}
