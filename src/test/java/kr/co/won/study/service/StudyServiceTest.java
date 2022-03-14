package kr.co.won.study.service;

import kr.co.won.config.TestAppConfiguration;
import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.factory.StudyDomainBuilderFactory;
import kr.co.won.study.persistence.StudyPersistence;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.factory.UserDomainBuilderFactory;
import kr.co.won.user.factory.UserFactory;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@Slf4j
@Import(value = {TestAppConfiguration.class, StudyDomainBuilderFactory.class, UserDomainBuilderFactory.class})
@ExtendWith(value = {MockitoExtension.class})
class StudyServiceTest {

    private TestAppConfiguration configuration = new TestAppConfiguration();

    private StudyDomainBuilderFactory studyFactory = new StudyDomainBuilderFactory();

    @Spy
    @Resource(name = "skipModelMapper")
    private ModelMapper modelMapper = configuration.modelMapper();

    @Spy
    @Resource(name = "notSkipModelMapper")
    private ModelMapper skipModelMapper = configuration.notSkipModelMapper();

    @Mock
    private UserPersistence userPersistence;

    @Mock
    private StudyPersistence studyPersistence;


    private UserDomainBuilderFactory userFactory = new UserDomainBuilderFactory();

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

    @DisplayName(value = "04. update study service Tests")
    @Test
    void updateStudyWithPathMockTests() {
        String path = "test/study";
        String description = "testingStudy";
        String studyName = "study";
        String organizer = "tester@co.kr";
        // make study
        StudyDomain studyDomain = studyFactory.makeDefaultStudyInfinityAllowUser(organizer, studyName, description, path);
        // study update
        String updateStudyName = "studyTesting";
        StudyDomain updateStudy = StudyDomain.builder()
                .name(updateStudyName)
                .build();
        // user mock factory
        UserDomain testUser = userFactory.makeDomainUser(1L, "tester", organizer, "1234", UserRoleType.USER);
        // mock study
        when(studyPersistence.findByPath(path)).thenReturn(Optional.of(studyDomain));
        StudyDomain updateStudyDone = studyService.updateStudy(path, updateStudy, testUser);
        assertThat(updateStudyDone.getName()).isEqualTo(updateStudyName);
        assertThat(updateStudyDone.getPath()).isEqualTo(path);
        assertThat(updateStudyDone.getOrganizer()).isEqualTo(testUser.getEmail());
    }

    @DisplayName(value = "05. delete study Service Tests")
    @Test
    void deleteStudyMockTests() {
        String path = "test/study";
        String description = "testingStudy";
        String studyName = "study";
        String organizer = "tester@co.kr";
        // make study
        StudyDomain studyDomain = studyFactory.makeDefaultStudyInfinityAllowUser(organizer, studyName, description, path);
        // user mock factory
        UserDomain testUser = userFactory.makeDomainUser(1L, "tester", organizer, "1234", UserRoleType.USER);
        // mock study
        when(studyPersistence.findByPath(path)).thenReturn(Optional.of(studyDomain));
        // study delete function
        StudyDomain deleteStudy = studyService.deleteStudy(path, testUser);
        // delete flag set true
        assertThat(deleteStudy.isDeleted()).isTrue();
    }

    @DisplayName(value = "05. delete study list Service Tests")
    @Test
    void deleteStudiesMockTests() {
        String organizer = "tester@co.kr";
        List<StudyDomain> studies = studyFactory.makeMockListStudy(organizer, 10);
        // get path
        List<String> paths = studies.stream().map(StudyDomain::getPath).collect(Collectors.toList());
        // user mock factory
        UserDomain testUser = userFactory.makeDomainUser(1L, "tester", organizer, "1234", UserRoleType.USER);
        // find study domain
        when((studyPersistence.findByPathIn(paths))).thenReturn(studies);
        List<StudyDomain> studyDomains = studyService.deleteStudyBulkWithPaths(paths, testUser);
        // delete study list
        // delete flag set true
        studyDomains.forEach(study -> assertThat(study.isDeleted()).isTrue());
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