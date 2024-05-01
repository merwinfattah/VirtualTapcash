package org.example.virtualtapcash.repository;

import org.example.virtualtapcash.model.ExternalSystemCard;
import org.example.virtualtapcash.model.TapcashCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExternalSystemCardJpaRepository extends JpaRepository<ExternalSystemCard, String> {
    Optional<ExternalSystemCard> findTapcashCardsByCardId(String cardId);

    Optional<ExternalSystemCard> findTapcashCardsByRfid(String rfid);

    Optional<ExternalSystemCard> findCardIdByRfid(String rfid);
}