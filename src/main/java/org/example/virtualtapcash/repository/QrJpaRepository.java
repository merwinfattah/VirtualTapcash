package org.example.virtualtapcash.repository;

import org.example.virtualtapcash.model.QR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QrJpaRepository extends JpaRepository<QR, Long> {

    // Use a custom query to find inactive QR codes by card ID
    @Query("SELECT q FROM QR q WHERE q.card.cardId = :cardId AND q.isActive = false")
    List<QR> findInactiveQRCodesByCardId(@Param("cardId") String cardId);

    @Query("SELECT q FROM QR q WHERE q.card.cardId = :cardId AND q.isActive = true")
    QR findActiveQRCodeByCardId(@Param("cardId") String cardId);

    // Find active QR codes where activation time is before a given time
    @Query("SELECT q FROM QR q WHERE q.isActive = true AND q.activationTime < :time")
    List<QR> findExpiredQRCodes(@Param("time") LocalDateTime time);

    @Query("SELECT q FROM QR q WHERE q.card.cardId = :cardId AND (q.isActive IS NULL OR q.isActive = false)")
    List<QR> findInactiveOrNullQRCodesByCardId(@Param("cardId") String cardId);


}
