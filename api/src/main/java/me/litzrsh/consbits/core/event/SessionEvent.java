package me.litzrsh.consbits.core.event;

import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.core.CommonConstants;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;

@Getter
public class SessionEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    private final User userDetails;
    private final HttpSession session;

    public SessionEvent(User userDetails, HttpSession session) {
        super(SessionEvent.class);
        this.userDetails = userDetails;
        this.session = session;
    }
}
