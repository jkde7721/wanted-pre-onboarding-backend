package wanted.preonboarding.backend.domain.user.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.preonboarding.backend.domain.user.persistence.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
