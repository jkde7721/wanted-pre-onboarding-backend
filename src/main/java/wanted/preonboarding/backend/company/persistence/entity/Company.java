package wanted.preonboarding.backend.company.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Company {

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
