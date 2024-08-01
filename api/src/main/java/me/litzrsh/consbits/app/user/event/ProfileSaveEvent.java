package me.litzrsh.consbits.app.user.event;

import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.event.SingleSaveEvent;
import lombok.Getter;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.Serial;

@Getter
public class ProfileSaveEvent extends SingleSaveEvent<User> {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    private final MultipartHttpServletRequest request;

    public ProfileSaveEvent(User entity, MultipartHttpServletRequest request) {
        super(entity);
        this.request = request;
    }
}
