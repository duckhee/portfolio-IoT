package kr.co.won.study.persistence;

import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import org.springframework.data.domain.Page;

public interface StudyPersistenceExtension {

    public Page pagingStudy(PageDto page);

    public Page pagingStudy(PageDto page, UserDomain loginUser);
}
