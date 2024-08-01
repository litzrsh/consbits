package me.litzrsh.consbits.app.authority.repository;

import me.litzrsh.consbits.app.authority.AuthorityMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 권한 메뉴 데이터를 관리하는 저장소 인터페이스.
 * JPA를 통해 권한 메뉴 데이터를 저장하고 조회하는 기능을 제공합니다.
 */
@Repository
public interface AuthorityMenuRepository extends AuthorityMenuQuerydslRepository, JpaRepository<AuthorityMenu, AuthorityMenu.Key> {

    /**
     * 특정 권한 ID에 해당하는 모든 권한 메뉴를 삭제합니다.
     *
     * @param authorityId 삭제할 권한의 ID.
     */
    void deleteAllByAuthorityId(String authorityId);

    /**
     * 특정 메뉴 ID에 해당하는 모든 권한 메뉴를 삭제합니다.
     *
     * @param menuId 삭제할 메뉴의 ID.
     */
    void deleteAllByMenuId(String menuId);
}
