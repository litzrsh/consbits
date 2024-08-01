package me.litzrsh.consbits.core.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public abstract class MessageUtils {

    private static MessageSource messageSource;

    public static String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public static void setMessageSource(MessageSource messageSource) throws Exception {
        if (MessageUtils.messageSource != null) {
            throw new Exception("Message source has already been set!");
        }
        MessageUtils.messageSource = messageSource;
    }
}
