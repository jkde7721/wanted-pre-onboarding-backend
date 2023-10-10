package wanted.preonboarding.backend.apply.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wanted.preonboarding.backend.apply.persistence.entity.Apply;

import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Long> {

    @Query("select a from Apply a where a.user.id = :userId and a.recruit.id = :recruitId")
    Optional<Apply> findByUserAndRecruit(@Param("userId") Long userId, @Param("recruitId") Long recruitId);
}
