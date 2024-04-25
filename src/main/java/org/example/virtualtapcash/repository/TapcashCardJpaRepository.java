package org.example.virtualtapcash.repository;

import jakarta.transaction.Transactional;
import org.example.virtualtapcash.models.ExternalSystemCard;
import org.example.virtualtapcash.models.TapcashCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TapcashCardJpaRepository extends JpaRepository<TapcashCard, String> {

    Optional <TapcashCard> findTapcashCardsByRfid(String rfid);

    @Query(value = "SELECT * FROM tb_tapcash_card WHERE virtual_tapcash_id = ?1", nativeQuery = true)
    List<TapcashCard> findTapcashCardsByVirtualTapcashId(String virtualTapcashId);

    @Query(value = "SELECT * FROM tb_tapcash_card WHERE is_default = true", nativeQuery = true)
    Boolean isThereCardSetToDefault();

    @Query(value = "SELECT COUNT(*) FROM tb_tapcash_card WHERE card_id = ?1", nativeQuery = true)
    Boolean isCardAlreadyRegistered(String cardId);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM tb_tapcash_card WHERE card_id = ?1 AND status = 'Active'", nativeQuery = true)
    boolean isCardActive(String cardId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tb_tapcash_card SET status = ?1, cardName = ?2 WHERE cardId = ?3",nativeQuery = true)
    void updateTapcashCardStatusAndName(String newStatus, String newCardName, String cardId);


}

