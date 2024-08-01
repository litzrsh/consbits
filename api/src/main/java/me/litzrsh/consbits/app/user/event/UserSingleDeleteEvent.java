package me.litzrsh.consbits.app.user.event;

import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.event.SingleDeleteEvent;

import java.io.Serial;

public class UserSingleDeleteEvent extends SingleDeleteEvent<User> {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    public UserSingleDeleteEvent(User entity) {
        super(entity);
    }
}
