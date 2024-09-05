package org.maevgal.registration_bot.configuration;

import org.maevgal.registration_bot.constant.TgOperationStatus;
import org.maevgal.registration_bot.handler.MessageHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties(value = {BotProperties.class})
@EnableJdbcRepositories
public class ApplicationConfig {

    @Bean
    public Map<TgOperationStatus, MessageHandler> tgOperationStatusMessageHandlerMap(List<MessageHandler> messageHandlers) {
        return messageHandlers.stream()
                .collect(Collectors.toMap(MessageHandler::getHandleStatus, handler -> handler));
    }

    @Bean
    public TelegramClient telegramClient(BotProperties botProperties) {
        return new OkHttpTelegramClient(botProperties.getToken());
    }


}
