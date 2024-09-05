package org.maevgal.registration_bot.repository;

import org.maevgal.registration_bot.model.OutgoingDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OutgoingDocumentRepository extends CrudRepository<OutgoingDocument, Long> {
}
