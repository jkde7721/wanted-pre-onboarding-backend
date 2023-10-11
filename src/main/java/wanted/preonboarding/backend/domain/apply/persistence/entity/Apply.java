package wanted.preonboarding.backend.domain.apply.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import wanted.preonboarding.backend.global.audit.BaseTimeEntity;
import wanted.preonboarding.backend.domain.recruit.persistence.entity.Recruit;
import wanted.preonboarding.backend.domain.user.persistence.entity.User;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Apply extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id", nullable = false)
    private Recruit recruit;
}
