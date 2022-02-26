package kr.co.won.study.service;

import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.persistence.StudyPersistence;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "studyService")
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {

    private final ModelMapper modelMapper;

    private final UserPersistence userPersistence;

    private final StudyPersistence studyPersistence;

    @Transactional
    @Override
    public StudyDomain createStudy(StudyDomain studyDomain) {
        StudyDomain savedStudy = studyPersistence.save(studyDomain);
        return savedStudy;
    }

    @Transactional
    @Override
    public StudyDomain createStudy(StudyDomain studyDomain, String organizer) {
        studyDomain.setOrganizer(organizer);
        StudyDomain saveStudy = studyPersistence.save(studyDomain);
        return saveStudy;
    }

    @Transactional
    @Override
    public StudyDomain createStudy(StudyDomain studyDomain, UserDomain loginUser) {
        studyDomain.setOrganizer(loginUser.getEmail());
        StudyDomain savedStudy = studyPersistence.save(studyDomain);
        return savedStudy;
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



}
