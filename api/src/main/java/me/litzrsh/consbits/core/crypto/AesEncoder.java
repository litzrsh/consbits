package me.litzrsh.consbits.core.crypto;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SuppressWarnings({"unused"})
@Slf4j
public class AesEncoder {

    public byte[] encrypt(byte[] bytes, String key) {
        try {
            Cipher cipher = Cipher.getInstance(Aes.AES_INSTANCE);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES"));
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            log.error("Failed to encrypt message : {}", e.getMessage());
            if (log.isTraceEnabled()) e.printStackTrace(System.err);
            return null;
        }
    }

    public byte[] encrypt(String message, String key) {
        return encrypt(message.getBytes(StandardCharsets.UTF_8), key);
    }

    public String encryptString(byte[] bytes, String key) {
        byte[] res = encrypt(bytes, key);
        return res == null ? null : Base64.getEncoder().encodeToString(res);
    }

    public String ecryptString(String message, String key) {
        byte[] res = encrypt(message, key);
        return res == null ? null : Base64.getEncoder().encodeToString(res);
    }
}
