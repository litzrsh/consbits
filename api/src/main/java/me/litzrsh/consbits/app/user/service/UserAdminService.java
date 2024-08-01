package me.litzrsh.consbits.app.user.service;

import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.app.user.UserSearchParams;
import me.litzrsh.consbits.app.user.dto.UserUpdateDTO;
import me.litzrsh.consbits.app.user.event.UserListGetEvent;
import me.litzrsh.consbits.app.user.event.UserSingleDeleteEvent;
import me.litzrsh.consbits.app.user.event.UserSingleSaveEvent;
import me.litzrsh.consbits.app.user.repository.UserRepository;
import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.Page;
import me.litzrsh.consbits.core.annotation.HasAuthority;
import me.litzrsh.consbits.core.exception.RestfulException;
import me.litzrsh.consbits.core.util.SerialUtils;
import me.litzrsh.consbits.core.util.SessionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

import static me.litzrsh.consbits.core.CommonConstants.ADMIN;
import static me.litzrsh.consbits.core.CommonConstants.SYS_ADMIN;

/**
 * 관리자용 사용자 서비스 클래스.
 * 관리자 권한으로 사용자 목록 조회, 사용자 정보 수정, 사용자 삭제 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
@HasAuthority({SYS_ADMIN, ADMIN})
public class UserAdminService {

    public static final RestfulException USER_NOT_FOUND = new RestfulException(HttpStatus.NOT_FOUND, "UE0001");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 사용자 목록을 조회합니다.
     *
     * @param params 사용자 검색 조건.
     * @return 검색된 사용자 목록을 포함하는 페이지 객체.
     */
    public Page<User> findAll(UserSearchParams params) {
        Page<User> page = userRepository.findByPages(params);
        eventPublisher.publishEvent(new UserListGetEvent(page.getContents()));
        return page;
    }

    /**
     * ID로 사용자를 조회합니다.
     *
     * @param id 조회할 사용자 ID.
     * @return 조회된 사용자 객체.
     * @throws RestfulException 사용자를 찾을 수 없는 경우 예외 발생.
     */
    public User findById(String id) throws RestfulException {
        User user = userRepository.findById(id).orElseThrow(() -> USER_NOT_FOUND);
        eventPublisher.publishEvent(new UserListGetEvent(List.of(user)));
        return user;
    }

    /**
     * 사용자를 저장합니다. 새로운 사용자를 생성하거나 기존 사용자를 업데이트합니다.
     *
     * @param id 저장할 사용자 ID. 새 사용자인 경우 null.
     * @param dto 저장할 사용자 정보가 담긴 DTO.
     * @return 저장된 사용자 객체.
     * @throws RestfulException 저장 과정에서 문제가 발생한 경우 예외 발생.
     */
    @Transactional
    public User save(String id, UserUpdateDTO dto) throws RestfulException {
        final boolean isEdit = StringUtils.hasText(id);
        User user;
        if (isEdit) {
            user = userRepository.findById(id).orElseThrow(() -> USER_NOT_FOUND);
        } else {
            user = User.builder()
                    .id(SerialUtils.get(User.SERIAL))
                    .type(dto.getType())
                    .loginId(dto.getEmail())
                    .joinAt(new Date())
                    .joinType(CommonConstants.ADMIN)
                    .joinPath(CommonConstants.WEB)
                    .build();
        }
        BeanUtils.copyProperties(dto, user, "type", "loginId", "password", "email", "mobile");
        if (StringUtils.hasText(dto.getPassword())) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setLastChangePasswordAt(new Date());
        }
        if (StringUtils.hasText(dto.getEmail()) && !dto.getEmail().equalsIgnoreCase(user.getEmail())) {
            userService.checkEmail(dto.getEmail(), user.getId());
            user.setEmail(dto.getEmail());
            user.setEmailVerified(false);
            user.setEmailVerifiedAt(null);
        } else if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            user.setEmail(null);
            user.setEmailVerified(false);
            user.setEmailVerifiedAt(null);
        }
        if (StringUtils.hasText(dto.getMobile()) && !dto.getMobile().equalsIgnoreCase(user.getMobile())) {
            userService.checkMobile(dto.getMobile(), user.getId());
            user.setMobile(dto.getMobile());
            user.setMobileVerified(false);
            user.setMobileVerifiedAt(null);
        } else if (dto.getMobile() == null || dto.getMobile().isEmpty()) {
            user.setMobile(null);
            user.setMobileVerified(false);
            user.setMobileVerifiedAt(null);
        }
        userRepository.save(user);
        eventPublisher.publishEvent(new UserSingleSaveEvent(user));
        return user;
    }

    /**
     * 사용자를 삭제합니다.
     *
     * @param id 삭제할 사용자 ID.
     * @throws RestfulException 삭제 과정에서 문제가 발생한 경우 예외 발생.
     */
    @Transactional
    public void delete(String id) throws RestfulException {
        if (SessionUtils.getUsername().equals(id)) throw new RestfulException(HttpStatus.BAD_REQUEST, "UE0005");
        User user = userRepository.findById(id).orElseThrow(() -> USER_NOT_FOUND);
        eventPublisher.publishEvent(new UserSingleDeleteEvent(user));
        userRepository.delete(user);
    }

    /**
     * 사용자의 이메일을 확인합니다.
     *
     * @param email 확인할 이메일.
     * @param id 사용자 ID.
     * @throws RestfulException 이메일이 이미 사용 중인 경우 예외 발생.
     */
    public void checkEmail(String email, String id) throws RestfulException {
        userService.checkEmail(email, id);
    }

    /**
     * 사용자의 휴대폰 번호를 확인합니다.
     *
     * @param mobile 확인할 휴대폰 번호.
     * @param id 사용자 ID.
     * @throws RestfulException 휴대폰 번호가 이미 사용 중인 경우 예외 발생.
     */
    public void checkMobile(String mobile, String id) throws RestfulException {
        userService.checkMobile(mobile, id);
    }
}
