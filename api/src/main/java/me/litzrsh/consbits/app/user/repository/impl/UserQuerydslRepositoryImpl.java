package me.litzrsh.consbits.app.user.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;
import me.litzrsh.consbits.app.user.QUser;
import me.litzrsh.consbits.app.user.QUserAuthority;
import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.app.user.UserSearchParams;
import me.litzrsh.consbits.app.user.repository.UserQuerydslRepository;
import me.litzrsh.consbits.core.Page;
import me.litzrsh.consbits.core.QuerydslSearchHelper;
import me.litzrsh.consbits.core.util.SessionUtils;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserQuerydslRepositoryImpl extends QuerydslRepositorySupport implements UserQuerydslRepository, QuerydslSearchHelper {

    public UserQuerydslRepositoryImpl() {
        super(User.class);
    }

    @Override
    public Page<User> findByPages(UserSearchParams params) {
        QUser u = QUser.user;

        BooleanExpression e = null;
        if (StringUtils.hasText(params.getType())) {
            e = and(e, u.type.eq(params.getType()));
        }
        if (StringUtils.hasText(params.getStatus())) {
            e = and(e, u.status.eq(params.getStatus()));
        }
        if (StringUtils.hasText(params.getQuery())) {
            String query = "%" + params.getQuery() + "%";
            e = and(e,
                    u.loginId.like(query)
                            .or(u.name.like(query))
                            .or(u.email.like(query))
                            .or(u.mobile.like(query))
            );
        }
        if (!SessionUtils.isSysAdmin()) {
            QUserAuthority ua = QUserAuthority.userAuthority;
            e = and(e, u.id.in(
                    from(ua)
                            .select(ua.userId)
                            .where(ua.authorityId.in(SessionUtils.getAuthorityIds()))
            ));
        }

        JPQLQuery<User> query = from(u)
                .select(u)
                .where(e);

        final List<User> contents = query
                .limit(params.getPerPage())
                .offset(params.getOffset())
                .orderBy(u.name.asc())
                .fetch();
        final long totalItems = query.fetchCount();

        return new Page<>(contents, params, totalItems);
    }

    @Override
    public Optional<String> getUsername(String id) {
        QUser u = QUser.user;
        return Optional.ofNullable(from(u)
                .select(u.name)
                .where(u.id.eq(id))
                .fetchFirst());
    }

    @Override
    public void addLoginFailCount(String id) {
        QUser u = QUser.user;
        JPAUpdateClause update = new JPAUpdateClause(Objects.requireNonNull(getEntityManager()), u);
        update
                .set(u.loginFailCount, u.loginFailCount.add(1))
                .where(u.id.eq(id))
                .execute();
    }

    @Override
    public void processAuthenticationSuccess(String id) {
        QUser u = QUser.user;
        JPAUpdateClause update = new JPAUpdateClause(Objects.requireNonNull(getEntityManager()), u);
        update
                .set(u.lastLoginAt, new Date())
                .set(u.loginFailCount, 0)
                .where(u.id.eq(id))
                .execute();
    }
}
