package wanted.preonboarding.backend.domain.recruit.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import wanted.preonboarding.backend.global.audit.BaseTimeEntity;
import wanted.preonboarding.backend.domain.company.persistence.entity.Company;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Recruit extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private Long compensationFee;

    @Column(nullable = false, length = 1000)
    private String details;

    @Column(nullable = false, length = 500)
    private String skills;

    public void modify(String position, Long compensationFee, String details, String skills) {
        this.position = position;
        this.compensationFee = compensationFee;
        this.details = details;
        this.skills = skills;
    }
}
