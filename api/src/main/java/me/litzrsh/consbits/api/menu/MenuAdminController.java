package me.litzrsh.consbits.api.menu;

import me.litzrsh.consbits.app.menu.Menu;
import me.litzrsh.consbits.app.menu.service.MenuAdminService;
import me.litzrsh.consbits.core.exception.RestfulException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 메뉴 관리를 위한 REST API 컨트롤러 클래스.
 * 메뉴에 대한 CRUD 작업을 제공합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0/admin/menu")
public class MenuAdminController {

    // MenuAdminService 주입 (의존성 주입)
    private final MenuAdminService menuAdminService;

    /**
     * 모든 메뉴를 조회합니다.
     *
     * @return 모든 메뉴 리스트.
     */
    @GetMapping("")
    public List<Menu> findAll() {
        // 서비스 레이어를 호출하여 모든 메뉴를 가져옵니다.
        return menuAdminService.findAll();
    }

    /**
     * ID로 메뉴를 조회합니다.
     *
     * @param id 조회할 메뉴의 ID.
     * @return 조회된 메뉴 객체.
     * @throws RestfulException 메뉴를 찾을 수 없는 경우 예외를 던집니다.
     */
    @GetMapping("/{id}")
    public Menu findById(@PathVariable String id) throws RestfulException {
        // 서비스 레이어를 호출하여 ID에 해당하는 메뉴를 가져옵니다.
        return menuAdminService.findById(id);
    }

    /**
     * 메뉴를 저장합니다. 새로운 메뉴를 생성하거나 기존 메뉴를 업데이트합니다.
     *
     * @param id 저장할 메뉴의 ID. 새 메뉴인 경우 null.
     * @param menu 저장할 메뉴 정보가 담긴 객체.
     * @return 저장된 메뉴 객체.
     * @throws RestfulException 저장 과정에서 문제가 발생한 경우 예외를 던집니다.
     */
    @PostMapping({"", "/{id}"})
    public Menu save(@PathVariable(required = false) String id, @RequestBody Menu menu) throws RestfulException {
        // 서비스 레이어를 호출하여 메뉴를 저장합니다. 새 메뉴인 경우 null, 기존 메뉴인 경우 ID를 사용합니다.
        return menuAdminService.save(id, menu);
    }

    /**
     * 메뉴를 삭제합니다.
     *
     * @param id 삭제할 메뉴의 ID.
     * @throws RestfulException 메뉴를 찾을 수 없는 경우 예외를 던집니다.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) throws RestfulException {
        // 서비스 레이어를 호출하여 ID에 해당하는 메뉴를 삭제합니다.
        menuAdminService.delete(id);
    }
}
