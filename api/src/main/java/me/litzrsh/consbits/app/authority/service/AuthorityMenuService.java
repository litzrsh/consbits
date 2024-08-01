package me.litzrsh.consbits.app.authority.service;

import me.litzrsh.consbits.app.authority.Authority;
import me.litzrsh.consbits.app.authority.AuthorityMenu;
import me.litzrsh.consbits.app.authority.dto.MenuDTO;
import me.litzrsh.consbits.app.authority.event.AuthoritySingleDeleteEvent;
import me.litzrsh.consbits.app.authority.event.AuthoritySingleGetEvent;
import me.litzrsh.consbits.app.authority.event.AuthoritySingleSaveEvent;
import me.litzrsh.consbits.app.authority.repository.AuthorityMenuRepository;
import me.litzrsh.consbits.app.menu.Menu;
import me.litzrsh.consbits.app.menu.event.MenuSingleDeleteEvent;
import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.core.event.SessionEvent;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 권한 메뉴 관리를 위한 서비스 클래스.
 * 권한과 메뉴의 관계를 관리하며, 다양한 이벤트를 처리합니다.
 */
@Service
@RequiredArgsConstructor
public class AuthorityMenuService {

    public static final String SESSION_MENU = "SYSTEM_MENU";

    private final AuthorityMenuRepository authorityMenuRepository;

    /**
     * 세션 이벤트를 처리하여 사용자 세션에 메뉴 정보를 설정합니다.
     *
     * @param e 세션 이벤트 객체.
     */
    @EventListener({SessionEvent.class})
    public void sessionEventHandler(SessionEvent e) {
        User user = e.getUserDetails();
        HttpSession session = e.getSession();
        // 사용자의 세션에 메뉴 정보를 설정합니다.
        List<MenuDTO> menus = authorityMenuRepository.findSessionMenus(user.getId());
        session.setAttribute(SESSION_MENU, menus);
    }

    /**
     * 권한 조회 이벤트를 처리하여 권한에 해당하는 메뉴 정보를 설정합니다.
     *
     * @param event 애플리케이션 이벤트 객체.
     */
    @EventListener({AuthoritySingleGetEvent.class})
    public void getEventHandler(ApplicationEvent event) {
        if (event instanceof AuthoritySingleGetEvent e) {
            Authority authority = e.getEntity();
            // 권한에 해당하는 메뉴 정보를 설정합니다.
            authority.setMenus(authorityMenuRepository.findByAuthority(authority));
        }
    }

    /**
     * 권한 저장 이벤트를 처리하여 권한에 해당하는 메뉴 정보를 저장합니다.
     *
     * @param event 애플리케이션 이벤트 객체.
     */
    @Transactional
    @EventListener({AuthoritySingleSaveEvent.class})
    public void saveEventHandler(ApplicationEvent event) {
        if (event instanceof AuthoritySingleSaveEvent e) {
            Authority authority = e.getEntity();
            // 기존의 권한-메뉴 관계를 모두 삭제합니다.
            authorityMenuRepository.deleteAllByAuthorityId(authority.getId());
            if (CollectionUtils.isEmpty(authority.getMenus())) return;
            // 새로운 권한-메뉴 관계를 생성하여 저장합니다.
            List<AuthorityMenu> authorityMenus = authority.getMenus().stream()
                    .map(m -> new AuthorityMenu(authority.getId(), m.getId(), m.getAuthorityValue()))
                    .toList();
            authorityMenuRepository.saveAll(authorityMenus);
        }
    }

    /**
     * 권한 삭제 및 메뉴 삭제 이벤트를 처리하여 해당하는 권한-메뉴 관계를 삭제합니다.
     *
     * @param event 애플리케이션 이벤트 객체.
     */
    @Transactional
    @EventListener({AuthoritySingleDeleteEvent.class, MenuSingleDeleteEvent.class})
    public void deleteEventHandler(ApplicationEvent event) {
        if (event instanceof AuthoritySingleDeleteEvent e) {
            Authority authority = e.getEntity();
            // 권한에 해당하는 모든 권한-메뉴 관계를 삭제합니다.
            authorityMenuRepository.deleteAllByAuthorityId(authority.getId());
        } else if (event instanceof MenuSingleDeleteEvent e) {
            Menu menu = e.getEntity();
            // 메뉴에 해당하는 모든 권한-메뉴 관계를 삭제합니다.
            authorityMenuRepository.deleteAllByMenuId(menu.getId());
        }
    }
}
