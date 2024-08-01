package me.litzrsh.consbits.core.crypto;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SuppressWarnings({"unused"})
@Slf4j
public class AesDecoder {

    public byte[] decrypt(byte[] bytes, String key) {
        try {
            Cipher cipher = Cipher.getInstance(Aes.AES_INSTANCE);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES"));
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            log.error("Failed to decrypt message : {}", e.getMessage());
            if (log.isTraceEnabled()) e.printStackTrace(System.err);
            return null;
        }
    }

    public byte[] decrypt(String message, String key) {
        return decrypt(Base64.getDecoder().decode(message), key);
    }

    public String decryptString(byte[] bytes, String key) {
        byte[] res = decrypt(bytes, key);
        return res == null ? null : new String(res, StandardCharsets.UTF_8);
    }

    public String decryptString(String message, String key) {
        byte[] res = decrypt(message, key);
        return res == null ? null : new String(res, StandardCharsets.UTF_8);
    }
}
