package org.maevgal.registration_bot.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.maevgal.registration_bot.model.TgOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.maevgal.registration_bot.constant.TgOperationStatus.WAIT_TYPE;

@DataJpaTest
public class TgOperationRepositoryTest {
    @Autowired
    TgOperationRepository tgOperationRepository;

    @Test
    public void findByChatId() {
        long chatId = 1032086687L;

        TgOperation tgOperation = new TgOperation();
        tgOperation.setChatId(chatId);
        tgOperation.setStatus(WAIT_TYPE);
        tgOperationRepository.save(tgOperation);

        Assertions.assertThat(tgOperationRepository.findByChatId(chatId).get())
                .isEqualTo(tgOperation);
    }

}