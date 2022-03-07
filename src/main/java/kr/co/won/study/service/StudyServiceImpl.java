package kr.co.won.study.service;

import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.persistence.StudyPersistence;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.persistence.UserPersistence;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.SuppressLoggerChecks;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service(value = "studyService")
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {

    @Resource(name = "skipModelMapper")
    private final ModelMapper modelMapper;

    @Resource(name = "notSkipModelMapper")
    private final ModelMapper updateModelMapper;

    private final UserPersistence userPersistence;

    private final StudyPersistence studyPersistence;

    @Transactional
    @Override
    public StudyDomain createStudy(StudyDomain studyDomain) {
        LocalDateTime nowTime = LocalDateTime.now();
        studyDomain.setCreatedAt(nowTime);
        studyDomain.setUpdatedAt(nowTime);
        StudyDomain savedStudy = studyPersistence.save(studyDomain);
        return savedStudy;
    }

    @Transactional
    @Override
    public StudyDomain createStudy(StudyDomain studyDomain, String organizer) {
        studyDomain.setOrganizer(organizer);
        LocalDateTime nowTime = LocalDateTime.now();
        studyDomain.setCreatedAt(nowTime);
        studyDomain.setUpdatedAt(nowTime);
        StudyDomain saveStudy = studyPersistence.save(studyDomain);
        return saveStudy;
    }

    @Transactional
    @Override
    public StudyDomain createStudy(StudyDomain studyDomain, UserDomain loginUser) {
        studyDomain.setOrganizer(loginUser.getEmail());
        LocalDateTime nowTime = LocalDateTime.now();
        studyDomain.setCreatedAt(nowTime);
        studyDomain.setUpdatedAt(nowTime);
        StudyDomain savedStudy = studyPersistence.save(studyDomain);
        return savedStudy;
    }

    @Override
    public Page pagingStudy(PageDto page) {
        return StudyService.super.pagingStudy(page);
    }

    /**
     * Study List Paging
     */

    @Override
    public Page pagingStudy(PageDto page, UserDomain authUser) {
        return StudyService.super.pagingStudy(page, authUser);
    }

    /**
     * Study Find Using Idx
     */
    @Override
    public StudyDomain findStudyWithIdx(Long studyIdx) {
        StudyDomain findStudy = studyPersistence.findByIdx(studyIdx).orElseThrow(() ->
                new IllegalArgumentException("study not found."));
        return findStudy;
    }

    /**
     * Study Find Using Path
     */
    @Override
    public StudyDomain findStudyWithPath(String path) {
        StudyDomain findStudy = studyPersistence.findByPath(path).orElseThrow(() ->
                new IllegalArgumentException("study not found."));
        return findStudy;
    }

    @Override
    public StudyDomain findStudyWithPath(String path, UserDomain authUser) {
        StudyDomain findStudy = studyPersistence.findByPath(path).orElseThrow(() ->
                new IllegalArgumentException("study not found."));
        if (isHaveAuth(authUser, findStudy)) {
            return findStudy;
        }
        return null;
    }

    // study user role check
    private boolean isHaveAuth(UserDomain loginUser, StudyDomain study) {
        return loginUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER) || loginUser.getEmail().equals(study.getOrganizer()) || loginUser.getEmail().equals(study.getManager());
    }
}
