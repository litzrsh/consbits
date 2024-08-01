package me.litzrsh.consbits.core.crypto.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

@SuppressWarnings({"unused"})
public abstract class KeyGeneratorUtils {

    public static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }
}
