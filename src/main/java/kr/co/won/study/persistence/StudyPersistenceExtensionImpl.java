package kr.co.won.study.persistence;

import kr.co.won.study.domain.QStudyDomain;
import kr.co.won.study.domain.StudyDomain;
import kr.co.won.user.domain.QUserDomain;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import static kr.co.won.study.domain.QStudyDomain.studyDomain;
import static kr.co.won.user.domain.QUserDomain.userDomain;

@Transactional(readOnly = true)
public class StudyPersistenceExtensionImpl extends QuerydslRepositorySupport implements StudyPersistenceExtension {

    public StudyPersistenceExtensionImpl() {
        super(StudyDomain.class);
    }


    @Override
    public Page pagingStudy(PageDto page) {
        QStudyDomain study = studyDomain;
        QUserDomain user = userDomain;


        return null;
    }

    @Override
    public Page pagingStudy(PageDto page, UserDomain loginUser) {
        QStudyDomain study = studyDomain;
        QUserDomain user = userDomain;
        return null;
    }
}
