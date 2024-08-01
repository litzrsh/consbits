package me.litzrsh.consbits.api.code;

import me.litzrsh.consbits.app.code.Code;
import me.litzrsh.consbits.app.code.CodeSearchParams;
import me.litzrsh.consbits.app.code.service.CodeAdminService;
import me.litzrsh.consbits.core.Page;
import me.litzrsh.consbits.core.exception.RestfulException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 코드 관리를 위한 REST API 컨트롤러 클래스.
 * 코드에 대한 CRUD 작업을 제공합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0/admin/code")
public class CodeAdminController {

    // CodeAdminService 주입 (의존성 주입)
    private final CodeAdminService codeAdminService;

    /**
     * 모든 코드를 조회합니다.
     *
     * @param params 코드 검색 파라미터.
     * @return 페이지네이션된 코드 리스트.
     */
    @GetMapping("")
    public Page<Code> findAll(CodeSearchParams params) {
        // 서비스 레이어를 호출하여 모든 코드를 가져옵니다.
        return codeAdminService.findAll(params);
    }

    /**
     * ID로 코드를 조회합니다.
     *
     * @param id 조회할 코드의 ID.
     * @return 조회된 코드 객체.
     * @throws RestfulException 코드를 찾을 수 없는 경우 예외를 던집니다.
     */
    @GetMapping("/{id}")
    public Code findById(@PathVariable String id) throws RestfulException {
        // 서비스 레이어를 호출하여 ID에 해당하는 코드를 가져옵니다.
        return codeAdminService.findById(id);
    }

    /**
     * 코드를 저장합니다. 새로운 코드를 생성하거나 기존 코드를 업데이트합니다.
     *
     * @param id    저장할 코드의 ID. 새 코드인 경우 null.
     * @param code  저장할 코드 정보가 담긴 객체.
     * @return 저장된 코드 객체.
     * @throws RestfulException 저장 과정에서 문제가 발생한 경우 예외를 던집니다.
     */
    @PostMapping({"", "/{id}"})
    public Code save(@PathVariable(required = false) String id, @RequestBody Code code) throws RestfulException {
        // 서비스 레이어를 호출하여 코드를 저장합니다. 새 코드인 경우 null, 기존 코드인 경우 ID를 사용합니다.
        return codeAdminService.save(id, code);
    }

    /**
     * @deprecated
     * 코드 리스트를 저장합니다.
     *
     * @param entities 저장할 코드 리스트.
     * @throws RestfulException 저장 과정에서 문제가 발생한 경우 예외를 던집니다.
     */
//    @PostMapping("/list")
    @Deprecated
    public void saveList(@RequestBody List<Code> entities) throws RestfulException {
        // 서비스 레이어를 호출하여 코드 리스트를 저장합니다.
        codeAdminService.saveList(entities);
    }

    /**
     * 코드를 삭제합니다.
     *
     * @param id 삭제할 코드의 ID.
     * @throws RestfulException 코드를 찾을 수 없는 경우 예외를 던집니다.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) throws RestfulException {
        // 서비스 레이어를 호출하여 ID에 해당하는 코드를 삭제합니다.
        codeAdminService.delete(id);
    }
}
