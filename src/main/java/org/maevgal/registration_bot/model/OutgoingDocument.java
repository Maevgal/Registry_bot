package org.maevgal.registration_bot.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Table(name = "outgoing_document")
public class OutgoingDocument {
    @Id
    private Long id;
    private String number;
    private LocalDate createDocumentDate;
    private String recipient;
    private String sender;
    private String description;
    private String copyFile;
    private LocalDate create_at;
    private LocalDate update_at;
}
