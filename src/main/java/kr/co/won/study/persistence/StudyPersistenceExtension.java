package kr.co.won.study.persistence;

import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudyPersistenceExtension {

    public Page pagingStudy(String type, String keyword, Pageable pageable);

    public Page pagingStudy(String type, String keyword, Pageable pageable, UserDomain loginUser);
}
