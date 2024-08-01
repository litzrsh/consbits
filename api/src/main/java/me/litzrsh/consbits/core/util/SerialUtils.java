package me.litzrsh.consbits.core.util;

import me.litzrsh.consbits.core.serial.SerialGenerator;
import me.litzrsh.consbits.core.serial.service.SerialService;

import java.util.Queue;

public abstract class SerialUtils {

    private static SerialService serialService;

    public static Queue<String> getQueue(SerialGenerator gen, Long size) {
        return serialService.getQueue(gen, size);
    }

    public static String get(SerialGenerator gen) {
        return serialService.getQueue(gen, 1L).poll();
    }

    public static void setSerialService(SerialService serialService) throws Exception {
        if (SerialUtils.serialService != null) {
            throw new Exception("SerialService already set");
        }
        SerialUtils.serialService = serialService;
    }
}
