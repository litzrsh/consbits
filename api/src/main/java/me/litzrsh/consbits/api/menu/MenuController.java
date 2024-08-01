package me.litzrsh.consbits.api.menu;

import me.litzrsh.consbits.app.authority.dto.MenuDTO;
import me.litzrsh.consbits.app.authority.service.AuthorityMenuService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 메뉴 관리를 위한 REST API 컨트롤러 클래스.
 * 세션에 저장된 메뉴 정보를 제공합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0/menu")
public class MenuController {

    /**
     * 세션에 저장된 메뉴 리스트를 조회합니다.
     *
     * @param session 현재 HTTP 세션.
     * @return 세션에 저장된 메뉴 리스트. 메뉴가 없으면 빈 리스트를 반환합니다.
     */
    @GetMapping("")
    public List<MenuDTO> getMenus(HttpSession session) {
        // 세션에서 메뉴 정보를 가져옵니다.
        Object value = session.getAttribute(AuthorityMenuService.SESSION_MENU);
        // 세션에 메뉴 정보가 없으면 빈 리스트를 반환하고, 있으면 해당 리스트를 반환합니다.
        //noinspection unchecked
        return value == null ? new ArrayList<>() : (List<MenuDTO>) value;
    }
}
