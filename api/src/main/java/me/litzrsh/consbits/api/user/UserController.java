package me.litzrsh.consbits.api.user;

import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.app.user.dto.ProfileUpdateDTO;
import me.litzrsh.consbits.app.user.dto.ProfileWithdrawDTO;
import me.litzrsh.consbits.app.user.service.UserImageService;
import me.litzrsh.consbits.app.user.service.UserService;
import me.litzrsh.consbits.core.exception.RestfulException;
import me.litzrsh.consbits.core.util.SessionUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 사용자 프로필 관리를 위한 REST API 컨트롤러 클래스.
 * 사용자 프로필 조회, 업데이트, 비밀번호 및 이메일 확인, 탈퇴 기능을 제공합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0/profile")
public class UserController {

    // UserService 주입 (의존성 주입)
    private final UserService userService;

    // UserImageService 주입 (의존성 주입)
    private final UserImageService userImageService;

    /**
     * 현재 사용자의 프로필을 조회합니다.
     *
     * @return 현재 사용자의 프로필.
     * @throws RestfulException 사용자가 인증되지 않은 경우 예외를 던집니다.
     */
    @GetMapping("")
    public User getProfile() throws RestfulException {
        // 서비스 레이어를 호출하여 현재 사용자의 프로필을 가져옵니다.
        return userService.getProfile();
    }

    /**
     * 현재 사용자의 프로필을 업데이트합니다.
     *
     * @param dto 업데이트할 프로필 정보가 담긴 DTO.
     * @return 업데이트된 사용자 프로필.
     * @throws RestfulException 업데이트 과정에서 문제가 발생한 경우 예외를 던집니다.
     */
    @PostMapping("")
    public User updateProfile(@RequestBody ProfileUpdateDTO dto) throws RestfulException {
        // 서비스 레이어를 호출하여 현재 사용자의 프로필을 업데이트합니다.
        return userService.updateProfile(dto);
    }

    /**
     * 현재 사용자의 비밀번호를 확인합니다.
     *
     * @param dto 비밀번호 정보가 담긴 DTO.
     * @throws RestfulException 비밀번호가 일치하지 않는 경우 예외를 던집니다.
     */
    @PostMapping("/check/password")
    public void checkPassword(@RequestBody ProfileUpdateDTO dto) throws RestfulException {
        // 서비스 레이어를 호출하여 현재 사용자의 비밀번호를 확인합니다.
        userService.checkPassword(dto.getPassword());
    }

    /**
     * 현재 사용자의 이메일을 확인합니다.
     *
     * @param dto 이메일 정보가 담긴 DTO.
     * @throws RestfulException 이메일이 이미 사용 중인 경우 예외를 던집니다.
     */
    @PostMapping("/check/email")
    public void checkEmail(@RequestBody ProfileUpdateDTO dto) throws RestfulException {
        // 서비스 레이어를 호출하여 현재 사용자의 이메일을 확인합니다.
        userService.checkEmail(dto.getEmail(), SessionUtils.getUsername());
    }

    /**
     * 현재 사용자의 휴대폰 번호를 확인합니다.
     *
     * @param dto 휴대폰 번호 정보가 담긴 DTO.
     * @throws RestfulException 휴대폰 번호가 이미 사용 중인 경우 예외를 던집니다.
     */
    @PostMapping("/check/mobile")
    public void checkMobile(@RequestBody ProfileUpdateDTO dto) throws RestfulException {
        // 서비스 레이어를 호출하여 현재 사용자의 휴대폰 번호를 확인합니다.
        userService.checkMobile(dto.getMobile(), SessionUtils.getUsername());
    }

    /**
     * 현재 사용자를 탈퇴 처리합니다.
     *
     * @param session 현재 세션.
     * @param dto 탈퇴 이유가 담긴 DTO.
     * @throws RestfulException 탈퇴 과정에서 문제가 발생한 경우 예외를 던집니다.
     */
    @PostMapping("/withdraw")
    public void withdraw(HttpSession session, @RequestBody ProfileWithdrawDTO dto) throws RestfulException {
        // 서비스 레이어를 호출하여 현재 사용자를 탈퇴 처리합니다.
        userService.withdraw(session, dto);
    }

    /**
     * 현재 사용자의 프로필 이미지를 업데이트합니다.
     *
     * @param request 멀티파트 HTTP 요청 객체.
     * @return 업데이트된 사용자 객체.
     * @throws RestfulException 업데이트 과정에서 문제가 발생한 경우 예외를 던집니다.
     */
    @PostMapping("/image")
    public User updateImage(MultipartHttpServletRequest request) throws RestfulException {
        // 서비스 레이어를 호출하여 현재 사용자의 프로필 이미지를 업데이트합니다.
        return userImageService.updateProfileImage(request);
    }

    /**
     * ID로 사용자의 프로필 이미지를 조회합니다.
     *
     * @param id 이미지 조회할 사용자 ID.
     * @param response HTTP 응답 객체.
     */
    @GetMapping("/image/{id}")
    public void getImage(@PathVariable String id, HttpServletResponse response) {
        // 서비스 레이어를 호출하여 ID에 해당하는 프로필 이미지를 가져옵니다.
        userImageService.getImage(id, response);
    }
}
