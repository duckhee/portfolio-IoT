package kr.co.won.user.persistence;

import kr.co.won.user.domain.UserDomain;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPersistence extends JpaRepository<UserDomain, Long>, UserPersistenceExtension {

    /**
     * user email check
     */
    boolean existsByEmail(String email);

    Optional<UserDomain> findByEmail(String email);

    @EntityGraph(value = "user.withRole", type = EntityGraph.EntityGraphType.FETCH)
    Optional<UserDomain> findWithRoleByEmail(String email);

    @EntityGraph(value = "user.withRole", type = EntityGraph.EntityGraphType.FETCH)
    Optional<UserDomain> findWithRoleByIdx(Long idx);

    Optional<UserDomain> findByIdx(Long idx);
}
