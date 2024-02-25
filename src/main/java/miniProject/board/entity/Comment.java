package miniProject.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import miniProject.board.dto.CommentDto;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="articleId")
    private Article article;

    @ManyToOne
    @JoinColumn(name="memberId")
    private Member member;

    @Column
    private String body;

    public Comment createComment(CommentDto dto, Article article, Member member) {
        if(dto.getId() != null)
            throw new IllegalArgumentException("댓글 생성 실패! 댓글의 id가 없어야 합니다!");
        if(!dto.getArticleId().equals(article.getId()))
            throw new IllegalArgumentException("댓글 생성 실패! 게시글의 id가 잘못됐습니다!");

        return new Comment(
                dto.getId(),
                article,
                member,
                dto.getBody()
        );
    }

    public void updateComment(CommentDto dto) {
        if(this.id.equals(dto.getId())) {
            throw new IllegalArgumentException("댓글 수정 실패! 잘못된 id가 입력됐습니다!");
        }

        if(dto.getBody() != null) {
            this.body = dto.getBody();
        }
    }
}
