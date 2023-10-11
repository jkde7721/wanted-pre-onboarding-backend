package wanted.preonboarding.backend.domain.company.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import wanted.preonboarding.backend.global.audit.BaseTimeEntity;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Company extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nation;

    @Column(nullable = false)
    private String region;
}
