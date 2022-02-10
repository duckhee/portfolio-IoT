package kr.co.won.blog.service;

import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.domain.BlogReplyDomain;
import kr.co.won.blog.domain.BlogResourceDomain;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

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
     * Create Blog and mapping blogResource
     */
    public default BlogDomain createBlogMapResource(BlogDomain blog, List<String> resourceSavNames) {
        return null;
    }

    /**
     * Create Blog
     */
    public default BlogDomain createBlog(BlogDomain blog, UserDomain loginUser) {
        return null;
    }

    /**
     * Create Blog and mapping blogResource
     */
    public default BlogDomain createBlogMapResource(BlogDomain blog, List<String> resourceSavNames, UserDomain loginUser) {
        return null;
    }

    /**
     * Read Blog
     * update view count update
     */
    public default BlogDomain readBlog(Long blogIdx) {
        return null;
    }

    /**
     * Read Blog With Replies
     */
    public default BlogDomain detailBlog(Long blogIdx) {
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
     * Update Blog parts
     */
    public default BlogDomain updatePartsBlog(Long blogIdx, BlogDomain updateBlog) {
        return null;
    }

    /**
     * Update Blog parts
     */
    public default BlogDomain updatePartsBlog(Long blogIdx, BlogDomain updateBlog, UserDomain authUser) {
        return null;
    }

    /**
     * Paging Blog
     */
    public default Page pagingBlog(PageDto page) {
        return null;
    }

    public default Page pagingListBlog(PageDto page) {
        return null;
    }

    /**
     * Delete Blog
     */
    public default void deleteBlog(Long blogIdx, UserDomain loginUser) {
        return;
    }

    public default void bulkDeleteBlogs(List<Long> blogIdxes) {
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

    /**
     * blog reply remove
     * remove failed return null
     */
    public default BlogReplyDomain removeReply(Long blogIdx, Long replyIdx, UserDomain loginUser) {
        return null;
    }

    /**
     * blog resource update
     */
    public default BlogResourceDomain createBlogResource(Long blogIdx, MultipartFile resource) {
        return null;
    }

    /**
     * blog resource update bulk
     */
    public default BlogResourceDomain createBulkBlogResource(Long blogIdx, List<MultipartFile> resource) {
        return null;
    }

    /**
     * bog resource add
     */
    public default BlogResourceDomain createBlogResource(Long blogIdx, BlogResourceDomain resource) {
        return null;
    }

    /**
     * blog resource find using blogs idx
     */
    public default BlogResourceDomain findBlogResource(Long blogIdx) {
        return null;
    }

    public default List<BlogResourceDomain> findBlogResourceAll(Long blogIdx) {
        return null;
    }

}
