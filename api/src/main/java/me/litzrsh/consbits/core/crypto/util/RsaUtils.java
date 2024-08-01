package me.litzrsh.consbits.core.crypto.util;

import com.nimbusds.jose.jwk.RSAKey;
import me.litzrsh.consbits.core.crypto.Rsa;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.security.PrivateKey;
import java.security.PublicKey;

@SuppressWarnings({"unused"})
@Slf4j
public abstract class RsaUtils {

    @Getter
    private static RSAKey key;
    @Getter
    private static PublicKey publicKey;
    @Getter
    private static PrivateKey privateKey;

    public static String encrypt(String message) {
        return Rsa.getEncoder().encryptString(message, publicKey);
    }

    public static String decrypt(String message) {
        return Rsa.getDecoder().decryptString(message, privateKey);
    }

    public static String sign(String message) {
        return Rsa.getEncoder().signString(message, privateKey);
    }

    public static String verify(String message) {
        return Rsa.getDecoder().verifyString(message, publicKey);
    }

    public static void setKey(RSAKey key) throws Exception {
        if (RsaUtils.key != null) throw new Exception("Key already set");
        RsaUtils.key = key;
    }

    public static void setPrivateKey(PrivateKey privateKey) throws Exception {
        if (RsaUtils.privateKey != null) throw new Exception("Private key already set");
        RsaUtils.privateKey = privateKey;
    }

    public static void setPublicKey(PublicKey publicKey) throws Exception {
        if (RsaUtils.publicKey != null) throw new Exception("Public key already set");
        RsaUtils.publicKey = publicKey;
    }
}
