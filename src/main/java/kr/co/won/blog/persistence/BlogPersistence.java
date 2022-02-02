package kr.co.won.blog.persistence;

import kr.co.won.blog.domain.BlogDomain;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface BlogPersistence extends JpaRepository<BlogDomain, Long>, BlogPersistenceExtension {

    Optional<BlogDomain> findByIdx(Long blogIdx);

    @EntityGraph(value = "blog.withReply")
    Optional<BlogDomain> findWithReplyByIdx(Long blogIdx);

}
