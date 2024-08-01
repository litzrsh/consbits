package me.litzrsh.consbits.core.event;

import me.litzrsh.consbits.core.CommonConstants;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;
import java.util.List;

@Getter
public class ListSaveEvent<T> extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    private final List<T> entities;

    public ListSaveEvent(List<T> entities) {
        super(ListSaveEvent.class);
        this.entities = entities;
    }
}
