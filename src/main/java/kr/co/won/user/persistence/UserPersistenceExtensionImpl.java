package kr.co.won.user.persistence;

import com.querydsl.jpa.JPQLQuery;
import kr.co.won.user.domain.QUserDomain;
import kr.co.won.user.domain.QUserRoleDomain;
import kr.co.won.user.domain.UserDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.co.won.user.domain.QUserDomain.userDomain;
import static kr.co.won.user.domain.QUserRoleDomain.userRoleDomain;

@Transactional(readOnly = true)
public class UserPersistenceExtensionImpl extends QuerydslRepositorySupport implements UserPersistenceExtension {

    public UserPersistenceExtensionImpl() {
        super(UserDomain.class);
    }

    @Override
    public Page pagingUser(String type, String keyword, Pageable pageable) {
        QUserDomain user = userDomain;
        QUserRoleDomain role = userRoleDomain;
        JPQLQuery<UserDomain> defaultQuery = from(user)
                .select(user)
                .distinct()
                .innerJoin(user.roles, role)
                .fetchJoin()
                .where(user.idx.gt(0L));

        /** search */
        if (type != null) {

        }

        long totalNumber = defaultQuery.fetchCount();
        List<UserDomain> result = defaultQuery.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(user.createdAt.desc())
                .fetch();

        return new PageImpl(result, pageable, totalNumber);
    }
}
