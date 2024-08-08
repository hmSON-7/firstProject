package miniProject.board.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;




    // 1개의 comment와 여러개의 신고 연결
    @ManyToOne
    @JoinColumn (name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    public Report (String description, Member member, ReportStatus status) {
        this.description = description;
        this.member = member;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public void updateStatus(ReportStatus newStatus) {
        this.status = newStatus;
    }

}
