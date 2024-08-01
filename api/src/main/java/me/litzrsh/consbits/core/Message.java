package me.litzrsh.consbits.core;

import me.litzrsh.consbits.core.util.MessageUtils;

@SuppressWarnings({"unused", "LombokGetterMayBeUsed"})
public class Message {

    private final String code;
    private final Object[] args;

    public Message(String code, Object... args) {
        this.code = code;
        this.args = args;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return MessageUtils.getMessage(code, args);
    }
}
