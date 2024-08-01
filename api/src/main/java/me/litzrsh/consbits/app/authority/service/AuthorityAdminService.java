package me.litzrsh.consbits.app.authority.service;

import me.litzrsh.consbits.app.authority.Authority;
import me.litzrsh.consbits.app.authority.event.AuthoritySingleDeleteEvent;
import me.litzrsh.consbits.app.authority.event.AuthoritySingleGetEvent;
import me.litzrsh.consbits.app.authority.event.AuthoritySingleSaveEvent;
import me.litzrsh.consbits.app.authority.repository.AuthorityRepository;
import me.litzrsh.consbits.core.annotation.HasAuthority;
import me.litzrsh.consbits.core.exception.RestfulException;
import me.litzrsh.consbits.core.util.SerialUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static me.litzrsh.consbits.core.CommonConstants.ADMIN;
import static me.litzrsh.consbits.core.CommonConstants.SYS_ADMIN;

/**
 * 관리자 권한 서비스를 제공하는 클래스.
 * 권한에 대한 조회, 저장, 삭제 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
@HasAuthority({SYS_ADMIN, ADMIN})
public class AuthorityAdminService {

    // 권한을 찾을 수 없는 경우 발생하는 예외를 정의합니다.
    public static final RestfulException NOT_FOUND = new RestfulException(HttpStatus.NOT_FOUND, "AE0001");

    private final AuthorityRepository authorityRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 모든 권한을 조회합니다.
     *
     * @return 모든 권한 리스트.
     */
    public List<Authority> findAll() {
        // 권한 저장소를 통해 모든 권한을 조회합니다.
        return authorityRepository.findAuthorities();
    }

    /**
     * ID로 권한을 조회합니다.
     *
     * @param id 조회할 권한의 ID.
     * @return 조회된 권한 객체.
     * @throws RestfulException 권한을 찾을 수 없는 경우 예외를 던집니다.
     */
    public Authority findById(String id) throws RestfulException {
        // 주어진 ID로 권한을 조회하고, 권한을 찾을 수 없는 경우 예외를 던집니다.
        Authority entity = authorityRepository.findById(id)
                .orElseThrow(() -> new RestfulException(NOT_FOUND));
        // 권한 조회 이벤트를 발행합니다.
        eventPublisher.publishEvent(new AuthoritySingleGetEvent(entity));
        return entity;
    }

    /**
     * 권한을 저장합니다. 새로운 권한을 생성하거나 기존 권한을 업데이트합니다.
     *
     * @param id        저장할 권한의 ID. 새 권한인 경우 null.
     * @param authority 저장할 권한 정보가 담긴 객체.
     * @return 저장된 권한 객체.
     * @throws RestfulException 저장 과정에서 문제가 발생한 경우 예외를 던집니다.
     */
    @Transactional
    public Authority save(String id, Authority authority) throws RestfulException {
        final boolean isEdit = StringUtils.hasText(id);
        if (isEdit) {
            // 주어진 ID로 기존 권한을 조회하고, 권한을 찾을 수 없는 경우 예외를 던집니다.
            Authority exists = authorityRepository.findById(id)
                    .orElseThrow(() -> new RestfulException(NOT_FOUND));
            // 기존 권한의 ID와 코드를 설정합니다.
            authority.setId(exists.getId());
            authority.setCode(exists.getCode());
        } else {
            // 새로운 권한의 ID를 설정합니다.
            authority.setId(SerialUtils.get(Authority.SERIAL));
        }
        // 권한 저장 이벤트를 발행합니다.
        eventPublisher.publishEvent(new AuthoritySingleSaveEvent(authority));
        // 권한을 저장합니다.
        authorityRepository.save(authority);
        return authority;
    }

    /**
     * 권한을 삭제합니다.
     *
     * @param id 삭제할 권한의 ID.
     * @throws RestfulException 권한을 찾을 수 없는 경우 예외를 던집니다.
     */
    @Transactional
    public void delete(String id) throws RestfulException {
        // 주어진 ID로 권한을 조회하고, 권한을 찾을 수 없는 경우 예외를 던집니다.
        Authority authority = authorityRepository.findById(id)
                .orElseThrow(() -> new RestfulException(NOT_FOUND));
        // 권한 삭제 이벤트를 발행합니다.
        eventPublisher.publishEvent(new AuthoritySingleDeleteEvent(authority));
        // 권한을 삭제합니다.
        authorityRepository.delete(authority);
    }
}
