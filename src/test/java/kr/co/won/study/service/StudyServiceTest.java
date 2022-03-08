package kr.co.won.study.service;

import kr.co.won.config.TestAppConfiguration;
import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.factory.StudyDomainBuilderFactory;
import kr.co.won.study.persistence.StudyPersistence;
import kr.co.won.user.persistence.UserPersistence;
import kr.co.won.util.page.PageDto;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@Slf4j
@Import(value = {TestAppConfiguration.class, StudyDomainBuilderFactory.class})
@ExtendWith(value = {MockitoExtension.class})
class StudyServiceTest {

    private TestAppConfiguration configuration = new TestAppConfiguration();

    private StudyDomainBuilderFactory studyFactory = new StudyDomainBuilderFactory();

    @Spy
    @Resource(name = "skipModelMapper")
    private ModelMapper modelMapper;

    @Spy
    @Resource(name = "notSkipModelMapper")
    private ModelMapper skipModelMapper;

    @Mock
    private UserPersistence userPersistence;

    @Mock
    private StudyPersistence studyPersistence;

    @InjectMocks
    @MockBean
    private StudyServiceImpl studyService;

    @DisplayName(value = "01. create study service test - with User")
    @Test
    void createStudyMockTests() {
        StudyDomain testStudy = StudyDomain.builder()
                .allowMemberNumber(0)
                .name("testStudy")
                .organizer("tester@co.kr")
                .path("testing/path")
                .description("testing study")
                .shortDescription("testing study")
                .build();
        when(studyPersistence.save(testStudy)).thenReturn(settingIdx(testStudy));
        StudyDomain study = studyService.createStudy(testStudy);
        assertThat(testStudy.getName()).isEqualTo(study.getName());
    }

    @DisplayName(value = "02. list study service Tests")
    @Test
    void listStudyMockTests() {
        String organizer = "tester@co.kr";
        int totalNumber = 10;
        PageDto page = new PageDto();
        Pageable pageable = page.makePageable(0, "idx");
        Page listPage = pagingStudy(organizer, totalNumber, pageable);
        when(studyPersistence.pagingStudy(any(), any(), any())).thenReturn(listPage);
        Page pagingResult = studyService.pagingStudy(page);
        // total size setting
        assertThat(pagingResult.getTotalElements()).isEqualTo(totalNumber);
        // sort paging test
        assertThat(pageable.getSort()).isEqualTo(pagingResult.getSort());
        // paging content
        assertThat(pagingResult.getContent()).isEqualTo(listPage.getContent());

    }

    @DisplayName(value = "03. find study service Tests")
    @Test
    void findStudyWithPathMockTests() {
        String path = "/test/study";
        String description = "testingStudy";
        String studyName = "study";
        String organizer = "tester@co.kr";
        // make study
        StudyDomain studyDomain = studyFactory.makeDefaultStudyInfinityAllowUser(organizer, studyName, description, path);
        // mock study
        when(studyPersistence.findByPath(path)).thenReturn(Optional.of(studyDomain));
        StudyDomain findStudy = studyService.findStudyWithPath(path);
        // assert
        assertThat(path).isEqualTo(findStudy.getPath());
        assertThat(organizer).isEqualTo(findStudy.getOrganizer());
        assertThat(studyName).isEqualTo(findStudy.getName());


    }

    private StudyDomain settingIdx(StudyDomain studyDomain) {
        studyDomain.setIdx(1L);
        return studyDomain;
    }

    // mock paging result
    private Page pagingStudy(String organizer, int number, Pageable pageable) {
        List<StudyDomain> mockStudy = studyFactory.makeMockListStudy(organizer, number);
        return new PageImpl(mockStudy, pageable, 10);
    }
}