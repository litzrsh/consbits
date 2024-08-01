package me.litzrsh.consbits.app.user.props;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.user")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserImageConfigurationProperties {

    private String basePath = "./upload/profile";
}
