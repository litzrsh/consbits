package me.litzrsh.consbits.app.authority.event;

import me.litzrsh.consbits.app.authority.Authority;
import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.event.SingleSaveEvent;

import java.io.Serial;

/**
 * 단일 권한 저장 이벤트 클래스.
 * 특정 권한이 저장될 때 발생하는 이벤트를 정의합니다.
 */
public class AuthoritySingleSaveEvent extends SingleSaveEvent<Authority> {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    /**
     * AuthoritySingleSaveEvent 생성자.
     *
     * @param entity 저장된 권한 엔티티.
     */
    public AuthoritySingleSaveEvent(Authority entity) {
        super(entity);
    }
}
