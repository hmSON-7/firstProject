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
import java.util.Objects;

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
    public Article show(Long articleId) {
        return articleRepository.findById(articleId).orElse(null);
    }

    @Override
    public Article write(ArticleAddDto articleAddDto, Long memberId) {
        /*
        * Article 엔티티에 정보를 반환하기 위해 매개변수로 member 객체를 요구하므로
        * 세션으로 memberId를 받아온 후 해당 Member 객체를 찾아 매개변수로 넘김
        */
        Member member = memberService.findMemberDao(memberId);
        Article article = new Article(articleAddDto.getTitle(),
                articleAddDto.getContent(),
                member
        );

        return articleRepository.save(article);
    }

    @Override
    public Article update(ArticleDto articleDto, Long articleId, Long memberId) {
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
            article.update(articleDto.getTitle(), articleDto.getContent());
            return articleRepository.save(article);
        }

        return null;
    }

    @Override
    public boolean delete(Long articleId, Long memberId) {
        /*
        삭제 요청을 받은 Article의 id를 받아온다. 존재하지 않는 id이면 삭제 불가
        삭제를 요청한 Member의 세션으로부터 MemberId를 받아온다. 로그아웃 상태인 유저가 삭제 요청하는 것을 방지
        게시글의 작성자 id와 삭제를 요청한 Member의 id를 비교. 같은 경우에만 게시글 삭제 허용
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
