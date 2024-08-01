package me.litzrsh.consbits.app.user.repository.impl;

import com.querydsl.jpa.impl.JPADeleteClause;
import me.litzrsh.consbits.app.authority.Authority;
import me.litzrsh.consbits.app.authority.QAuthority;
import me.litzrsh.consbits.app.user.QUserAuthority;
import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.app.user.UserAuthority;
import me.litzrsh.consbits.app.user.repository.UserAuthorityQuerydslRepository;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

// UserAuthorityQuerydslRepository의 구현체
public class UserAuthorityQuerydslRepositoryImpl extends QuerydslRepositorySupport implements UserAuthorityQuerydslRepository {

    // 생성자: UserAuthority 엔티티 클래스를 전달
    public UserAuthorityQuerydslRepositoryImpl() {
        super(UserAuthority.class);
    }

    @Override
    public void setAllUserAuthorities(List<User> users) {
        QUserAuthority ua = QUserAuthority.userAuthority;
        QAuthority a = QAuthority.authority;

        // 전달받은 사용자 목록의 ID를 이용해 UserAuthority 목록을 조회
        List<UserAuthority> userAuthorities = from(ua)
                .select(ua)
                .where(ua.userId.in(users.stream().map(User::getId).distinct().toList()))
                .fetch();

        // 조회 결과가 비어있으면 함수 종료
        if (CollectionUtils.isEmpty(userAuthorities)) return;

        // UserAuthority에서 Authority ID를 추출해 Authority 맵 생성
        Map<String, Authority> authorityMap = from(a)
                .select(a)
                .where(a.id.in(userAuthorities.stream().map(UserAuthority::getAuthorityId).distinct().toList()))
                .fetch()
                .stream()
                .collect(Collectors.toMap(Authority::getId, value -> value));

        // 사용자에게 해당하는 권한을 설정
        users.forEach(user -> user.setAuthorities(userAuthorities.stream()
                .filter((e) -> e.getUserId().equals(user.getId()))
                .map(e -> authorityMap.get(e.getAuthorityId()))
                .filter(Objects::nonNull)
                .toList()));
    }

    @Override
    public List<Authority> findAllByUsername(String username) {
        QUserAuthority ua = QUserAuthority.userAuthority;
        QAuthority a = QAuthority.authority;

        // 주어진 사용자 이름으로 UserAuthority와 Authority를 조인하여 권한 목록 조회
        return from(ua)
                .select(a)
                .innerJoin(a).on(ua.authorityId.eq(a.id).and(a.use.isTrue()))
                .where(ua.userId.eq(username))
                .orderBy(a.id.asc())
                .fetch();
    }

    @Override
    public void deleteAllByUser(User user) {
        // 사용자 객체가 null인 경우 함수 종료
        if (user == null) return;
        QUserAuthority ua = QUserAuthority.userAuthority;

        // 사용자에 대한 모든 UserAuthority 삭제
        JPADeleteClause delete = new JPADeleteClause(Objects.requireNonNull(getEntityManager()), ua);
        delete
                .where(ua.userId.eq(user.getId()))
                .execute();
    }

    @Override
    public void deleteAllByUsers(List<User> users) {
        // 사용자 목록이 비어있는 경우 함수 종료
        if (CollectionUtils.isEmpty(users)) return;
        QUserAuthority ua = QUserAuthority.userAuthority;

        // 주어진 사용자 목록에 대한 모든 UserAuthority 삭제
        JPADeleteClause delete = new JPADeleteClause(Objects.requireNonNull(getEntityManager()), ua);
        delete
                .where(ua.userId.in(users.stream().map(User::getId).distinct().toList()))
                .execute();
    }

    @Override
    public void deleteAllByAuthority(Authority authority) {
        // 권한 객체가 null인 경우 함수 종료
        if (authority == null) return;
        QUserAuthority ua = QUserAuthority.userAuthority;

        // 주어진 권한에 대한 모든 UserAuthority 삭제
        JPADeleteClause delete = new JPADeleteClause(Objects.requireNonNull(getEntityManager()), ua);
        delete
                .where(ua.authorityId.eq(authority.getId()))
                .execute();
    }

    @Override
    public void deleteAllByAuthorities(List<Authority> authorities) {
        // 권한 목록이 비어있는 경우 함수 종료
        if (CollectionUtils.isEmpty(authorities)) return;
        QUserAuthority ua = QUserAuthority.userAuthority;

        // 주어진 권한 목록에 대한 모든 UserAuthority 삭제
        JPADeleteClause delete = new JPADeleteClause(Objects.requireNonNull(getEntityManager()), ua);
        delete
                .where(ua.authorityId.in(authorities.stream().map(Authority::getId).distinct().toList()))
                .execute();
    }
}
