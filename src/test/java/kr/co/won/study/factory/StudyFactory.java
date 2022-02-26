package kr.co.won.study.factory;

import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.persistence.StudyPersistence;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@RequiredArgsConstructor
public class StudyFactory {

    private final StudyPersistence studyPersistence;

    public StudyDomain makeMockStudy() {
        return null;
    }

    public StudyDomain makeStudy() {
        return null;
    }


}
