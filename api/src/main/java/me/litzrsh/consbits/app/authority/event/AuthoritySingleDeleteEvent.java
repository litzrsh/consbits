package me.litzrsh.consbits.app.authority.event;

import me.litzrsh.consbits.app.authority.Authority;
import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.event.SingleDeleteEvent;

import java.io.Serial;

/**
 * 단일 권한 삭제 이벤트 클래스.
 * 특정 권한이 삭제될 때 발생하는 이벤트를 정의합니다.
 */
public class AuthoritySingleDeleteEvent extends SingleDeleteEvent<Authority> {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    /**
     * AuthoritySingleDeleteEvent 생성자.
     *
     * @param entity 삭제된 권한 엔티티.
     */
    public AuthoritySingleDeleteEvent(Authority entity) {
        super(entity);
    }
}
