package kr.co.won.user.persistence;

import kr.co.won.user.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPersistence extends JpaRepository<UserDomain, Long> {

    /**
     * user email check
     */
    boolean existsByEmail(String email);

    Optional<UserDomain> findByEmail(String email);
}
