package me.litzrsh.consbits.core.crypto;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@SuppressWarnings({"unused"})
@Slf4j
public class RsaDecoder {

    public byte[] decrypt(byte[] bytes, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(Rsa.RSA_INSTANCE);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            log.error("Failed to decrypt message : {}", e.getMessage());
            if (log.isTraceEnabled()) e.printStackTrace(System.err);
            return null;
        }
    }

    public byte[] decrypt(String message, PrivateKey privateKey) {
        return decrypt(Base64.getDecoder().decode(message), privateKey);
    }

    public String decryptString(byte[] bytes, PrivateKey privateKey) {
        byte[] results = decrypt(bytes, privateKey);
        return results == null ? null : new String(results, StandardCharsets.UTF_8);
    }

    public String decryptString(String message, PrivateKey privateKey) {
        byte[] results = decrypt(message, privateKey);
        return results == null ? null : new String(results, StandardCharsets.UTF_8);
    }

    public byte[] verify(byte[] bytes, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(Rsa.SIGN_INSTANCE);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            log.error("Failed to verify message : {}", e.getMessage());
            if (log.isTraceEnabled()) e.printStackTrace(System.err);
            return null;
        }
    }

    public byte[] verify(String message, PublicKey publicKey) {
        return verify(Base64.getDecoder().decode(message), publicKey);
    }

    public String verifyString(byte[] bytes, PublicKey publicKey) {
        byte[] results = verify(bytes, publicKey);
        return results == null ? null : new String(results, StandardCharsets.UTF_8);
    }

    public String verifyString(String message, PublicKey publicKey) {
        byte[] results = verify(message, publicKey);
        return results == null ? null : new String(results, StandardCharsets.UTF_8);
    }
}
