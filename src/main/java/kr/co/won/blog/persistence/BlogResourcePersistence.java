package kr.co.won.blog.persistence;

import kr.co.won.blog.domain.BlogResourceDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogResourcePersistence extends JpaRepository<BlogResourceDomain, Long> {

    public List<BlogResourceDomain> findBySaveFileNameIn(List<String> savedNames);
}
