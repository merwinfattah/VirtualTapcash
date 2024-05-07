package org.example.virtualtapcash.controller;


import org.example.virtualtapcash.service.QrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/qr")
public class QrController {

    @Autowired
    private QrService qrService;

    @PostMapping("/activate/{cardId}")
    public ResponseEntity<String> activateQrCode(@PathVariable String cardId) {
        boolean isCreatedOrUpdated = qrService.createIsActiveByCardId(cardId);
        if (isCreatedOrUpdated) {
            return ResponseEntity.ok("QR Code activated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("QR Code is already active.");
        }
    }


    @PostMapping("/deactivate/{cardId}")
    public ResponseEntity<String> deactivateQrCodeImmediately(@PathVariable String cardId) {
        boolean isDeactivated = qrService.deactivateQrCodeByCardIdImmediately(cardId);
        if (isDeactivated) {
            return ResponseEntity.ok("QR Code deactivated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("QR Code not found or already inactive.");
        }
    }
}
