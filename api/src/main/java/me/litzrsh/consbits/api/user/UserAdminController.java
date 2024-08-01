package me.litzrsh.consbits.api.user;

import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.app.user.UserSearchParams;
import me.litzrsh.consbits.app.user.dto.UserUpdateDTO;
import me.litzrsh.consbits.app.user.service.UserAdminService;
import me.litzrsh.consbits.core.Page;
import me.litzrsh.consbits.core.exception.RestfulException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 관리자 사용자 관리를 위한 REST API 컨트롤러 클래스.
 * 사용자에 대한 조회, 저장, 삭제, 이메일 및 휴대폰 번호 확인 기능을 제공합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0/admin/user")
public class UserAdminController {

    // UserAdminService 주입 (의존성 주입)
    private final UserAdminService userAdminService;

    /**
     * 모든 사용자를 조회합니다.
     *
     * @param params 사용자 검색 조건이 담긴 객체.
     * @return 검색된 사용자 리스트를 포함하는 페이지 객체.
     */
    @GetMapping("")
    public Page<User> findAll(UserSearchParams params) {
        // 서비스 레이어를 호출하여 모든 사용자를 가져옵니다.
        return userAdminService.findAll(params);
    }

    /**
     * ID로 사용자를 조회합니다.
     *
     * @param id 조회할 사용자의 ID.
     * @return 조회된 사용자 객체.
     * @throws RestfulException 사용자를 찾을 수 없는 경우 예외를 던집니다.
     */
    @GetMapping("/{id}")
    public User findById(@PathVariable String id) throws RestfulException {
        // 서비스 레이어를 호출하여 ID에 해당하는 사용자를 가져옵니다.
        return userAdminService.findById(id);
    }

    /**
     * 사용자를 저장합니다. 새로운 사용자를 생성하거나 기존 사용자를 업데이트합니다.
     *
     * @param id 저장할 사용자의 ID. 새 사용자인 경우 null.
     * @param dto 저장할 사용자 정보가 담긴 DTO 객체.
     * @return 저장된 사용자 객체.
     * @throws RestfulException 저장 과정에서 문제가 발생한 경우 예외를 던집니다.
     */
    @PostMapping({"", "/{id}"})
    public User save(@PathVariable(required = false) String id, @RequestBody UserUpdateDTO dto) throws RestfulException {
        // 서비스 레이어를 호출하여 사용자를 저장합니다. 새 사용자인 경우 null, 기존 사용자인 경우 ID를 사용합니다.
        return userAdminService.save(id, dto);
    }

    /**
     * 사용자를 삭제합니다.
     *
     * @param id 삭제할 사용자의 ID.
     * @throws RestfulException 사용자를 찾을 수 없는 경우 예외를 던집니다.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) throws RestfulException {
        // 서비스 레이어를 호출하여 ID에 해당하는 사용자를 삭제합니다.
        userAdminService.delete(id);
    }

    /**
     * 사용자의 이메일을 확인합니다.
     *
     * @param id 사용자 ID. 새로운 사용자인 경우 null.
     * @param dto 확인할 이메일 정보가 담긴 DTO 객체.
     * @throws RestfulException 이메일이 이미 사용 중인 경우 예외를 던집니다.
     */
    @PostMapping({"/check/email", "/{id}/check/email"})
    public void checkEmail(@PathVariable(required = false) String id, @RequestBody UserUpdateDTO dto) throws RestfulException {
        // 서비스 레이어를 호출하여 사용자의 이메일을 확인합니다.
        userAdminService.checkEmail(dto.getEmail(), id);
    }

    /**
     * 사용자의 휴대폰 번호를 확인합니다.
     *
     * @param id 사용자 ID. 새로운 사용자인 경우 null.
     * @param dto 확인할 휴대폰 번호 정보가 담긴 DTO 객체.
     * @throws RestfulException 휴대폰 번호가 이미 사용 중인 경우 예외를 던집니다.
     */
    @PostMapping({"/check/mobile", "/{id}/check/mobile"})
    public void checkMobile(@PathVariable(required = false) String id, @RequestBody UserUpdateDTO dto) throws RestfulException {
        // 서비스 레이어를 호출하여 사용자의 휴대폰 번호를 확인합니다.
        userAdminService.checkMobile(dto.getMobile(), id);
    }
}
