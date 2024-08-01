package me.litzrsh.consbits.app.authority.repository.impl;

import com.querydsl.core.Tuple;
import me.litzrsh.consbits.app.authority.Authority;
import me.litzrsh.consbits.app.authority.AuthorityMenu;
import me.litzrsh.consbits.app.authority.QAuthority;
import me.litzrsh.consbits.app.authority.QAuthorityMenu;
import me.litzrsh.consbits.app.authority.dto.MenuDTO;
import me.litzrsh.consbits.app.authority.repository.AuthorityMenuQuerydslRepository;
import me.litzrsh.consbits.app.menu.Menu;
import me.litzrsh.consbits.app.menu.QMenu;
import me.litzrsh.consbits.app.user.QUserAuthority;
import me.litzrsh.consbits.core.util.TreeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

/**
 * QueryDSL을 사용한 권한 메뉴 데이터의 커스텀 쿼리를 구현한 클래스.
 * QuerydslRepositorySupport를 상속받아 복잡한 쿼리 메서드를 정의합니다.
 */
public class AuthorityMenuQuerydslRepositoryImpl extends QuerydslRepositorySupport implements AuthorityMenuQuerydslRepository {

    /**
     * AuthorityMenuQuerydslRepositoryImpl 생성자.
     * AuthorityMenu 엔티티를 위한 QuerydslRepositorySupport 초기화.
     */
    public AuthorityMenuQuerydslRepositoryImpl() {
        super(AuthorityMenu.class);
    }

    /**
     * 특정 사용자의 세션 메뉴를 조회합니다.
     *
     * @param username 조회할 사용자의 이름.
     * @return 세션 메뉴 리스트.
     */
    @Override
    public List<MenuDTO> findSessionMenus(String username) {
        QUserAuthority ua = QUserAuthority.userAuthority;
        QAuthorityMenu am = QAuthorityMenu.authorityMenu;
        QAuthority a = QAuthority.authority;
        QMenu m = QMenu.menu;

        List<Tuple> tuples = from(m)
                .select(m, am.authorityValue)
                .innerJoin(am).on(am.menuId.eq(m.id))
                .innerJoin(a).on(a.id.eq(am.authorityId).and(a.use.isTrue()))
                .innerJoin(ua).on(ua.authorityId.eq(a.id).and(ua.userId.eq(username)))
                .where(m.use.isTrue())
                .distinct()
                .fetch();

        return TreeUtils.convert(convert(tuples));
    }

    /**
     * 특정 권한에 해당하는 메뉴를 조회합니다.
     *
     * @param authority 조회할 권한.
     * @return 권한에 해당하는 메뉴 리스트.
     */
    @Override
    public List<MenuDTO> findByAuthority(Authority authority) {
        QAuthorityMenu am = QAuthorityMenu.authorityMenu;
        QMenu m = QMenu.menu;

        List<Tuple> tuples = from(am)
                .select(m, am.authorityValue)
                .innerJoin(m).on(am.menuId.eq(m.id))
                .where(am.authorityId.eq(authority.getId()))
                .fetch();

        return convert(tuples);
    }

    /**
     * Tuple 리스트를 MenuDTO 리스트로 변환합니다.
     *
     * @param tuples 변환할 Tuple 리스트.
     * @return 변환된 MenuDTO 리스트.
     */
    protected final List<MenuDTO> convert(List<Tuple> tuples) {
        return tuples.stream()
                .map(t -> {
                    MenuDTO dto = new MenuDTO();
                    Menu menu = t.get(0, Menu.class);
                    Integer authorityValue = t.get(1, Integer.class);
                    //noinspection DataFlowIssue
                    BeanUtils.copyProperties(menu, dto);
                    dto.setAuthorityValue(authorityValue);
                    return dto;
                })
                .toList();
    }
}
