package org.maevgal.registration_bot.handler;

import lombok.RequiredArgsConstructor;
import org.maevgal.registration_bot.constant.DocumentType;
import org.maevgal.registration_bot.constant.TgOperationStatus;
import org.maevgal.registration_bot.model.TgOperation;
import org.maevgal.registration_bot.repository.TgOperationRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;

import static org.maevgal.registration_bot.constant.TgOperationStatus.WAIT_RECIPIENT;
import static org.maevgal.registration_bot.constant.TgOperationStatus.WAIT_TYPE;

@RequiredArgsConstructor
@Service
public class TypeMessageHandler implements MessageHandler {
    private final TgOperationRepository tgOperationRepository;

    public SendMessage handleMessage(Update update, TgOperation tgOperation, long chatId) {
        DocumentType receivedMessage = DocumentType.valueOf(update.getCallbackQuery().getData());

        tgOperation.setStatus(WAIT_RECIPIENT);

        tgOperation.setDocumentType(receivedMessage);


        tgOperation.setUpdate_at(LocalDate.now());
        tgOperationRepository.save(tgOperation);

        return SendMessage
                .builder()
                .chatId(chatId)
                .text("Введите Фамимилию и инициалы кому адресован документ")
                .build();
    }

    @Override
    public TgOperationStatus getHandleStatus() {
        return WAIT_TYPE;
    }
}


