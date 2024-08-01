package me.litzrsh.consbits.core.props;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DatasourceConfigurationProperties {

    private boolean init = false;
}
