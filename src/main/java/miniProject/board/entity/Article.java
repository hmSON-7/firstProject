package miniProject.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    @Column
    private String title;

    @Column
    private String content;

    /*ManyToOne
    * 게시글 작성자의 정보를 Member 엔티티에서 가져옴
    */
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    /*OneToMany
    * 하나의 글이 여러 댓글을 가지도록 함
    * 글이 삭제되면 댓글도 삭제
    */
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    /*
    * 게시글 작성 시간
    * Null 값 입력 불가, 업데이트 불가
    */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /*
    * 게시글 업데이트 시간
    * 게시글 수정시 갱신되는 속성*/
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Article(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
}
