package kr.co.won.blog.persistence;

import kr.co.won.blog.domain.BlogReplyDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogReplyPersistence extends JpaRepository<BlogReplyDomain, Long> {

    List<BlogReplyDomain> findByBlogIdx(Long blogIdx);

    Optional<BlogReplyDomain> findByIdx(Long replyIdx);
}
