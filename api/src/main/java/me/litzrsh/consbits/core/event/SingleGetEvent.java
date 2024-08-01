package me.litzrsh.consbits.core.event;

import me.litzrsh.consbits.core.CommonConstants;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;

@Getter
public class SingleGetEvent<T> extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    private final T entity;

    public SingleGetEvent(T entity) {
        super(SingleGetEvent.class);
        this.entity = entity;
    }
}
