package kr.co.won.study.factory;

import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.persistence.StudyPersistence;
import kr.co.won.user.domain.UserDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;

import java.time.LocalDateTime;
import java.util.List;

@TestComponent
@RequiredArgsConstructor
public class StudyFactory {

    private final StudyPersistence studyPersistence;

    public StudyDomain makeMockStudy() {
        return null;
    }

    public StudyDomain makeStudy(String path, String studyName, int allowMemberNumber, UserDomain loginUser) {
        String organizer =  loginUser.getEmail();
        LocalDateTime nowTime = LocalDateTime.now();

        StudyDomain testStudy = StudyDomain.builder()
                .name(studyName)
                .shortDescription("shortDescription")
                .description("description")
                .path(path)
                .allowMemberNumber(allowMemberNumber)
                .organizer(organizer)
                .createdAt(nowTime)
                .updatedAt(nowTime)
                .build();
        StudyDomain savedStudy = studyPersistence.save(testStudy);
        return savedStudy;
    }

    public List<StudyDomain> makeBulkStudies() {
        return null;
    }


}
