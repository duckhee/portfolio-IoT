package kr.co.won.study.factory;

import kr.co.won.study.persistence.StudyPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@RequiredArgsConstructor
public class StudyFactory {

    private final StudyPersistence studyPersistence;
}
