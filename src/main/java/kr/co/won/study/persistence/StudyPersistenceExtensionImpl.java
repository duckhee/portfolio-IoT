package kr.co.won.study.persistence;

import com.querydsl.jpa.JPQLQuery;
import kr.co.won.study.domain.QStudyDomain;
import kr.co.won.study.domain.StudyDomain;
import kr.co.won.study.dto.QStudyListQueryDto;
import kr.co.won.study.dto.StudyListQueryDto;
import kr.co.won.user.domain.QUserDomain;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.SuppressLoggerChecks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.co.won.study.domain.QStudyDomain.studyDomain;
import static kr.co.won.user.domain.QUserDomain.userDomain;

@Slf4j
@Transactional(readOnly = true)
public class StudyPersistenceExtensionImpl extends QuerydslRepositorySupport implements StudyPersistenceExtension {

    public StudyPersistenceExtensionImpl() {
        super(StudyDomain.class);
    }


    @Override
    public Page pagingStudy(String type, String keyword, Pageable pageable) {
        log.info("paging ::: type : {}, keyword : {}", type, keyword);
        QStudyDomain study = studyDomain;
        QUserDomain user = userDomain;
        JPQLQuery<StudyListQueryDto> defaultQuery = from(study)
                .select(
                        new QStudyListQueryDto(
                                study.idx, study.name, study.shortDescription, study.organizer,
                                study.path, study.allowMemberNumber, study.memberCount, study.closed, study.closedDateTime,
                                study.published, study.publishedDateTime, study.recruiting, study.recruitingEndDateTime, study.createdAt,
                                study.updatedAt
                        )
                ).where(study.idx.gt(0L).and(study.deleted.isFalse()));

        // study search option
        if (type != null) {
            switch (keyword) {

            }
        }
        long totalNumber = defaultQuery.fetchCount();
        // offset and limit setting
        JPQLQuery<StudyListQueryDto> query = defaultQuery.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(study.createdAt.desc());

        List<StudyListQueryDto> result = query.fetch();

        return new PageImpl(result, pageable, totalNumber);
    }

    @Override
    public Page pagingStudy(String type, String keyword, Pageable pageable, UserDomain loginUser) {
        QStudyDomain study = studyDomain;
        QUserDomain user = userDomain;

        // study search option
        if (type != null) {
            switch (keyword) {

            }
        }

        return null;
    }
}
