package kr.co.won.blog.service;

import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.domain.BlogReplyDomain;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BlogService {

    /**
     * Create Blog
     */
    public default BlogDomain createBlog(BlogDomain blog, String email, String writer) {
        return null;
    }

    /**
     * Create Blog
     */
    public default BlogDomain createBlog(BlogDomain blog) {
        return null;
    }

    /**
     * Create Blog
     */
    public default BlogDomain createBlog(BlogDomain blog, UserDomain loginUser) {
        return null;
    }

    /**
     * Read Blog
     */
    public default BlogDomain readBlog(Long blogIdx) {
        return null;
    }


    /**
     * Update Blog
     */
    public default BlogDomain updateBlog(Long blogIdx, BlogDomain updateBlog) {
        return null;
    }

    /**
     * Update Blog
     */
    public default BlogDomain updateBlog(Long blogIdx, BlogDomain updateBlog, UserDomain authUser) {
        return null;
    }

    /**
     * Paging Blog
     */
    public default Page pagingBlog(PageDto page) {
        return null;
    }

    /**
     * Delete Blog
     */
    public default void deleteBlog(Long blogIdx, UserDomain loginUser) {
        return;
    }

    /**
     * blog reply add
     */
    public default BlogReplyDomain createReply(Long blogIdx, BlogReplyDomain reply, UserDomain loginUser) {
        return null;
    }

    public default BlogReplyDomain createReply(Long blogIdx, BlogReplyDomain reply, String userEmail, String userName) {
        return null;
    }

    public default BlogReplyDomain createReply(Long blogIdx, BlogReplyDomain reply) {
        return null;
    }

    /**
     * blog reply read
     */
    public default BlogReplyDomain readReply(Long replyIdx) {
        return null;
    }

    /**
     * blog reply List
     */
    public default List<BlogReplyDomain> listReply(Long blogIdx) {
        return null;
    }

    /**
     * blog reply paging list
     */
    public default Page pagingReply(PageDto page) {
        return null;
    }
}
