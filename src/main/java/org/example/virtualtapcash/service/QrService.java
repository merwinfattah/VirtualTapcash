package org.example.virtualtapcash.service;

import jakarta.transaction.Transactional;
import org.example.virtualtapcash.dto.general.response.ApiResponseDto;
import org.example.virtualtapcash.exception.card.CardNotFoundException;
import org.example.virtualtapcash.exception.qr.QrAlreadyActivatedException;
import org.example.virtualtapcash.model.QR;
import org.example.virtualtapcash.model.TapcashCard;
import org.example.virtualtapcash.repository.QrJpaRepository;
import org.example.virtualtapcash.repository.TapcashCardJpaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class QrService {
    private final QrJpaRepository qrJpaRepository;

    private final TapcashCardJpaRepository tapcashCardRepository;

    public QrService(QrJpaRepository qrGeneratedRepository, TapcashCardJpaRepository tapcashCardRepository) {
        this.qrJpaRepository = qrGeneratedRepository;
        this.tapcashCardRepository = tapcashCardRepository;
    }

    @Transactional
    public ApiResponseDto createIsActiveByCardId(String cardId) throws CardNotFoundException, QrAlreadyActivatedException {
        Optional<TapcashCard> cardOptional = tapcashCardRepository.findTapcashCardsByCardId(cardId);

        if (!cardOptional.isPresent()) {
            // Card not found, return false or handle the error appropriately
            throw new CardNotFoundException("Card not Found");
        }

        TapcashCard card = cardOptional.get();

        QR existingQr = qrJpaRepository.findByCard(card);
        if (existingQr != null) {
            if (existingQr.getIsActive() && existingQr.getActivationTime().plusMinutes(1).isAfter(LocalDateTime.now())) {
                // QR code is already active within 1 minute, don't create a new one
                throw new QrAlreadyActivatedException("QR code is already active within 1 minute, don't create a new one");
            } else {
                // QR code is inactive, update the existing record
                existingQr.setIsActive(true);
                existingQr.setActivationTime(LocalDateTime.now());
                qrJpaRepository.save(existingQr);
                return new ApiResponseDto("success", true, "Qr code activated");
            }
        }

        // Create a new QR code entry if none exists
        QR newQr = new QR();
        newQr.setCard(card); // Set the TapcashCard object
        newQr.setIsActive(true);
        newQr.setActivationTime(LocalDateTime.now());

        // Save the new QR code to the repository
        qrJpaRepository.save(newQr);
        return new ApiResponseDto("success", true, "Qr code is created and activated");
    }

    public ApiResponseDto checkIsActiveByCardId(String cardId) {
        QR qr = qrJpaRepository.findActiveQRCodeByCardId(cardId);
        String message;
        Boolean isActive = qr != null && qr.getIsActive() && qr.getActivationTime().plusMinutes(1).isAfter(LocalDateTime.now());
        if (isActive) {
            message = "Qr is active";
        } else {
            message = "Qr is inactive";
        }
        return new ApiResponseDto("success", isActive, message);
    }

    @Transactional
    public boolean activateQrCodeByCardId(String cardId) {
        List<QR> inactiveOrNullQRCodes = qrJpaRepository.findInactiveOrNullQRCodesByCardId(cardId);
        if (!inactiveOrNullQRCodes.isEmpty()) {
            QR qr = inactiveOrNullQRCodes.get(0); // Assuming you activate the first one found
            qr.setIsActive(true);
            qr.setActivationTime(LocalDateTime.now());
            qrJpaRepository.save(qr);
            return true;
        }
        return false;
    }

    @Scheduled(fixedRate = 60000) // Every 30 seconds
    public void deactivateExpiredQRCodes() {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        List<QR> expiredQRCodes = qrJpaRepository.findExpiredQRCodes(oneMinuteAgo);
        for (QR qr : expiredQRCodes) {
            qr.setIsActive(false);
            qrJpaRepository.save(qr);
        }
    }


    @Transactional
    public void deactivateQrCodeByCardId(String cardId) {
        QR qr = qrJpaRepository.findActiveQRCodeByCardId(cardId);
        if (qr != null && qr.getIsActive()) {
            qr.setIsActive(false);
            qrJpaRepository.save(qr);
        }
    }

    public boolean checkIsActive(long qrId) {
        QR qr = qrJpaRepository.findById(qrId).orElseThrow(() -> new RuntimeException("QR Code not found"));
        return qr.getIsActive() && qr.getActivationTime().plusMinutes(1).isAfter(LocalDateTime.now());
    }

    @Transactional
    public void deactivateQrCodesForUser(String username) {
        // Assuming that each QR code is linked to a user (possibly via a card)
        List<QR> userQRCodes = qrJpaRepository.findByUser(username); // Modify this method based on your model
        for (QR qr : userQRCodes) {
            qr.setIsActive(false);
            qrJpaRepository.save(qr);
        }
    }

    @Transactional
    public boolean deactivateQrCodeByCardIdImmediately(String cardId) {
        QR qr = qrJpaRepository.findActiveQRCodeByCardId(cardId);
        if (qr != null && qr.getIsActive()) {
            qr.setIsActive(false);
            qrJpaRepository.save(qr);
            // Set up a timer to reactivate it after 1 minute
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    deactivateQrCodeByCardId(cardId);
                }
            }, 60000); // 60 seconds
            return true;
        }
        return false;
    }
}
