package me.litzrsh.consbits.core.crypto.config;

import com.nimbusds.jose.jwk.RSAKey;
import me.litzrsh.consbits.core.crypto.PemStorage;
import me.litzrsh.consbits.core.crypto.props.CryptoConfigurationProperties;
import me.litzrsh.consbits.core.crypto.util.RsaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CryptoConfig implements InitializingBean {

    private static final String ID = "MAGINON_RSA";

    private final CryptoConfigurationProperties properties;

    @Override
    public void afterPropertiesSet() throws Exception {
        PemStorage storage = new PemStorage();
        RSAKey pair = storage.getRsaKey(ID, properties.getPublicKeyLocation(), properties.getPrivateKeyLocation());
        RsaUtils.setKey(pair);
        RsaUtils.setPublicKey(pair.toPublicKey());
        RsaUtils.setPrivateKey(pair.toPrivateKey());
    }
}
