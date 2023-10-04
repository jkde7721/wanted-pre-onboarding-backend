package wanted.preonboarding.backend.user.persistence.entity;

import jakarta.persistence.*;
import wanted.preonboarding.backend.audit.BaseTimeEntity;

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
