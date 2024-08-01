package me.litzrsh.consbits.app.menu.event;

import me.litzrsh.consbits.app.menu.Menu;
import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.event.SingleDeleteEvent;

import java.io.Serial;

/**
 * 메뉴 삭제 이벤트 클래스.
 * 단일 메뉴 삭제 이벤트를 나타내며, 이벤트 소스를 메뉴 엔티티로 설정합니다.
 */
public class MenuSingleDeleteEvent extends SingleDeleteEvent<Menu> {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    /**
     * MenuSingleDeleteEvent 생성자.
     * 주어진 메뉴 엔티티를 이벤트 소스로 설정합니다.
     *
     * @param entity 삭제된 메뉴 엔티티.
     */
    public MenuSingleDeleteEvent(Menu entity) {
        super(entity);
    }
}
