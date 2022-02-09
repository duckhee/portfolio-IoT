package kr.co.won.blog.persistence;

import com.querydsl.jpa.JPQLQuery;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.domain.QBlogDomain;
import kr.co.won.blog.domain.QBlogReplyDomain;
import kr.co.won.blog.dto.BlogListDto;
import kr.co.won.blog.dto.QBlogListDto;
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
                .distinct()
                .select(blog)
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

    @Override
    public Page pagingListBlog(String type, String keyword, Pageable pageable) {
        QBlogDomain blog = blogDomain;
        QBlogReplyDomain reply = blogReplyDomain;

        JPQLQuery<BlogListDto> baseQuery = from(blog)
                .distinct()
                .select(new QBlogListDto(
                        blog.idx,
                        blog.title,
                        blog.writer,
                        blog.viewCnt.castToNum(Integer.TYPE),
                        blog.replies.size(),
                        blog.createdAt
                ))
                .leftJoin(reply)
                .on(reply.blog.eq(blog))
                .where(blog.idx.gt(0L))
                .fetchJoin();

        /** blog search writer and title */
        if (type != null) {
            switch (type) {
                case "writer":
                    baseQuery.where(blog.writer.like("%" + keyword + "%"));
                    break;
                case "title":
                    baseQuery.where(blog.title.like("%" + keyword + "%"));
                    break;
            }
        }

        long totalNumber = baseQuery.fetchCount();
        List<BlogListDto> result = baseQuery.orderBy(blog.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl(result, pageable, totalNumber);
    }
}
