package org.example.virtualtapcash.controllers;

import org.example.virtualtapcash.entities.AddCardRequest;
import org.example.virtualtapcash.models.ExternalSystemCard;
import org.example.virtualtapcash.models.MBankingAccount;
import org.example.virtualtapcash.models.TapcashCard;
import org.example.virtualtapcash.repository.TapcashCardJpaRepository;
import org.example.virtualtapcash.repository.UserJpaRepository;
import org.example.virtualtapcash.services.ExternalSystemCardService;
import org.example.virtualtapcash.services.TapcashCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1/card")
public class CardController {

    @Autowired
    TapcashCardService tapcashCardService;

    @GetMapping ("/get-cards-data/{virtualTapCashId}")
    public ResponseEntity<?> getCardsData(@PathVariable String virtualTapCashId) {
        try {
            return tapcashCardService.getAllCard(virtualTapCashId);
        } catch (Exception e) {
            String errorMessage = "An error occurred while retrieving card data.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }

    }

    @PostMapping ("/add-card")
    public ResponseEntity<?> addCard(@RequestBody AddCardRequest request) {
        try {
            return tapcashCardService.registerCard(request.getCardId(), request.getVirtualTapcashId());
        } catch (Exception e) {
            String errorMessage = "An error occurred while processing the request.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }

    }

    @PatchMapping("/remove-card/{rfid}")
    public ResponseEntity<?> removeCards(@PathVariable String rfid) {
        try {
            return tapcashCardService.updateCard(rfid);
        } catch (Exception e) {
            String errorMessage = "An error occurred while processing the request.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }


    }






}
