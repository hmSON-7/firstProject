package miniProject.board.entity.report;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import miniProject.board.entity.BaseTimeEntity;
import miniProject.board.entity.Member;
import miniProject.board.entity.ReportStatus;

@Entity
@Getter
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

    public Report (String description, Member member) {
        this.description = description;
        this.member = member;
        this.reportStatus = ReportStatus.PENDING;
    }

    public void updateStatus(ReportStatus newStatus) {
        this.reportStatus = newStatus;
    }

}
