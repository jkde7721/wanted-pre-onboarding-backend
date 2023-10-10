package wanted.preonboarding.backend.user.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import wanted.preonboarding.backend.audit.BaseTimeEntity;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer careerYear = 0;
}
