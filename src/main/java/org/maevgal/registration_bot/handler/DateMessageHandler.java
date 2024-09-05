package org.maevgal.registration_bot.handler;

import lombok.RequiredArgsConstructor;
import org.maevgal.registration_bot.constant.DocumentType;
import org.maevgal.registration_bot.constant.TgOperationStatus;
import org.maevgal.registration_bot.model.TgOperation;
import org.maevgal.registration_bot.repository.TgOperationRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.maevgal.registration_bot.constant.TgOperationStatus.WAIT_CONFIRMATION;
import static org.maevgal.registration_bot.constant.TgOperationStatus.WAIT_DATE;

@RequiredArgsConstructor
@Service
public class DateMessageHandler implements MessageHandler {
    private final TgOperationRepository tgOperationRepository;

    public SendMessage handleMessage(Update update, TgOperation tgOperation, long chatId) {
        String receivedMessage = update.getMessage().getText();

        LocalDate localDate;
        try {
            DateTimeFormatter formatter
                    = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            localDate = LocalDate.parse(receivedMessage, formatter);
        } catch (DateTimeParseException e) {
            return SendMessage
                    .builder()
                    .chatId(chatId)
                    .text("Вы ввели неверный формат даты. Введите дату в формате 01.01.2024")
                    .build();
        }

        tgOperation.setStatus(WAIT_CONFIRMATION);
        tgOperation.setCreateDocumentDate(localDate);
        tgOperation.setUpdate_at(LocalDate.now());
        tgOperationRepository.save(tgOperation);

        return SendMessage
                .builder()
                .chatId(chatId)
                .text("Подтвердите что хотите зарегистрировать сообщение")
                .replyMarkup(InlineKeyboardMarkup
                        .builder()
                        .keyboardRow(
                                new InlineKeyboardRow(InlineKeyboardButton
                                        .builder()
                                        .text("Да")
                                        .callbackData("Yes")
                                        .build()
                                )
                        )
                        .keyboardRow(new InlineKeyboardRow(InlineKeyboardButton
                                .builder()
                                .text("Нет")
                                .callbackData("No")
                                .build()))
                        .build())
                .build();

    }

    @Override
    public TgOperationStatus getHandleStatus() {
        return WAIT_DATE;
    }


}
