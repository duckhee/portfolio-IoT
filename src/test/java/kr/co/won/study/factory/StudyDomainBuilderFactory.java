package kr.co.won.study.factory;

import kr.co.won.study.domain.StudyDomain;
import org.springframework.boot.test.context.TestComponent;

import java.net.URI;

@TestComponent
public class StudyDomainBuilderFactory {

    public StudyDomain makeDefaultStudyInfinityAllowUser() {
        StudyDomain mockStudy = StudyDomain.builder()
                .name("default study")
                .arrowMemberNumber(0)
                .description("default study description")
                .organizer("tester")
                .path(URI.create("/testing/default-study").toString())
                .recruiting(false)
                .closed(true)
                .build();
        return mockStudy;
    }
}
