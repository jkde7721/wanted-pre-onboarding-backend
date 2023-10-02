package wanted.preonboarding.backend.user.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.preonboarding.backend.user.persistence.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
