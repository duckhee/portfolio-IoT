package kr.co.won.blog.persistence;

import kr.co.won.blog.domain.BlogReplyDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogReplyPersistence extends JpaRepository<BlogReplyDomain, Long> {

    List<BlogReplyDomain> findByBlogIdx(Long blogIdx);
}
