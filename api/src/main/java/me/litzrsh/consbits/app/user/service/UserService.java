package me.litzrsh.consbits.app.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.app.user.dto.ProfileUpdateDTO;
import me.litzrsh.consbits.app.user.dto.ProfileWithdrawDTO;
import me.litzrsh.consbits.app.user.event.ProfileSaveEvent;
import me.litzrsh.consbits.app.user.event.UserSingleGetEvent;
import me.litzrsh.consbits.app.user.repository.UserRepository;
import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.exception.RestfulException;
import me.litzrsh.consbits.core.util.SessionUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Date;

import static me.litzrsh.consbits.core.CommonConstants.UNAUTHORIZED_EXCEPTION;

/**
 * 사용자 프로필 관리를 위한 서비스 클래스.
 * 사용자 프로필 조회, 업데이트, 탈퇴 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class UserService implements InitializingBean {

    public static final RestfulException INCORRECT_PASSWORD = new RestfulException(HttpStatus.BAD_REQUEST, "UE0002");
    public static final RestfulException USED_EMAIL = new RestfulException(HttpStatus.BAD_REQUEST, "UE0003");
    public static final RestfulException USED_MOBILE = new RestfulException(HttpStatus.BAD_REQUEST, "UE0004");
    public static final RestfulException USED_NAME = new RestfulException(HttpStatus.BAD_REQUEST, "UE0006");
    public static final RestfulException DATA_PARSE_FAILED = new RestfulException(HttpStatus.BAD_REQUEST, "UE0007");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 현재 인증된 사용자의 프로필을 조회합니다.
     *
     * @return 사용자 프로필을 나타내는 User 객체.
     * @throws RestfulException 사용자가 인증되지 않은 경우 예외 발생.
     */
    public User getProfile() throws RestfulException {
        User user = userRepository.findById(SessionUtils.getUsername())
                .orElseThrow(() -> new RestfulException(UNAUTHORIZED_EXCEPTION));
        eventPublisher.publishEvent(new UserSingleGetEvent(user));
        return user;
    }

    /**
     * 사용자의 프로필을 업데이트합니다.
     *
     * @param dto 업데이트할 프로필 정보가 담긴 DTO.
     * @return 업데이트된 User 객체.
     * @throws RestfulException 업데이트 과정에서 문제가 발생한 경우 예외 발생.
     */
    @Transactional
    public User updateProfile(ProfileUpdateDTO dto) throws RestfulException {
        User user = getProfile();
        BeanUtils.copyProperties(dto, user, "password", "email", "mobile");
        if (StringUtils.hasText(dto.getPassword())) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setLastChangePasswordAt(new Date());
        }
        if (StringUtils.hasText(dto.getEmail()) && !dto.getEmail().equalsIgnoreCase(user.getEmail())) {
            checkEmail(dto.getEmail(), user.getId());
            user.setEmail(dto.getEmail());
            user.setEmailVerified(false);
            user.setEmailVerifiedAt(null);
        } else if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            user.setEmail(null);
            user.setEmailVerified(false);
            user.setEmailVerifiedAt(null);
        }
        if (StringUtils.hasText(dto.getMobile()) && !dto.getMobile().equalsIgnoreCase(user.getMobile())) {
            checkMobile(dto.getMobile(), user.getId());
            user.setMobile(dto.getMobile());
            user.setMobileVerified(false);
            user.setMobileVerifiedAt(null);
        } else if (dto.getMobile() == null || dto.getMobile().isEmpty()) {
            user.setMobile(null);
            user.setMobileVerified(false);
            user.setMobileVerifiedAt(null);
        }
        userRepository.save(user);
        SessionUtils.NAME_MAP.remove(SessionUtils.getUsername());
        return user;
    }

    /**
     * 사용자의 비밀번호를 확인합니다.
     *
     * @param password 확인할 비밀번호.
     * @throws RestfulException 비밀번호가 일치하지 않는 경우 예외 발생.
     */
    public void checkPassword(String password) throws RestfulException {
        User user = getProfile();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RestfulException(INCORRECT_PASSWORD);
        }
    }

    /**
     * 사용자의 이메일을 확인합니다.
     *
     * @param email 확인할 이메일.
     * @param id    사용자 ID.
     * @throws RestfulException 이메일이 이미 사용 중인 경우 예외 발생.
     */
    public void checkEmail(String email, String id) throws RestfulException {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null && !user.getId().equals(id)) {
            throw new RestfulException(USED_EMAIL);
        }
    }

    /**
     * 사용자의 휴대폰 번호를 확인합니다.
     *
     * @param mobile 확인할 휴대폰 번호.
     * @param id     사용자 ID.
     * @throws RestfulException 휴대폰 번호가 이미 사용 중인 경우 예외 발생.
     */
    public void checkMobile(String mobile, String id) throws RestfulException {
        User user = userRepository.findByMobile(mobile).orElse(null);
        if (user != null && !user.getId().equals(id)) {
            throw new RestfulException(USED_MOBILE);
        }
    }

    /**
     * 사용자를 탈퇴 처리합니다.
     *
     * @param session 현재 세션.
     * @param dto     탈퇴 이유가 담긴 DTO.
     * @throws RestfulException 탈퇴 과정에서 문제가 발생한 경우 예외 발생.
     */
    @Transactional
    public void withdraw(HttpSession session, ProfileWithdrawDTO dto) throws RestfulException {
        User user = getProfile();
        user.cleanup();
        user.setStatus(CommonConstants.USER_STATUS_WITHDRAW);
        user.setWithdrawAt(new Date());
        user.setWithdrawReasonCode(dto.getReasonCode());
        user.setWithdrawReason(dto.getReason());
        userRepository.save(user);
        session.invalidate();
    }

    /**
     * 초기화 작업을 수행합니다.
     *
     * @throws Exception 초기화 과정에서 문제가 발생한 경우 예외 발생.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        SessionUtils.setUserRepository(userRepository);
    }
}
