package kr.co.won.blog.persistence;

import kr.co.won.blog.domain.BlogDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPersistence extends JpaRepository<BlogDomain, Long> {

}
