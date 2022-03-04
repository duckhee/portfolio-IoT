package kr.co.won.study.service;

import kr.co.won.auth.AuthUser;
import kr.co.won.config.TestAppConfiguration;
import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.persistence.StudyPersistence;
import kr.co.won.user.persistence.UserPersistence;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.hibernate.annotations.DiscriminatorFormula;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@Slf4j
@Import(value = {TestAppConfiguration.class})
@ExtendWith(value = {MockitoExtension.class})
class StudyServiceTest {

    @Autowired
    private ModelMapper modelMapper;

    @Mock
    private UserPersistence userPersistence;

    @Mock
    private StudyPersistence studyPersistence;

    @InjectMocks
    @MockBean
    private StudyServiceImpl studyService;

    @DisplayName(value = "01. create study service test - with User")
    @Test
    void createStudyMockTests() throws Exception {
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

    private StudyDomain settingIdx(StudyDomain studyDomain) {
        studyDomain.setIdx(1L);
        return studyDomain;
    }
}