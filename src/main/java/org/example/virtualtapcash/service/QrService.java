package org.example.virtualtapcash.service;

import jakarta.transaction.Transactional;
import org.example.virtualtapcash.model.QR;
import org.example.virtualtapcash.repository.QrJpaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class QrService {
    private final QrJpaRepository qrJpaRepository;

    public QrService(QrJpaRepository qrGeneratedRepository) {
        this.qrJpaRepository = qrGeneratedRepository;
    }

    public boolean checkIsActiveByCardId(String cardId) {
        QR qr = qrJpaRepository.findActiveQRCodeByCardId(cardId);
        return qr != null && qr.getIsActive() && qr.getActivationTime().plusMinutes(1).isAfter(LocalDateTime.now());
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
