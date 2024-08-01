package me.litzrsh.consbits.core.crypto;

import com.nimbusds.jose.jwk.RSAKey;
import lombok.extern.slf4j.Slf4j;
import me.litzrsh.consbits.core.crypto.util.KeyGeneratorUtils;
import org.apache.commons.io.FilenameUtils;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.*;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@SuppressWarnings({"unused"})
@Slf4j
public class PemStorage {

    public static final String RSA_PUBLIC_KEY_NAME = "RSA PUBLIC KEY";
    public static final String RSA_PRIVATE_KEY_NAME = "RSA PRIVATE KEY";

    public RSAKey getRsaKey(String id, String publicKeyLocation, String privateKeyLocation) {
        RSAPublicKey publicKey;
        try {
            publicKey = this.getRsaPublicKeyFromFile(publicKeyLocation);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            return this.generateRsaAndStoreToFile(id, publicKeyLocation, privateKeyLocation);
        }

        RSAPrivateKey privateKey;
        try {
            privateKey = this.getRsaPrivateKeyFromFile(privateKeyLocation);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            return this.generateRsaAndStoreToFile(id, publicKeyLocation, privateKeyLocation);
        }

        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(id)
                .build();
    }

    private RSAKey generateRsaAndStoreToFile(String id, String publicKeyLocation, String privateKeyLocation) {
        KeyPair keyPair = KeyGeneratorUtils.generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        this.saveRsaPublicKeyToFile(id, publicKey, publicKeyLocation);
        this.saveRsaPrivateKeyToFile(id, privateKey, privateKeyLocation);

        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(id)
                .build();
    }

    private void saveRsaPublicKeyToFile(String id, RSAPublicKey publicKey, String location) {
        this.saveRsaKeyToFile(
                publicKey,
                RSA_PUBLIC_KEY_NAME + " " + id,
                location);
    }

    private void saveRsaPrivateKeyToFile(String id, RSAPrivateKey privateKey, String location) {
        this.saveRsaKeyToFile(
                privateKey,
                RSA_PRIVATE_KEY_NAME + " " + id,
                location);
    }

    private void saveRsaKeyToFile(Key key, String name, String filePath) {
        final String directoryPath = FilenameUtils.getFullPathNoEndSeparator(filePath);

        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            boolean result = directory.mkdirs();
            log.debug("Create directory '{}' result : {}", directoryPath, result);
        }

        try {
            this.writePemObject(key, name, filePath);
        } catch (IOException e) {
            if (log.isTraceEnabled()) {
                e.printStackTrace(System.err);
            }
        }
    }

    private void writePemObject(Key key, String name, String filepath) throws IOException {
        PemObject publicKeyObject = new PemObject(name, key.getEncoded());

        try (PemWriter pemWriter = new PemWriter(new OutputStreamWriter(new FileOutputStream(filepath)))) {
            pemWriter.writeObject(publicKeyObject);
        }
    }

    private RSAPublicKey getRsaPublicKeyFromFile(String location)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPublicKey publicKey;
        try (FileInputStream fis = new FileInputStream(location);
             InputStreamReader isr = new InputStreamReader(fis);
             PemReader reader = new PemReader(isr)) {
            PemObject object = reader.readPemObject();
            X509EncodedKeySpec spec = new X509EncodedKeySpec(object.getContent());
            KeyFactory factory = KeyFactory.getInstance("RSA");
            publicKey = (RSAPublicKey) factory.generatePublic(spec);
        }
        return publicKey;
    }

    private RSAPrivateKey getRsaPrivateKeyFromFile(String location)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPrivateKey privateKey;
        try (FileInputStream fis = new FileInputStream(location);
             InputStreamReader isr = new InputStreamReader(fis);
             PemReader reader = new PemReader(isr)) {
            PemObject object = reader.readPemObject();
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(object.getContent());
            KeyFactory factory = KeyFactory.getInstance("RSA");
            privateKey = (RSAPrivateKey) factory.generatePrivate(spec);
        }
        return privateKey;
    }

}
