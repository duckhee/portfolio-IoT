package kr.co.won.study.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StudyDomainTest {


    @DisplayName(value = "01. study create builder Tests")
    @Test
    void createStudyDomainBuilderTests() {
        String name = "test";
        String shortDescription = "testing";
        String organizer = "tester@co.kr";
        StudyDomain buildTestStudy = StudyDomain.builder()
                .arrowMemberNumber(0)
                .name(name)
                .shortDescription(shortDescription)
                .description("dafdasfead")
                .path("/study/testing")
                .organizer(organizer)
                .build();
        assertThat(buildTestStudy.isDeleted()).isNotNull();
        assertThat(buildTestStudy.isDeleted()).isEqualTo(false);
        assertThat(buildTestStudy.getName()).isEqualTo(name);
        assertThat(buildTestStudy.getShortDescription()).isEqualTo(shortDescription);
        assertThat(buildTestStudy.getOrganizer()).isEqualTo(organizer);
        assertThat(buildTestStudy.isClosed()).isEqualTo(false);
        assertThat(buildTestStudy.isPublished()).isEqualTo(false);
        assertThat(buildTestStudy.isRecruiting()).isEqualTo(false);
    }

    @DisplayName(value = "02. study joinFunction Tests")
    @Test
    void joinStudyDomainFunctionTests() {
        String name = "test";
        String shortDescription = "testing";
        String organizer = "tester@co.kr";
        StudyDomain buildTestStudy = StudyDomain.builder()
                .arrowMemberNumber(0)
                .name(name)
                .shortDescription(shortDescription)
                .description("dafdasfead")
                .path("/study/testing")
                .organizer(organizer)
                .recruiting(true)
                .build();

        assertThat(buildTestStudy.isJoined()).isEqualTo(true);
        // study member join one
        buildTestStudy.setArrowMemberNumber(1);
        assertThat(buildTestStudy.isJoined()).isEqualTo(true);
        // study member count is one
        buildTestStudy.setMemberCount(1);
        assertThat(buildTestStudy.isJoined()).isEqualTo(false);
    }

    @DisplayName(value = "03. study publishing Test")
    @Test
    void publishedStudyDomainFunctionTests() {

    }

}