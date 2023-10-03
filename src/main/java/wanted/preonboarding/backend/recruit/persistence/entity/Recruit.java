package wanted.preonboarding.backend.recruit.persistence.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import wanted.preonboarding.backend.company.persistence.entity.Company;

@Getter
@Builder
@Entity
public class Recruit {

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
}
