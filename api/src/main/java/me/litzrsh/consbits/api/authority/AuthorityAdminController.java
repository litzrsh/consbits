package me.litzrsh.consbits.api.authority;

import me.litzrsh.consbits.app.authority.Authority;
import me.litzrsh.consbits.app.authority.service.AuthorityAdminService;
import me.litzrsh.consbits.core.exception.RestfulException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 권한 관리를 위한 REST API 컨트롤러 클래스.
 * 권한에 대한 CRUD 작업을 제공합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0/admin/authority")
public class AuthorityAdminController {

    // AuthorityAdminService 주입 (의존성 주입)
    private final AuthorityAdminService authorityAdminService;

    /**
     * 모든 권한을 조회합니다.
     *
     * @return 모든 권한 리스트.
     */
    @GetMapping("")
    public List<Authority> findAll() {
        // 서비스 레이어를 호출하여 모든 권한을 가져옵니다.
        return authorityAdminService.findAll();
    }

    /**
     * ID로 권한을 조회합니다.
     *
     * @param id 조회할 권한의 ID.
     * @return 조회된 권한 객체.
     * @throws RestfulException 권한을 찾을 수 없는 경우 예외를 던집니다.
     */
    @GetMapping("/{id}")
    public Authority findById(@PathVariable String id) throws RestfulException {
        // 서비스 레이어를 호출하여 ID에 해당하는 권한을 가져옵니다.
        return authorityAdminService.findById(id);
    }

    /**
     * 권한을 저장합니다. 새로운 권한을 생성하거나 기존 권한을 업데이트합니다.
     *
     * @param id        저장할 권한의 ID. 새 권한인 경우 null.
     * @param authority 저장할 권한 정보가 담긴 객체.
     * @return 저장된 권한 객체.
     * @throws RestfulException 저장 과정에서 문제가 발생한 경우 예외를 던집니다.
     */
    @PostMapping({"", "/{id}"})
    public Authority save(@PathVariable(required = false) String id, @RequestBody Authority authority) throws RestfulException {
        // 서비스 레이어를 호출하여 권한을 저장합니다. 새 권한인 경우 null, 기존 권한인 경우 ID를 사용합니다.
        return authorityAdminService.save(id, authority);
    }

    /**
     * 권한을 삭제합니다.
     *
     * @param id 삭제할 권한의 ID.
     * @throws RestfulException 권한을 찾을 수 없는 경우 예외를 던집니다.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) throws RestfulException {
        // 서비스 레이어를 호출하여 ID에 해당하는 권한을 삭제합니다.
        authorityAdminService.delete(id);
    }
}
