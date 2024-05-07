package org.example.virtualtapcash.repository;

import jakarta.transaction.Transactional;
import org.example.virtualtapcash.model.ExternalSystemCard;
import org.example.virtualtapcash.model.TapcashCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface TapcashCardJpaRepository extends JpaRepository<TapcashCard, String> {

    Optional <TapcashCard> findTapcashCardsByRfid(String rfid);

    List <TapcashCard> findAllByOrderByCardNameAsc();

    Optional<TapcashCard> findTapcashCardsByCardId(String cardId);

    @Query(value = "SELECT * FROM tb_tapcash_card WHERE virtual_tapcash_id = ?1 AND is_default = false AND status = 'Active' ORDER BY updated_at DESC", nativeQuery = true)
    List<TapcashCard> changeIsDefault(String virtualTapcashId);

    @Query(value = "SELECT * FROM tb_tapcash_card WHERE virtual_tapcash_id = ?1 AND is_default = true AND card_id != ?2", nativeQuery = true)
    List<TapcashCard> changeDefaultCard(String virtualTapcashId, String cardId);

    @Query(value = "SELECT * FROM tb_tapcash_card WHERE virtual_tapcash_id = ?1 AND is_default = true AND status = 'Active' ORDER BY updated_at DESC", nativeQuery = true)
    List<TapcashCard> removeIsDefault(String virtualTapcashId);

    @Query(value = "SELECT * FROM tb_tapcash_card WHERE virtual_tapcash_id = ?1 AND status = ?2", nativeQuery = true)
    List<TapcashCard> findTapcashCardsByVirtualTapcashId(String virtualTapcashId, String status);

    @Query(value = "SELECT * FROM tb_tapcash_card WHERE virtual_tapcash_id = ?1 ORDER BY card_name ASC", nativeQuery = true)
    List<TapcashCard> findTapcashCardsByVirtualTapcashIdOrderByCardNameAsc(String virtualTapcashId);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM tb_tapcash_card WHERE is_default = true AND virtual_tapcash_id = ?1", nativeQuery = true)
    boolean isThereCardSetToDefaultByVirtualTapcashId(String virtualTapcashId);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM tb_tapcash_card WHERE card_id = ?1", nativeQuery = true)
    boolean isCardAlreadyRegistered(String cardId);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM tb_tapcash_card WHERE rfid = ?1", nativeQuery = true)
    boolean isCardAlreadyRegisteredByRfid(String rfid);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM tb_tapcash_card WHERE rfid = ?1 AND status = 'Active'", nativeQuery = true)
    boolean isCardActiveByRfid(String rfid);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM tb_tapcash_card WHERE card_id = ?1 AND status = 'Active'", nativeQuery = true)
    boolean isCardActive(String cardId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tb_tapcash_card SET status = ?1, card_name = ?2, is_default = ?4, virtual_tapcash_id = ?5 WHERE card_id = ?3",nativeQuery = true)
    void updateTapcashCardStatusAndName(String newStatus, String newCardName, String cardId, Boolean isDefault, String virtualTapcashId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tb_tapcash_card SET status = ?1, card_name = ?2, is_default = ?4, virtual_tapcash_id = ?5 WHERE rfid = ?3",nativeQuery = true)
    void updateTapcashCardStatusAndNameByRfid(String newStatus, String newCardName, String rfid, Boolean is_default, String virtualTapcashId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tb_tapcash_card SET is_default = false WHERE virtual_tapcash_id = ?1 AND is_default = true AND status = 'Active'", nativeQuery = true)
    void updateIsDefaultToFalse(String virtualTapcashId);
}

