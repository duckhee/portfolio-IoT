package kr.co.won.blog.service;

import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import org.springframework.data.domain.Page;

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
    public default BlogDomain creatBlog(BlogDomain blog, UserDomain loginUser) {
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
    public default BlogDomain deleteBlog(Long blogIdx, UserDomain loginUser) {
        return null;
    }
}
