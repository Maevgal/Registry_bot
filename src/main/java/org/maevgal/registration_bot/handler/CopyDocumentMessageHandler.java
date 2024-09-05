package org.maevgal.registration_bot.handler;

import lombok.RequiredArgsConstructor;
import org.maevgal.registration_bot.constant.TgOperationStatus;
import org.maevgal.registration_bot.model.OutgoingDocument;
import org.maevgal.registration_bot.model.TgOperation;
import org.maevgal.registration_bot.repository.OutgoingDocumentRepository;
import org.maevgal.registration_bot.repository.TgOperationRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import static org.maevgal.registration_bot.constant.TgOperationStatus.CONFIRM;
import static org.maevgal.registration_bot.constant.TgOperationStatus.WAIT_DOCUMENT;

@RequiredArgsConstructor
@Service
public class CopyDocumentMessageHandler implements MessageHandler {
    private final OutgoingDocumentRepository outgoingDocumentRepository;
    private final TgOperationRepository tgOperationRepository;
    private final TelegramClient telegramClient;

    @Override
    public SendMessage handleMessage(Update update, TgOperation tgOperation, long chatId) {
        if (!update.getMessage().hasDocument()) {
            return null;
        }

        Optional<OutgoingDocument> od = outgoingDocumentRepository.findById(tgOperation.getOutgoingDocumentId());
        if (od.isEmpty()) {
            return null;
        }
        OutgoingDocument outgoingDocument = od.get();

        Document document = update.getMessage().getDocument();
        String filePath = getFilePath(document);
        String pathname = "/home/jane/Документы/%s.pdf".formatted(document.getFileSize());
        try {
            java.io.File file = telegramClient.downloadFile(filePath);

            java.io.File fileForSave = new java.io.File(pathname);
            fileForSave.createNewFile();
            FileOutputStream fw = new FileOutputStream(fileForSave);
            fw.write(new FileInputStream(file).readAllBytes());
            fw.flush();
        } catch (IOException | TelegramApiException e) {
            return null;
        }

        tgOperation.setStatus(CONFIRM);
        tgOperation.setCopyFile(pathname);
        outgoingDocument.setCopyFile(pathname);

        tgOperationRepository.save(tgOperation);
        outgoingDocumentRepository.save(outgoingDocument);

        return SendMessage
                .builder()
                .chatId(chatId)
                .text("Документ под номером " + outgoingDocument.getNumber()
                        + " и датой " + outgoingDocument.getCreateDocumentDate()
                        + " зарегистрирован. Приходите еще))")
                .build();
    }

    @Override
    public TgOperationStatus getHandleStatus() {
        return WAIT_DOCUMENT;
    }

    private String getFilePath(Document document) {
        GetFile getFile = GetFile.builder().fileId(document.getFileId()).build();
        try {
            File execute = telegramClient.execute(getFile);
            return execute.getFilePath();
        } catch (TelegramApiException e) {
            System.out.println("Zhopa");
            return null;
        }
    }
}
