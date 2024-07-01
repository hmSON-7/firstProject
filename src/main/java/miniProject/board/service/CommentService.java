package miniProject.board.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import miniProject.board.dto.CommentDto;
import miniProject.board.entity.Article;
import miniProject.board.entity.Comment;
import miniProject.board.entity.Member;
import miniProject.board.repository.ArticleRepository;
import miniProject.board.repository.CommentRepository;
import miniProject.board.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service

public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public Long commentSave(String username, Long id, CommentDto.CommentRequest commentRequest) {
        Optional<Member> __member = memberRepository.findByUsername(username);
        Member member;
        if (__member.isPresent()) { // Optional이 값으로 채워져 있는지 확인
            member = __member.get(); // User 객체 추출
        } else {
            System.out.println("사용자가 존재하지 않습니다: " + username);
            return null;
        }

            Article article = articleRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않음"));

            commentRequest.setMember(member);
            commentRequest.setArticle(article);

            Comment comment = commentRequest.toEntity();
            commentRepository.save(comment);

            return commentRequest.getCommentId();
        }
    }
