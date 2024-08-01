package me.litzrsh.consbits.core.security.props;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.security.cors")
@Data
@EqualsAndHashCode(callSuper = false)
public class CorsConfigurationProperties extends CorsConfiguration {

    public CorsConfigurationProperties() {
        setAllowedOriginPatterns(List.of("*"));
        setAllowedMethods(List.of("GET", "POST", "DELETE"));
        setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Locale"));
        setAllowCredentials(false);
    }
}
