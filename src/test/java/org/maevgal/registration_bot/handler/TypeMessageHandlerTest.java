package org.maevgal.registration_bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.maevgal.registration_bot.constant.DocumentType;
import org.maevgal.registration_bot.constant.TgOperationStatus;
import org.maevgal.registration_bot.model.TgOperation;
import org.maevgal.registration_bot.repository.TgOperationRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class TypeMessageHandlerTest {
    @Mock
    private TgOperationRepository tgOperationRepository;
    @InjectMocks
    private TypeMessageHandler typeMessageHandler;//внедрение моков в сервис


    @Test
    void handleMessage() {
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setData(DocumentType.LETTER.name());
        Update update = new Update();
        update.setCallbackQuery(callbackQuery);
        long chatId = 1032086687L;


        TgOperation expectedTgOperation = new TgOperation()
                .setStatus(TgOperationStatus.WAIT_RECIPIENT)
                .setDocumentType(DocumentType.LETTER)
                .setUpdate_at(LocalDate.now())
                .setChatId(chatId);

        SendMessage expectedMessage = SendMessage
                .builder()
                .chatId(chatId)
                .text("Введите Фамимилию и инициалы кому адресован документ")
                .build();
        //test
        SendMessage actualMessage = typeMessageHandler.handleMessage(update, expectedTgOperation, chatId);

        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);

        Mockito.verify(tgOperationRepository).save(expectedTgOperation);
    }

    @Test
    void getHandleStatus() {
    }
}