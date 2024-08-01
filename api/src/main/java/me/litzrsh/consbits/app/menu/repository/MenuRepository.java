package me.litzrsh.consbits.app.menu.repository;

import me.litzrsh.consbits.app.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 메뉴 데이터를 관리하는 저장소 인터페이스.
 * JPA를 통해 메뉴 데이터를 저장하고 조회하는 기능을 제공합니다.
 */
@Repository
public interface MenuRepository extends MenuQuerydslRepository, JpaRepository<Menu, String> {
}
