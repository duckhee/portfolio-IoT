package kr.co.won.user.persistence;

import kr.co.won.user.domain.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPersistence extends JpaRepository<UserDomain, Long> {
}
