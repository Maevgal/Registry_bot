package org.maevgal.registration_bot.service;

import lombok.RequiredArgsConstructor;
import org.maevgal.registration_bot.constant.TgOperationStatus;
import org.maevgal.registration_bot.handler.MessageHandler;
import org.maevgal.registration_bot.model.TgOperation;
import org.maevgal.registration_bot.repository.TgOperationRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HandleMessageService {

    private final TgOperationRepository tgOperationRepository;
    private final Map<TgOperationStatus, MessageHandler> messageHandlers;

    public SendMessage handle(Update update, long chatId) {
        Optional<TgOperation> tg = tgOperationRepository.findByChatId(chatId);
        if (tg.isEmpty()) {
            return SendMessage // Create a message object
                    .builder()
                    .chatId(chatId)
                    .text("Введите команду /start")
                    .build();
        }
        TgOperation tgOperation = tg.get();
        return messageHandlers.get(tgOperation.getStatus()).handleMessage(update, tgOperation, chatId);
    }
}
