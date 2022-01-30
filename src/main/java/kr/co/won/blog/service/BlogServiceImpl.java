package kr.co.won.blog.service;

import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.blog.persistence.BlogPersistence;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service(value = "blogService")
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final ModelMapper modelMapper;

    private final BlogPersistence blogPersistence;


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
    public BlogDomain creatBlog(BlogDomain blog, UserDomain loginUser) {
        if (loginUser == null) {
            throw new IllegalArgumentException("login first.");
        }
        blog.setWriter(loginUser.getName());
        blog.setWriterEmail(loginUser.getEmail());
        BlogDomain savedBlog = blogPersistence.save(blog);
        return savedBlog;
    }

    @Override
    public BlogDomain readBlog(Long blogIdx) {
        return BlogService.super.readBlog(blogIdx);
    }

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
        return BlogService.super.pagingBlog(page);
    }

    @Transactional
    @Override
    public BlogDomain deleteBlog(Long blogIdx, UserDomain loginUser) {
        return BlogService.super.deleteBlog(blogIdx, loginUser);
    }
}
