package kr.co.won.blog.service;

import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.domain.BlogReplyDomain;
import kr.co.won.blog.domain.BlogResourceDomain;
import kr.co.won.blog.persistence.BlogPersistence;
import kr.co.won.blog.persistence.BlogReplyPersistence;
import kr.co.won.blog.persistence.BlogResourcePersistence;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.persistence.UserPersistence;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service(value = "blogService")
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    @Resource(name = "skipModelMapper")
    private final ModelMapper modelMapper;

    @Resource(name = "notSkipModelMapper")
    private final ModelMapper putModelMapper;

    private final UserPersistence userPersistence;

    private final BlogPersistence blogPersistence;

    private final BlogReplyPersistence blogReplyPersistence;

    private final BlogResourcePersistence resourcePersistence;


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
    public BlogDomain createBlogMapResource(BlogDomain blog, List<String> resourceSavNames) {
        BlogDomain savedBlog = blogPersistence.save(blog);
        List<BlogResourceDomain> findResources = resourcePersistence.findBySaveFileNameIn(resourceSavNames);
        // resource set blog
        findResources.forEach(resource -> resource.setBlog(savedBlog));
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

    /**
     * blog read function call update view count number update
     */
    @Transactional
    @Override
    public BlogDomain readBlog(Long blogIdx) {
        BlogDomain findBlog = blogPersistence.findByIdx(blogIdx).orElseThrow(() ->
                new IllegalArgumentException("not hve blog."));
        findBlog.setViewCnt(findBlog.getViewCnt() + 1);
        return findBlog;
    }

    /**
     * blog read function call update view count number update
     */
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
        BlogDomain findBlog = blogPersistence.findByIdx(blogIdx).orElseThrow(() ->
                new IllegalArgumentException("not have blogs."));
        // update input data check
        if (updateBlog.getTitle() != null || !updateBlog.getTitle().isBlank()) {
            findBlog.setTitle(updateBlog.getTitle());
        }
        if (updateBlog.getContent() != null || !updateBlog.getContent().isBlank()) {
            findBlog.setContent(updateBlog.getContent());
        }
        if (updateBlog.getProjectUrl() != null || !updateBlog.getProjectUrl().isBlank()) {
            findBlog.setContent(updateBlog.getProjectUrl());
        }
        return findBlog;
    }

    @Transactional
    @Override
    public BlogDomain updateBlog(Long blogIdx, BlogDomain updateBlog, UserDomain loginUser) {
        // find update blog
        BlogDomain findBlog = blogPersistence.findByIdx(blogIdx).orElseThrow(() ->
                new IllegalArgumentException("not have blogs."));
        // user role check and owner check
        if (!isHaveAuth(loginUser, findBlog)) {
            throw new AccessDeniedException("not have auth.");
        }
        modelMapper.map(updateBlog, findBlog);
        // update input data check
        if (updateBlog.getTitle() != null || !updateBlog.getTitle().isBlank()) {
            findBlog.setTitle(updateBlog.getTitle());
        }
        if (updateBlog.getContent() != null || !updateBlog.getContent().isBlank()) {
            findBlog.setContent(updateBlog.getContent());
        }
        if (updateBlog.getProjectUrl() != null || !updateBlog.getProjectUrl().isBlank()) {
            findBlog.setContent(updateBlog.getProjectUrl());
        }
        return findBlog;
    }

    @Transactional
    @Override
    public BlogDomain updatePartsBlog(Long blogIdx, BlogDomain updateBlog) {
        return BlogService.super.updatePartsBlog(blogIdx, updateBlog);
    }

    @Transactional
    @Override
    public BlogDomain updatePartsBlog(Long blogIdx, BlogDomain updateBlog, UserDomain authUser) {

        return BlogService.super.updatePartsBlog(blogIdx, updateBlog, authUser);
    }

    @Override
    public Page pagingBlog(PageDto page) {
        Pageable pageable = page.makePageable(0, "idx");
        Page pagingResult = blogPersistence.pagingBlog(page.getType(), page.getKeyword(), pageable);
        return pagingResult;
    }

    @Override
    public Page pagingListBlog(PageDto page) {
        Pageable pageable = page.makePageable(0, "idx");
        Page pageResult = blogPersistence.pagingListBlog(page.getType(), page.getKeyword(), pageable);
        return pageResult;
    }

    @Transactional
    @Override
    public void deleteBlog(Long blogIdx, UserDomain loginUser) {
        BlogDomain findBlog = blogPersistence.findByIdx(blogIdx).orElseThrow(() ->
                new IllegalArgumentException("not have blogs."));
        // not match writer
        if (!isHaveAuth(loginUser, findBlog)) {
            throw new AccessDeniedException("not have auth user.");
        }
        blogPersistence.delete(findBlog);
    }

    @Transactional
    @Override
    public void bulkDeleteBlogs(List<Long> blogIdxes, UserDomain loginUser) {
        BlogService.super.bulkDeleteBlogs(blogIdxes, loginUser);
    }


    /**
     * Blog Reply
     */
    @Transactional
    @Override
    public BlogReplyDomain createReply(Long blogIdx, BlogReplyDomain reply) {
        BlogDomain findBlog = blogPersistence.findByIdx(blogIdx).orElseThrow(() ->
                new IllegalArgumentException("not have blog."));
        findBlog.addReply(reply);
        return reply;
    }

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

    @Transactional
    @Override
    public BlogReplyDomain createReply(Long blogIdx, BlogReplyDomain reply, String userEmail, String userName) {
        BlogDomain findBlog = blogPersistence.findByIdx(blogIdx).orElseThrow(() ->
                new IllegalArgumentException("not have blog."));
        reply.setReplyerEmail(userEmail);
        reply.setReplyer(userName);
        findBlog.addReply(reply);
        return reply;
    }

    /**
     * Paging replies
     */
    @Override
    public Page pagingReply(PageDto page) {
        return BlogService.super.pagingReply(page);
    }

    @Override
    public List<BlogReplyDomain> listReply(Long blogIdx) {
        List<BlogReplyDomain> findReplies = blogReplyPersistence.findByBlogIdxOrderByCreatedAtDesc(blogIdx);
        return findReplies;
    }

    @Override
    public BlogReplyDomain readReply(Long replyIdx) {
        BlogReplyDomain findReply = blogReplyPersistence.findByIdx(replyIdx).orElseThrow(() ->
                new IllegalArgumentException(replyIdx + " not have reply."));
        return findReply;
    }

    @Override
    public BlogReplyDomain readReply(Long blogIdx, Long replyIdx) {
        BlogReplyDomain findReply = blogReplyPersistence.findByIdx(replyIdx).orElseThrow(() ->
                new IllegalArgumentException(replyIdx + " not have reply."));
        return findReply;
    }

    /**
     * blog reply remove
     * check blog writer same loginUser,
     * check reply writer same loginUser,
     * loginUser Role Check
     */
    @Transactional
    @Override
    public BlogReplyDomain removeReply(Long blogIdx, Long replyIdx, UserDomain loginUser) {
        BlogReplyDomain findReply = blogReplyPersistence.findByIdx(replyIdx).orElseThrow(() ->
                new IllegalArgumentException("not have blog replies."));
        // get blog
        BlogDomain findBlog = findReply.getBlog();
        // user role Admin or Manager, blog writer, replyer
        if (isHaveAuth(loginUser, findBlog, findReply)) {
            blogReplyPersistence.delete(findReply);
            return findReply;
        }
        // delete failed return null
        return null;
    }

    @Transactional
    @Override
    public void removeReplies(Long blogIdx, List<Long> repliesIdx, UserDomain loginUser) {
        BlogService.super.removeReplies(blogIdx, repliesIdx, loginUser);
    }

    // this is match user update possible
    private boolean isHaveAuth(UserDomain loginUser, BlogDomain findBlog) {
        return loginUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER) || loginUser.getEmail().equals(findBlog.getWriterEmail());
    }

    private boolean isHaveAuth(UserDomain loginUser, BlogDomain findBlog, BlogReplyDomain reply) {
        return loginUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER) || loginUser.getEmail().equals(findBlog.getWriterEmail()) || reply.getReplyerEmail().equals(loginUser.getEmail());
    }
}
