package org.maevgal.registration_bot.handler;

import lombok.RequiredArgsConstructor;
import org.maevgal.registration_bot.constant.DocumentType;
import org.maevgal.registration_bot.constant.TgOperationStatus;
import org.maevgal.registration_bot.model.OutgoingDocument;
import org.maevgal.registration_bot.model.TgOperation;
import org.maevgal.registration_bot.repository.OutgoingDocumentRepository;
import org.maevgal.registration_bot.repository.TgOperationRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;

import static org.maevgal.registration_bot.constant.TgOperationStatus.WAIT_CONFIRMATION;
import static org.maevgal.registration_bot.constant.TgOperationStatus.WAIT_DOCUMENT;

@RequiredArgsConstructor
@Service
public class ConfirmationMessageHandler implements MessageHandler {
    private final TgOperationRepository tgOperationRepository;
    private final OutgoingDocumentRepository outgoingDocumentRepository;

    public SendMessage handleMessage(Update update, TgOperation tgOperation, long chatId) {
        String receivedMessage = update.getCallbackQuery().getData();
        OutgoingDocument outgoingDocument = new OutgoingDocument();
        //Если нет, то куда?)
        if (receivedMessage.equals("No")) {

            return SendMessage
                    .builder()
                    .chatId(chatId)
                    .text("Регистрация документа остановлена. Начните регистрацию заново, используя команду /start.")
                    .build();
        } else {


            outgoingDocument.setCreateDocumentDate(tgOperation.getCreateDocumentDate());
            outgoingDocument.setSender(tgOperation.getSender());
            outgoingDocument.setRecipient(tgOperation.getRecipient());
            outgoingDocument.setDescription(tgOperation.getDescription());
            outgoingDocument.setUpdate_at(LocalDate.now());
            outgoingDocument.setCreate_at(LocalDate.now());
            outgoingDocumentRepository.save(outgoingDocument);

            tgOperation.setStatus(WAIT_DOCUMENT);
            tgOperation.setOutgoingDocumentId(outgoingDocument.getId());
            tgOperationRepository.save(tgOperation);

            if (tgOperation.getDocumentType().equals(DocumentType.OFFICE_MEMO)) {
                outgoingDocument.setNumber(String.valueOf(outgoingDocument.getId()));
            } else if (tgOperation.getDocumentType().equals(DocumentType.LETTER)) {

                outgoingDocument.setNumber("359-" + String.valueOf(outgoingDocument.getId()));
            }
            outgoingDocumentRepository.save(outgoingDocument);

            return SendMessage
                    .builder()
                    .chatId(chatId)
                    .text("Документ под номером " + outgoingDocument.getNumber()
                            + " и датой " + outgoingDocument.getCreateDocumentDate()
                            + " зарегистрирован, прикрепите скан документа")
                    .build();
        }
    }

    @Override
    public TgOperationStatus getHandleStatus() {
        return WAIT_CONFIRMATION;
    }
}
