package kr.co.won.blog.persistence;

import kr.co.won.blog.domain.BlogDomain;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface BlogPersistence extends JpaRepository<BlogDomain, Long>, BlogPersistenceExtension {

    Optional<BlogDomain> findByIdx(Long blogIdx);

    @EntityGraph(value = "blog.withReply", type = EntityGraph.EntityGraphType.LOAD)
    Optional<BlogDomain> findWithReplyByIdx(Long blogIdx);

    @Modifying
    @Query(value = "delete from BlogDomain blog where blog.idx in(:idxes)")
    void deleteAllByIdx(@Param(value = "idxes") List<Long> idxes);

    Optional<BlogDomain> findFirstByWriterOrderByCreatedAtDesc(String writer);

    List<BlogDomain> findTop10ByWriterOrderByCreatedAtDesc(String writer);

}
