package kr.co.won.study.factory;

import kr.co.won.study.domain.StudyDomain;
import org.springframework.boot.test.context.TestComponent;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@TestComponent
public class StudyDomainBuilderFactory {

    public StudyDomain makeDefaultStudyInfinityAllowUser(String organizer) {
        StudyDomain mockStudy = StudyDomain.builder()
                .idx(1L)
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

    public StudyDomain makeDefaultStudyInfinityAllowUser(String organizer, String studyName, String description, String uri) {
        StudyDomain mockStudy = StudyDomain.builder()
                .idx(1L)
                .name(studyName)
                .allowMemberNumber(0)
                .description(description)
                .organizer(organizer)
                .path(URI.create(uri).toString())
                .recruiting(false)
                .closed(true)
                .build();
        return mockStudy;
    }

    public List<StudyDomain> makeMockListStudy(String organizer, int number) {
        List<StudyDomain> list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            StudyDomain mockStudy = StudyDomain.builder()
                    .idx(Long.valueOf(i+1))
                    .name("default study" + i)
                    .allowMemberNumber(i)
                    .description("default study description" + i)
                    .organizer(organizer)
                    .path(URI.create("/testing/default-study" + i).toString())
                    .recruiting(false)
                    .closed(true)
                    .build();
            list.add(mockStudy);
        }
        return list;
    }
}
