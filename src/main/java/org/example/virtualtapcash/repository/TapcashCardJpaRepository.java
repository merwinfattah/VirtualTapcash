package org.example.virtualtapcash.repository;

import org.example.virtualtapcash.models.ExternalSystemCard;
import org.example.virtualtapcash.models.TapcashCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TapcashCardJpaRepository extends JpaRepository<TapcashCard, String> {


    @Query(value = "SELECT * FROM tb_tapcash_card WHERE virtual_tapcash_id = ?1", nativeQuery = true)
    List<TapcashCard> findTapcashCardsByVirtualTapcashId(String virtualTapcashId);

    @Query(value = "SELECT * FROM tb_tapcash_card WHERE is_default = true", nativeQuery = true)
    Boolean isThereCardSetToDefault();

    @Query(value = "SELECT COUNT(*) FROM tb_tapcash_card WHERE card_id = ?1", nativeQuery = true)
    Boolean isCardAlreadyRegistered(String cardId);

}
