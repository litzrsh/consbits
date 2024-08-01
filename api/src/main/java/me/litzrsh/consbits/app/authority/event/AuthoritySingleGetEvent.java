package me.litzrsh.consbits.app.authority.event;

import me.litzrsh.consbits.app.authority.Authority;
import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.event.SingleGetEvent;

import java.io.Serial;

/**
 * 단일 권한 조회 이벤트 클래스.
 * 특정 권한이 조회될 때 발생하는 이벤트를 정의합니다.
 */
public class AuthoritySingleGetEvent extends SingleGetEvent<Authority> {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    /**
     * AuthoritySingleGetEvent 생성자.
     *
     * @param entity 조회된 권한 엔티티.
     */
    public AuthoritySingleGetEvent(Authority entity) {
        super(entity);
    }
}
