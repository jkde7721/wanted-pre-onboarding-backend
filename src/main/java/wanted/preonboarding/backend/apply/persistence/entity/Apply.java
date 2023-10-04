package wanted.preonboarding.backend.apply.persistence.entity;

import jakarta.persistence.*;
import wanted.preonboarding.backend.audit.BaseTimeEntity;
import wanted.preonboarding.backend.recruit.persistence.entity.Recruit;
import wanted.preonboarding.backend.user.persistence.entity.User;

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
