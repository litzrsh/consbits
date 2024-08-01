package me.litzrsh.consbits.core.crypto;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@SuppressWarnings({"unused"})
@Slf4j
public class RsaEncoder {

    public byte[] encrypt(byte[] bytes, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(Rsa.RSA_INSTANCE);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            log.error("Failed to encrypt message : {}", e.getMessage());
            if (log.isTraceEnabled()) e.printStackTrace(System.err);
            return null;
        }
    }

    public byte[] encrypt(String message, PublicKey publicKey) {
        return encrypt(message.getBytes(StandardCharsets.UTF_8), publicKey);
    }

    public String encryptString(byte[] bytes, PublicKey publicKey) {
        byte[] results = encrypt(bytes, publicKey);
        return results == null ? null : Base64.getEncoder().encodeToString(results);
    }

    public String encryptString(String message, PublicKey publicKey) {
        byte[] results = encrypt(message, publicKey);
        return results == null ? null : Base64.getEncoder().encodeToString(results);
    }

    public byte[] sign(byte[] bytes, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(Rsa.SIGN_INSTANCE);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            log.error("Failed to sign message : {}", e.getMessage());
            if (log.isTraceEnabled()) e.printStackTrace(System.err);
            return null;
        }
    }

    public byte[] sign(String message, PrivateKey privateKey) {
        return sign(message.getBytes(StandardCharsets.UTF_8), privateKey);
    }

    public String signString(byte[] bytes, PrivateKey privateKey) {
        byte[] results = sign(bytes, privateKey);
        return results == null ? null : Base64.getEncoder().encodeToString(results);
    }

    public String signString(String message, PrivateKey privateKey) {
        byte[] results = sign(message, privateKey);
        return results == null ? null : Base64.getEncoder().encodeToString(results);
    }
}
