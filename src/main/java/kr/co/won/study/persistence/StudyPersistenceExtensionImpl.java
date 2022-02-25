package kr.co.won.study.persistence;

import kr.co.won.study.domain.StudyDomain;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class StudyPersistenceExtensionImpl extends QuerydslRepositorySupport implements StudyPersistenceExtension {

    public StudyPersistenceExtensionImpl() {
        super(StudyDomain.class);
    }


    @Override
    public Page pagingStudy(PageDto page) {
        return null;
    }

    @Override
    public Page pagingStudy(PageDto page, UserDomain loginUser) {
        return null;
    }
}
