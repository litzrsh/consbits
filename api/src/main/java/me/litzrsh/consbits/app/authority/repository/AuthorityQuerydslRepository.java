package me.litzrsh.consbits.app.authority.repository;

import me.litzrsh.consbits.app.authority.Authority;

import java.util.List;

/**
 * QueryDSL을 사용한 권한 데이터의 커스텀 쿼리를 위한 저장소 인터페이스.
 * 이 인터페이스를 통해 JPA 저장소에서 제공하지 않는 복잡한 쿼리를 정의할 수 있습니다.
 */
public interface AuthorityQuerydslRepository {

    /**
     * 모든 권한을 조회합니다.
     *
     * @return 권한 리스트.
     */
    List<Authority> findAuthorities();
}
