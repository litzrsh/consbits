package me.litzrsh.consbits.core.event;

import me.litzrsh.consbits.core.CommonConstants;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;

@Getter
public class SingleSaveEvent<T> extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    private final T entity;

    public SingleSaveEvent(T entity) {
        super(SingleSaveEvent.class);
        this.entity = entity;
    }
}
