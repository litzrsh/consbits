package me.litzrsh.consbits.core.event;

import me.litzrsh.consbits.core.CommonConstants;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;

@Getter
public class SingleDeleteEvent<T> extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    private final T entity;

    public SingleDeleteEvent(T entity) {
        super(SingleDeleteEvent.class);
        this.entity = entity;
    }
}
