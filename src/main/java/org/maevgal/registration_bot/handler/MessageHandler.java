package org.maevgal.registration_bot.handler;

import org.maevgal.registration_bot.constant.TgOperationStatus;
import org.maevgal.registration_bot.model.TgOperation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageHandler {

    SendMessage handleMessage(Update update, TgOperation tgOperation, long chatId);

    TgOperationStatus getHandleStatus();
}
