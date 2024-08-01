package me.litzrsh.consbits.app.menu.repository.impl;

import com.querydsl.jpa.impl.JPADeleteClause;
import me.litzrsh.consbits.app.menu.Menu;
import me.litzrsh.consbits.app.menu.QMenu;
import me.litzrsh.consbits.app.menu.repository.MenuQuerydslRepository;
import me.litzrsh.consbits.core.util.RestUtils;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Objects;

/**
 * QueryDSL을 사용하여 메뉴 데이터의 커스텀 쿼리를 구현한 클래스.
 * QuerydslRepositorySupport를 상속받아 복잡한 쿼리 메서드를 정의합니다.
 */
public class MenuQuerydslRepositoryImpl extends QuerydslRepositorySupport implements MenuQuerydslRepository {

    /**
     * MenuQuerydslRepositoryImpl 생성자.
     * Menu 엔티티를 위한 QuerydslRepositorySupport 초기화.
     */
    public MenuQuerydslRepositoryImpl() {
        super(Menu.class);
    }

    /**
     * 주어진 메뉴와 하위 메뉴를 삭제합니다.
     *
     * @param menu 삭제할 메뉴 객체.
     */
    @Override
    public void deleteWithChild(Menu menu) {
        QMenu m = QMenu.menu;
        String paths = RestUtils.append(menu.getPath(), menu.getId()) + "%";
        JPADeleteClause delete = new JPADeleteClause(Objects.requireNonNull(getEntityManager()), m);
        delete
                .where(m.id.eq(menu.getId()).or(m.path.like(paths)))
                .execute();
    }
}
