package org.maevgal.registration_bot.handler;

import lombok.RequiredArgsConstructor;
import org.maevgal.registration_bot.constant.TgOperationStatus;
import org.maevgal.registration_bot.model.TgOperation;
import org.maevgal.registration_bot.repository.TgOperationRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;

import static org.maevgal.registration_bot.constant.TgOperationStatus.WAIT_DATE;
import static org.maevgal.registration_bot.constant.TgOperationStatus.WAIT_DESCRIPTION;

@RequiredArgsConstructor
@Service
public class DescriptionMessageHandler implements MessageHandler {
    private final TgOperationRepository tgOperationRepository;

    public SendMessage handleMessage(Update update, TgOperation tgOperation, long chatId) {
        String receivedMessage = update.getMessage().getText();

        tgOperation.setStatus(WAIT_DATE);
        tgOperation.setDescription(receivedMessage);
        tgOperation.setUpdate_at(LocalDate.now());
        tgOperationRepository.save(tgOperation);

        return SendMessage
                .builder()
                .chatId(chatId)
                .text("Введите дату регистрации документа в формате 01.01.2024")
                .build();
    }

    @Override
    public TgOperationStatus getHandleStatus() {
        return WAIT_DESCRIPTION;
    }
}
