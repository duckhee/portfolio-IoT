package kr.co.won.blog.persistence;

import kr.co.won.blog.domain.BlogDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogPersistence extends JpaRepository<BlogDomain, Long> {

    Optional<BlogDomain> findByIdx(Long blogIdx);
}
