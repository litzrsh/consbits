package me.litzrsh.consbits.app.authority.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import me.litzrsh.consbits.app.authority.Authority;
import me.litzrsh.consbits.app.authority.QAuthority;
import me.litzrsh.consbits.app.authority.repository.AuthorityQuerydslRepository;
import me.litzrsh.consbits.app.user.QUserAuthority;
import me.litzrsh.consbits.core.util.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * QueryDSL을 사용한 권한 데이터의 커스텀 쿼리를 구현한 클래스.
 * QuerydslRepositorySupport를 상속받아 복잡한 쿼리 메서드를 정의합니다.
 */
@Slf4j
public class AuthorityQuerydslRepositoryImpl extends QuerydslRepositorySupport implements AuthorityQuerydslRepository {

    /**
     * AuthorityQuerydslRepositoryImpl 생성자.
     * Authority 엔티티를 위한 QuerydslRepositorySupport 초기화.
     */
    public AuthorityQuerydslRepositoryImpl() {
        super(Authority.class);
    }

    /**
     * 모든 권한을 조회합니다.
     *
     * @return 권한 리스트.
     */
    @Override
    public List<Authority> findAuthorities() {
        QAuthority a = QAuthority.authority;
        QUserAuthority ua = QUserAuthority.userAuthority;
        BooleanExpression exp = null;

        // 시스템 관리자가 아닌 경우 사용자의 권한 ID에 해당하는 권한만 조회합니다.
        if (!SessionUtils.isSysAdmin()) {
            exp = a.id.in(SessionUtils.getAuthorityIds());
        }

        // 권한을 조회하여 리스트로 반환합니다.
        final List<Authority> authorities = from(a)
                .select(a)
                .where(exp)
                .orderBy(a.id.asc())
                .fetch();

        // 각 권한에 해당하는 사용자 수를 맵으로 수집합니다.
        @SuppressWarnings("DataFlowIssue")
        Map<String, Long> tupleMap = from(ua)
                .select(ua.authorityId, ua.count())
                .groupBy(ua.authorityId)
                .fetch()
                .stream()
                .collect(Collectors.toMap(e -> e.get(0, String.class), e -> e.get(1, Long.class)));

        // 각 권한에 사용자 수를 설정합니다.
        authorities.forEach(authority -> authority
                .setUserCount(tupleMap.getOrDefault(authority.getId(), 0L).intValue())
        );

        return authorities;
    }
}
