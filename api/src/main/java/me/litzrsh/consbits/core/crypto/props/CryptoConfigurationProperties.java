package me.litzrsh.consbits.core.crypto.props;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.security.crypto")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CryptoConfigurationProperties {

    private String publicKeyLocation = "./.ssh/scratch.pem";
    private String privateKeyLocation = "./.ssh/scratch_id";
}
