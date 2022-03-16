package kr.co.won.study.service;

import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.persistence.StudyPersistence;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.persistence.UserPersistence;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.elasticsearch.common.SuppressLoggerChecks;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.JoinTable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
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
        Pageable pageable = page.makePageable(0, "idx");
        Page pagingResult = studyPersistence.pagingStudy(page.getType(), page.getKeyword(), pageable);
        return pagingResult;
    }

    /**
     * Study List Paging
     */

    @Override
    public Page pagingStudy(PageDto page, UserDomain authUser) {
        Pageable pageable = page.makePageable(0, "idx");
        Page pagingResult = studyPersistence.pagingStudy(page.getType(), page.getKeyword(), pageable, authUser);
        return pagingResult;
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

    /**
     * Study update Using Path
     */
    @Transactional
    @Override
    public StudyDomain updateStudy(String studyPath, StudyDomain updateStudy, UserDomain loginUser) {
        StudyDomain findStudy = studyPersistence.findByPath(studyPath).orElseThrow(() -> new IllegalArgumentException("wrong study path. check study path or not have study."));
        if (!isHaveAuth(loginUser, findStudy)) {
            throw new AccessDeniedException("not have auth.");
        }
        // update study information
        modelMapper.map(updateStudy, findStudy);
        log.info("get update study ::: {}", findStudy);
        return findStudy;
    }

    /**
     * Study Update Slice Using Path
     */

    @Transactional
    @Override
    public StudyDomain updateStudySlice(String path, StudyDomain updateStudy) {
        StudyDomain findStudy = studyPersistence.findByPath(path).orElseThrow(() -> new IllegalArgumentException("wrong study Path. check study path or not have study."));
        modelMapper.map(updateStudy, findStudy);
        return findStudy;
    }

    @Transactional
    @Override
    public StudyDomain updateStudySlice(String path, StudyDomain updateStudy, UserDomain loginUser) {
        return StudyService.super.updateStudySlice(path, updateStudy, loginUser);
    }

    /**
     * Study soft Delete Using study Idx
     */
    @Transactional
    @Override
    public StudyDomain deleteStudy(Long idx, UserDomain loginUser) {
        StudyDomain findStudy = studyPersistence.findByIdx(idx).orElseThrow(() -> new IllegalArgumentException("wrong study idx. not have study"));
        if (!isHaveAuth(loginUser, findStudy)) {
            return null;
        }
        findStudy.setDeleted(true);
        return findStudy;
    }

    @Transactional
    @Override
    public List<StudyDomain> deleteStudyBulkWithIdxes(List<Long> idxes, UserDomain loginUser) {
        List<StudyDomain> studies = studyPersistence.findByIdxIn(idxes);
        studies.forEach(study -> study.setDeleted(true));
        return studies;
    }

    /**
     * Study soft Delete Using study path
     */

    @Transactional
    @Override
    public StudyDomain deleteStudy(String path, UserDomain loginUser) {
        // find study
        StudyDomain findStudy = studyPersistence.findByPath(path).orElseThrow(() -> new IllegalArgumentException("wrong study path. not have study or wrong study path"));
        // user role check
        if (!isHaveAuth(loginUser, findStudy)) {
            return null;
        }
        // delete flag setting true
        findStudy.setDeleted(true);
        return findStudy;
    }

    @Transactional
    @Override
    public List<StudyDomain> deleteStudyBulkWithPaths(List<String> paths, UserDomain loginUser) {
        List<StudyDomain> findStudies = studyPersistence.findByPathIn(paths);
        if (!isHaveAuth(loginUser, findStudies)) {
            return null;
        }
        // study list setting delete flag true
        findStudies.forEach(study -> study.setDeleted(true));
        return findStudies;
    }

    /**
     * Study hard delete using study idx
     */
    @Transactional
    @Override
    public void deleteStudy(Long idx) {
        StudyDomain findStudy = studyPersistence.findByIdx(idx).orElseThrow(() -> new IllegalArgumentException("wrong study. not have study."));
        studyPersistence.delete(findStudy);
    }

    @Transactional
    @Override
    public void deleteStudyBulkWithIdxes(List<Long> idxes) {
        List<StudyDomain> findStudies = studyPersistence.findByIdxIn(idxes);
        studyPersistence.deleteAll(findStudies);
    }

    /**
     * Study hard delete using study path
     */

    @Transactional
    @Override
    public void deleteStudy(String path) {
        StudyDomain findStudy = studyPersistence.findByPath(path).orElseThrow(() -> new IllegalArgumentException("wrong study path. not have study or wrong study path"));
        studyPersistence.delete(findStudy);

    }

    @Transactional
    @Override
    public void deleteStudyBulkWithPaths(List<String> paths) {
        List<StudyDomain> findStudies = studyPersistence.findByPathIn(paths);
        studyPersistence.deleteAll(findStudies);
    }

    // study user role check
    private boolean isHaveAuth(UserDomain loginUser, StudyDomain study) {
        return loginUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER) || loginUser.getEmail().equals(study.getOrganizer()) || loginUser.getEmail().equals(study.getManager());
    }

    private boolean isHaveAuth(UserDomain loginUser, StudyDomain... studies) {
        if (loginUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER)) {
            return true;
        }
        return Arrays.stream(studies).allMatch(studyDomain -> studyDomain.getOrganizer().equals(loginUser.getEmail()));
    }

    private boolean isHaveAuth(UserDomain loginUser, List<StudyDomain> studies) {
        if (loginUser.hasRole(UserRoleType.ADMIN, UserRoleType.MANAGER)) {
            return true;
        }
        return studies.stream().allMatch(studyDomain -> studyDomain.getOrganizer().equals(loginUser.getEmail()));
    }

}
