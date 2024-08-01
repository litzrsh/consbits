package me.litzrsh.consbits.app.user.event;

import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.event.SingleSaveEvent;

import java.io.Serial;

public class UserSingleSaveEvent extends SingleSaveEvent<User> {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    public UserSingleSaveEvent(User entity) {
        super(entity);
    }
}
