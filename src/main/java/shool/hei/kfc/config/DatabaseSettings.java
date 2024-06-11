package shool.hei.kfc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "postgresql.database")
public class DatabaseSettings {
    private String url;
    private String username;
    private String password;
}
