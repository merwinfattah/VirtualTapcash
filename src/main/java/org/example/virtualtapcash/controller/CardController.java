package org.example.virtualtapcash.controller;

import org.example.virtualtapcash.dto.card.request.AddCardDto;
import org.example.virtualtapcash.service.TapcashCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@CrossOrigin
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

    @GetMapping("/get-card/{cardId}")
    public ResponseEntity<?> getOneCard(@PathVariable String cardId) {
        try {
            return tapcashCardService.getOneCard(cardId);
        } catch (Exception e) {
            String errorMessage = "An error occurred while retrieving card data.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PostMapping ("/add-card")
    public ResponseEntity<?> addCard(@RequestBody AddCardDto request) {
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
