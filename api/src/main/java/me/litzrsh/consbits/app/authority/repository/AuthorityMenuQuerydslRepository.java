package me.litzrsh.consbits.app.authority.repository;

import me.litzrsh.consbits.app.authority.Authority;
import me.litzrsh.consbits.app.authority.dto.MenuDTO;

import java.util.List;

/**
 * QueryDSL을 사용한 권한 메뉴 데이터의 커스텀 쿼리를 위한 저장소 인터페이스.
 * 이 인터페이스를 통해 JPA 저장소에서 제공하지 않는 복잡한 쿼리를 정의할 수 있습니다.
 */
public interface AuthorityMenuQuerydslRepository {

    /**
     * 특정 사용자의 세션 메뉴를 조회합니다.
     *
     * @param username 조회할 사용자의 이름.
     * @return 세션 메뉴 리스트.
     */
    List<MenuDTO> findSessionMenus(String username);

    /**
     * 특정 권한에 해당하는 메뉴를 조회합니다.
     *
     * @param authority 조회할 권한.
     * @return 권한에 해당하는 메뉴 리스트.
     */
    List<MenuDTO> findByAuthority(Authority authority);
}
