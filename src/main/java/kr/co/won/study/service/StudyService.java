package kr.co.won.study.service;

import kr.co.won.study.domain.StudyDomain;
import kr.co.won.user.domain.UserDomain;

public interface StudyService {

    /**
     * create study
     */
    public default StudyDomain createStudy(StudyDomain studyDomain) {
        return null;
    }

    public default StudyDomain createStudy(StudyDomain studyDomain, String organizer) {
        return null;
    }

    public default StudyDomain createStudy(StudyDomain studyDomain, UserDomain loginUser) {
        return null;
    }
}
