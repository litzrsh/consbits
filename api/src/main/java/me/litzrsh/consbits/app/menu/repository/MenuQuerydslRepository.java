package me.litzrsh.consbits.app.menu.repository;

import me.litzrsh.consbits.app.menu.Menu;

/**
 * QueryDSL을 사용한 메뉴 데이터의 커스텀 쿼리를 위한 저장소 인터페이스.
 * 이 인터페이스를 통해 JPA 저장소에서 제공하지 않는 복잡한 쿼리를 정의할 수 있습니다.
 */
public interface MenuQuerydslRepository {

    /**
     * 주어진 메뉴와 하위 메뉴를 삭제합니다.
     *
     * @param menu 삭제할 메뉴 객체.
     */
    void deleteWithChild(Menu menu);
}
