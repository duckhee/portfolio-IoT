package kr.co.won.blog.persistence;

import kr.co.won.blog.domain.BlogResourceDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogResourcePersistence extends JpaRepository<BlogResourceDomain, Long> {
}
