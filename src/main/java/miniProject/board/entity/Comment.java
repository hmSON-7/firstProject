package miniProject.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Comment(String content, Article article, Member member) {
        this.content = content;
        this.article = article;
        this.member = member;
    }

    public static Comment createComment(String content, Article article, Member member) {
        return new Comment(content, article, member);
    }

    public void update(String content){
        this.content = content;
    }
}
