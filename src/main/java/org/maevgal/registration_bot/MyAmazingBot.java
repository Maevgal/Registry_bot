package org.maevgal.registration_bot;

import lombok.RequiredArgsConstructor;
import org.maevgal.registration_bot.configuration.BotProperties;
import org.maevgal.registration_bot.constant.DocumentType;
import org.maevgal.registration_bot.model.TgOperation;
import org.maevgal.registration_bot.repository.OutgoingDocumentRepository;
import org.maevgal.registration_bot.repository.TgOperationRepository;
import org.maevgal.registration_bot.service.HandleMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.LocalDate;
import java.util.Optional;

import static org.maevgal.registration_bot.constant.TgOperationStatus.WAIT_DOCUMENT;
import static org.maevgal.registration_bot.constant.TgOperationStatus.WAIT_TYPE;

@RequiredArgsConstructor
@Component
public class MyAmazingBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private static final Logger log = LoggerFactory.getLogger(MyAmazingBot.class);
    private final TelegramClient telegramClient;
    private final TgOperationRepository tgOperationRepository;
    private final HandleMessageService handleMessageService;
    private final BotProperties properties;

    @Override
    public String getBotToken() {
        return properties.getToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            final long chatId = update.getMessage().getChatId();
            SendMessage sendMessage = null;
            if (update.getMessage().hasText() && update.getMessage().getText().startsWith("/")) {
                sendMessage = handleCommand(update, chatId);
            } else {
                sendMessage = handleMessageService.handle(update, chatId);
            }
            try {
                telegramClient.execute(sendMessage); // Sending our sendMessage object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            final long chatId = update.getCallbackQuery().getMessage().getChatId();
            SendMessage sendMessage = handleMessageService.handle(update, chatId);
            try {
                telegramClient.execute(sendMessage); // Sending our sendMessage object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private SendMessage handleCommand(Update update, long chatId) {
        SendMessage message;
        if (update.getMessage().getText().equals("/start")) {

            Optional<TgOperation> p = tgOperationRepository.findByChatId(chatId);

            if (p.isPresent() && !p.get().getStatus().equals(WAIT_DOCUMENT)) {
                tgOperationRepository.delete(p.get());
            } else if (p.isPresent() && p.get().getStatus().equals(WAIT_DOCUMENT)) {
                return SendMessage // Create a message object
                        .builder()
                        .chatId(chatId)
                        .text("Не можем начать новую регистрацию документа. Жду копию файла по документу")
                        .build();
            }

            TgOperation tgOperation = new TgOperation();

            tgOperation.setStatus(WAIT_TYPE);
            tgOperation.setChatId(chatId);
            tgOperation.setCreate_at(LocalDate.now());
            tgOperation.setUpdate_at(LocalDate.now());
            tgOperationRepository.save(tgOperation);


            message = SendMessage // Create a message object
                    .builder()
                    .chatId(chatId)
                    .text("Выберите тип документа")
                    .replyMarkup(InlineKeyboardMarkup
                            .builder()
                            .keyboardRow(
                                    new InlineKeyboardRow(InlineKeyboardButton
                                            .builder()
                                            .text("Служебная записка")
                                            .callbackData(DocumentType.OFFICE_MEMO.name())
                                            .build()
                                    )
                            )
                            .keyboardRow(new InlineKeyboardRow(InlineKeyboardButton
                                    .builder()
                                    .text("Письмо")
                                    .callbackData(DocumentType.LETTER.name())
                                    .build()))
                            .build())
                    .build();
        } else {
            message = SendMessage // Create a message object
                    .builder()
                    .chatId(chatId)
                    .text("Я не знаю такой команды")
                    .build();
        }
        return message;
    }
}
