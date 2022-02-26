package kr.co.won.study.service;

import kr.co.won.study.domain.StudyDomain;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import org.springframework.data.domain.Page;

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

    /**
     * paging study
     */
    public default Page pagingStudy(PageDto page, UserDomain authUser) {
        return null;
    }

    /**
     * detail study using id
     */
    public default StudyDomain findStudyWithIdx(Long studyIdx) {
        return null;
    }

    public default StudyDomain findStudyWithIdx(Long studyIdx, UserDomain loginUser) {
        return null;
    }

    /**
     * detail study using path
     */
    public default StudyDomain findStudyWithPath(String path) {
        return null;
    }

    public default StudyDomain findStudyWithPath(String path, UserDomain authUser) {
        return null;
    }

    /**
     * update Study
     */
    public default StudyDomain updateStudy(Long studyIdx, String userEmail) {
        return null;
    }

    public default StudyDomain updateStudy(Long studyIdx, UserDomain loginUser) {
        return null;
    }

    public default StudyDomain updateStudy(Long studyIdx, StudyDomain findStudy, StudyDomain updateStudy) {
        return null;
    }

    public default StudyDomain updateStudy(Long studyIdx, StudyDomain findStudy, StudyDomain updateStudy, UserDomain loginUser) {
        return null;
    }

    /**
     * update study slice
     */
    public default StudyDomain updateStudySlice(Long studyIdx, String userEmail) {
        return null;
    }

    public default StudyDomain updateStudySlice(Long studyIdx, UserDomain loginUser) {
        return null;
    }

    public default StudyDomain updateStudySlice(Long studyIdx, StudyDomain findStudy, StudyDomain updateStudy) {
        return null;
    }

    public default StudyDomain updateStudySlice(Long studyIdx, StudyDomain findStudy, StudyDomain updateStudy, UserDomain loginUser) {
        return null;
    }
    /** delete Study */
}
