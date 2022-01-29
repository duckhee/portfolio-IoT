package kr.co.won.user.persistence;

import kr.co.won.util.page.PageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserPersistenceExtension {

    public Page pagingUser(String type, String keyword, Pageable pageable);
}
