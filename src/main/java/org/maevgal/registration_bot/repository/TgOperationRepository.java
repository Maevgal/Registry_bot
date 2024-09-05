package org.maevgal.registration_bot.repository;

import org.maevgal.registration_bot.constant.TgOperationStatus;
import org.maevgal.registration_bot.model.TgOperation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TgOperationRepository extends CrudRepository<TgOperation, Long> {
    Optional<TgOperation> findByChatId(long chatId);
    Optional<TgOperation> findByChatIdAndStatusNot(long chatId, TgOperationStatus status);

}
