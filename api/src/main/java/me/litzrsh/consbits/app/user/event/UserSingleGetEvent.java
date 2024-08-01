package me.litzrsh.consbits.app.user.event;

import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.event.SingleGetEvent;

import java.io.Serial;

public class UserSingleGetEvent extends SingleGetEvent<User> {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    public UserSingleGetEvent(User entity) {
        super(entity);
    }
}
