package me.litzrsh.consbits.core.security.props;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.security")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SecurityConfigurationProperties {

    private String accessKey = "WWYkuCaLgmXSBgCf9TRxd6cQwn5mKpYU22kwZbCMyEwxrw6FMZE4GwJMdscnDm2F";
}
