package kr.co.won.study.factory;

import kr.co.won.study.domain.StudyDomain;
import org.springframework.boot.test.context.TestComponent;

import java.net.URI;

@TestComponent
public class StudyDomainBuilderFactory {

    public StudyDomain makeDefaultStudyInfinityAllowUser(String organizer) {
        StudyDomain mockStudy = StudyDomain.builder()
                .name("default study")
                .allowMemberNumber(0)
                .description("default study description")
                .organizer(organizer)
                .path(URI.create("/testing/default-study").toString())
                .recruiting(false)
                .closed(true)
                .build();
        return mockStudy;
    }
}
