package org.maevgal.registration_bot.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.maevgal.registration_bot.constant.DocumentType;
import org.maevgal.registration_bot.constant.TgOperationStatus;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Accessors(chain = true)
@Data
@Table(name = "tg_operation")
public class TgOperation {
    @Id
    private Long id;
    private Long chatId;
    private Long outgoingDocumentId;
    private DocumentType documentType;
    private TgOperationStatus status;
    private String recipient;
    private String sender;
    private String description;
    private String copyFile;
    private LocalDate createDocumentDate;
    private LocalDate create_at;
    private LocalDate update_at;
}
