package org.maevgal.registration_bot.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "bot")
public class BotProperties {

    private String token;
}
