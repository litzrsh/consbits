package me.litzrsh.consbits.app.user.event;

import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.event.ListGetEvent;
import lombok.Getter;

import java.io.Serial;
import java.util.List;

@Getter
public class UserListGetEvent extends ListGetEvent<User> {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    public UserListGetEvent(List<User> entities) {
        super(entities);
    }
}
