package kr.co.won.blog.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogPersistenceExtension {

    public Page pagingBlog(String type, String keyword, Pageable pageable);

    public Page pagingListBlog(String type, String keyword, Pageable pageable);
}
