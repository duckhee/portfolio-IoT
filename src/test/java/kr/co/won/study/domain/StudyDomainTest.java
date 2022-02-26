package kr.co.won.study.domain;

import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.factory.UserDomainBuilderFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

class StudyDomainTest {


    @DisplayName(value = "01. study create builder Tests")
    @Test
    void createStudyDomainBuilderTests() {
        String name = "test";
        String shortDescription = "testing";
        String organizer = "tester@co.kr";
        StudyDomain buildTestStudy = StudyDomain.builder()
                .allowMemberNumber(0)
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
                .allowMemberNumber(0)
                .name(name)
                .shortDescription(shortDescription)
                .description("dafdasfead")
                .path("/study/testing")
                .organizer(organizer)
                .recruiting(true)
                .build();

        assertThat(buildTestStudy.isJoined()).isEqualTo(true);
        // study member join one
        buildTestStudy.setAllowMemberNumber(1);
        assertThat(buildTestStudy.isJoined()).isEqualTo(true);
        // study member count is one
        buildTestStudy.setMemberCount(1);
        assertThat(buildTestStudy.isJoined()).isEqualTo(false);
    }

    @DisplayName(value = "03. study Status Test")
    @Test
    void statusStudyDomainFunctionTests() {
        String name = "test";
        String shortDescription = "testing";
        String organizer = "tester@co.kr";
        StudyDomain buildTestStudy = StudyDomain.builder()
                .allowMemberNumber(0)
                .name(name)
                .shortDescription(shortDescription)
                .description("dafdasfead")
                .path("/study/testing")
                .organizer(organizer)
                .recruiting(true)
                .build();

        assertThat(buildTestStudy.getName()).isEqualTo(name);
        assertThat(buildTestStudy.getOrganizer()).isEqualTo(organizer);
        assertThat(buildTestStudy.getShortDescription()).isEqualTo(shortDescription);
        // study publish tests
        buildTestStudy.studyPublishing();
        assertThat(buildTestStudy.isPublished()).isTrue();
        assertThat(buildTestStudy.isRecruiting()).isFalse();
        assertThat(buildTestStudy.isClosed()).isFalse();
        assertThat(buildTestStudy.studyStatus()).isEqualTo(StudyStatusType.PUBLISHED);
        // study close tests
        buildTestStudy.studyClosing();
        assertThat(buildTestStudy.isPublished()).isFalse();
        assertThat(buildTestStudy.isRecruiting()).isFalse();
        assertThat(buildTestStudy.isClosed()).isTrue();
        assertThat(buildTestStudy.studyStatus()).isEqualTo(StudyStatusType.CLOSE);
        // study recruiting tests
        buildTestStudy.studyRecruiting();
        assertThat(buildTestStudy.isPublished()).isFalse();
        assertThat(buildTestStudy.isRecruiting()).isTrue();
        assertThat(buildTestStudy.isClosed()).isFalse();
        assertThat(buildTestStudy.studyStatus()).isEqualTo(StudyStatusType.RECRUIT);
    }

    @DisplayName(value = "04. study member logic Tests")
    @Test
    void allowMemberCheckLogicTests() {
        int userNumber = 10;
        String name = "test";
        String shortDescription = "testing";
        String organizer = "tester@co.kr";
        // user make factory
        UserDomainBuilderFactory userDomainBuilderFactory = new UserDomainBuilderFactory();
        // test user makes
        List<UserDomain> testUsers = userDomainBuilderFactory.makeDomainBulkUser("test", userNumber);
        // make study
        StudyDomain buildTestStudy = StudyDomain.builder()
                .allowMemberNumber(2)
                .name(name)
                .shortDescription(shortDescription)
                .description("dafdasfead")
                .path("/study/testing")
                .organizer(organizer)
                .recruiting(true)
                .build();
        // allow member test before
        assertThat(testUsers.size()).isEqualTo(userNumber);
        assertThat(buildTestStudy.getName()).isEqualTo(name);
        assertThat(buildTestStudy.getOrganizer()).isEqualTo(organizer);
        assertThat(buildTestStudy.getShortDescription()).isEqualTo(shortDescription);
        // join study before member count
        int before = buildTestStudy.getMemberCount();
        // member add test
        List<StudyMemberDomain> collectStudyMembers = testUsers.stream().map(userDomain ->
                        StudyMemberDomain.builder().user(userDomain).build())
                .collect(Collectors.toList());

        for (int i = 0; i < collectStudyMembers.size(); i++) {
            collectStudyMembers.get(i).setIdx(Long.valueOf(i + 1));
        }
        // study join member
        buildTestStudy.joinStudyOverFailed(collectStudyMembers.get(0), collectStudyMembers.get(1), collectStudyMembers.get(2));
        assertThat(buildTestStudy.isJoinMember(collectStudyMembers.get(0))).isFalse();
        assertThat(buildTestStudy.isJoinMember(collectStudyMembers.get(2))).isFalse();
        assertThat(buildTestStudy.isJoinMember(collectStudyMembers.get(2).getUser())).isFalse();
        assertThat(buildTestStudy.isJoinMember(collectStudyMembers.get(2).getUser().getEmail())).isFalse();
        assertThat(buildTestStudy.getMemberCount()).isEqualTo(before);

    }

    @DisplayName(value = "05. study member logic Tests")
    @Test
    void allowMemberJoinMemberTests() {
        int userNumber = 10;
        String name = "test";
        String shortDescription = "testing";
        String organizer = "tester@co.kr";
        // user make factory
        UserDomainBuilderFactory userDomainBuilderFactory = new UserDomainBuilderFactory();
        // test user makes
        List<UserDomain> testUsers = userDomainBuilderFactory.makeDomainBulkUser("test", userNumber);
        // make study
        StudyDomain buildTestStudy = StudyDomain.builder()
                .allowMemberNumber(2)
                .name(name)
                .shortDescription(shortDescription)
                .description("dafdasfead")
                .path("/study/testing")
                .organizer(organizer)
                .recruiting(true)
                .build();
        // allow member test before
        assertThat(testUsers.size()).isEqualTo(userNumber);
        assertThat(buildTestStudy.getName()).isEqualTo(name);
        assertThat(buildTestStudy.getOrganizer()).isEqualTo(organizer);
        assertThat(buildTestStudy.getShortDescription()).isEqualTo(shortDescription);
        // join study before member
        int memberCount = buildTestStudy.getMemberCount();
        // member add test
        List<StudyMemberDomain> collectStudyMembers = testUsers.stream().map(userDomain ->
                        StudyMemberDomain.builder().user(userDomain).build())
                .collect(Collectors.toList());

        for (int i = 0; i < collectStudyMembers.size(); i++) {
            collectStudyMembers.get(i).setIdx(Long.valueOf(i + 1));
        }
        // study join member
        buildTestStudy.joinStudy(collectStudyMembers.get(0), collectStudyMembers.get(1), collectStudyMembers.get(2));
        assertThat(buildTestStudy.isJoinMember(collectStudyMembers.get(0))).isTrue();
        assertThat(buildTestStudy.isJoinMember(collectStudyMembers.get(2))).isFalse();
        assertThat(buildTestStudy.isJoinMember(collectStudyMembers.get(2).getUser())).isFalse();
        assertThat(buildTestStudy.isJoinMember(collectStudyMembers.get(2).getUser().getEmail())).isFalse();
        // member count
        assertThat(buildTestStudy.getMemberCount()).isEqualTo(2);
        assertThat(buildTestStudy.getMemberCount()).isEqualTo(buildTestStudy.getAllowMemberNumber());
    }

}