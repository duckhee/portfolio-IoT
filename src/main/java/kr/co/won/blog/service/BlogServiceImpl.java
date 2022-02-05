package kr.co.won.blog.service;

import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.domain.BlogReplyDomain;
import kr.co.won.blog.persistence.BlogPersistence;
import kr.co.won.blog.persistence.BlogReplyPersistence;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service(value = "blogService")
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final ModelMapper modelMapper;

    private final BlogPersistence blogPersistence;

    private final BlogReplyPersistence blogReplyPersistence;


    @Transactional
    @Override
    public BlogDomain createBlog(BlogDomain blog, String email, String writer) {
        blog.setWriterEmail(email);
        blog.setWriter(writer);
        BlogDomain savedBlog = blogPersistence.save(blog);
        return savedBlog;
    }

    @Transactional
    @Override
    public BlogDomain createBlog(BlogDomain blog) {
        BlogDomain savedBlog = blogPersistence.save(blog);
        return savedBlog;
    }

    @Transactional
    @Override
    public BlogDomain createBlog(BlogDomain blog, UserDomain loginUser) {
        if (loginUser == null) {
            throw new IllegalArgumentException("login first.");
        }
        blog.setWriter(loginUser.getName());
        blog.setWriterEmail(loginUser.getEmail());
        BlogDomain savedBlog = blogPersistence.save(blog);
        return savedBlog;
    }

    @Transactional
    @Override
    public BlogDomain readBlog(Long blogIdx) {
        BlogDomain findBlog = blogPersistence.findByIdx(blogIdx).orElseThrow(() ->
                new IllegalArgumentException("not hve blog."));
        findBlog.setViewCnt(findBlog.getViewCnt() + 1);
        return findBlog;
    }

    @Transactional
    @Override
    public BlogDomain detailBlog(Long blogIdx) {
        BlogDomain findBlog = blogPersistence.findWithReplyByIdx(blogIdx).orElseThrow(() ->
                new IllegalArgumentException("not have blogs."));
        // view cnt update
        findBlog.setViewCnt(findBlog.getViewCnt() + 1);
//        log.info("findBlog reply :: {}", findBlog.getReplies());
        return findBlog;
    }

    @Transactional
    @Override
    public BlogDomain updateBlog(Long blogIdx, BlogDomain updateBlog) {
        return BlogService.super.updateBlog(blogIdx, updateBlog);
    }

    @Transactional
    @Override
    public BlogDomain updateBlog(Long blogIdx, BlogDomain updateBlog, UserDomain loginUser) {
        return BlogService.super.updateBlog(blogIdx, updateBlog, loginUser);
    }

    @Override
    public Page pagingBlog(PageDto page) {
        Pageable pageable = page.makePageable(0, "idx");
        Page pagingResult = blogPersistence.pagingBlog(page.getType(), page.getKeyword(), pageable);
        return pagingResult;
    }

    @Transactional
    @Override
    public void deleteBlog(Long blogIdx, UserDomain loginUser) {
        BlogDomain findBlog = blogPersistence.findByIdx(blogIdx).orElseThrow(() ->
                new IllegalArgumentException("not have blogs."));
        // not match writer
        if (!findBlog.isOwner(loginUser.getEmail())) {
            throw new AccessDeniedException("not have auth user.");
        }

        blogPersistence.delete(findBlog);
    }


    /**
     * Blog Reply
     */
    @Transactional
    @Override
    public BlogReplyDomain createReply(Long blogIdx, BlogReplyDomain reply, UserDomain loginUser) {
        BlogDomain findBlog = blogPersistence.findByIdx(blogIdx).orElseThrow(() ->
                new IllegalArgumentException("not have blog."));
        reply.setReplyer(loginUser.getName());
        reply.setReplyerEmail(loginUser.getEmail());
        findBlog.addReply(reply);

        return reply;
    }

    @Override
    public List<BlogReplyDomain> listReply(Long blogIdx) {
        List<BlogReplyDomain> findReplies = blogReplyPersistence.findByBlogIdx(blogIdx);
        return findReplies;
    }
}
