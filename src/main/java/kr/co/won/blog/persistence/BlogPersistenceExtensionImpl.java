package kr.co.won.blog.persistence;

import com.querydsl.jpa.JPQLQuery;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.domain.QBlogDomain;
import kr.co.won.blog.domain.QBlogReplyDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.co.won.blog.domain.QBlogDomain.blogDomain;
import static kr.co.won.blog.domain.QBlogReplyDomain.blogReplyDomain;

@Transactional(readOnly = true)
public class BlogPersistenceExtensionImpl extends QuerydslRepositorySupport implements BlogPersistenceExtension {

    public BlogPersistenceExtensionImpl() {
        super(BlogDomain.class);
    }

    @Override
    public Page pagingBlog(String type, String keyword, Pageable pageable) {
        QBlogDomain blog = blogDomain;
        QBlogReplyDomain reply = blogReplyDomain;

        JPQLQuery<BlogDomain> defaultQuery = from(blog)
                .select(blog)
                .distinct()
                .innerJoin(blog.replies, reply)
                .fetchJoin()
                .where(blog.idx.gt(0L));

        /** search */
        if (type != null) {

        }

        long totalNumber = defaultQuery.fetchCount();
        List<BlogDomain> result = defaultQuery.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(blog.createdAt.desc())
                .fetch();

        return new PageImpl(result, pageable, totalNumber);
    }
}
