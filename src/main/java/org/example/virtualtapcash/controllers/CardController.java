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

    @Autowired
    ExternalSystemCardService externalSystemCardService;

    @Autowired
    TapcashCardJpaRepository tapcashCardJpaRepository;

    @Autowired
    UserJpaRepository userJpaRepository;


    @GetMapping ("/get-cards-data/{virtualTapCashId}")
    public ResponseEntity<?> getCardsData(@PathVariable String virtualTapCashId) {
        try {
            List<TapcashCard> cardsData = tapcashCardJpaRepository.findTapcashCardsByVirtualTapcashId(virtualTapCashId);
            if (!cardsData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(cardsData);
            } else {
                String stringMessage = "No Cards Found for Virtual Tapcash ID: " + virtualTapCashId;
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(stringMessage);
            }
        } catch (Exception e) {
            String errorMessage = "An error occurred while retrieving card data.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }

    }

    @PostMapping ("/add-card")
    public ResponseEntity<?> addCard(@RequestBody AddCardRequest request) {
        try {
            if (request.getCardId().isBlank()) {
                return ResponseEntity.badRequest().body("Card ID cannot be empty.");
            }
            if (request.getVirtualTapcashId().isBlank()) {
                return ResponseEntity.badRequest().body("Virtual Tapcash ID cannot be empty.");
            }

            if (tapcashCardJpaRepository.isCardAlreadyRegistered(request.getCardId()) && tapcashCardJpaRepository.isCardActive(request.getCardId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Card with ID " + request.getCardId() + " is already registered.");
            }

            if (tapcashCardJpaRepository.isCardAlreadyRegistered(request.getCardId()) && !tapcashCardJpaRepository.isCardActive(request.getCardId())) {

                List<TapcashCard> cardList = tapcashCardService.getAllCard();

                cardList.sort(Comparator.comparing(TapcashCard::getCardName));

                String cardName = null;

                int order = 1;


                for (int i = 0; i < cardList.size(); i++) {
                    TapcashCard tempCard = cardList.get(i);

                    if (tempCard.getCardName() == null || tempCard.getCardName().isEmpty()) {
                        continue;
                    }

                    if (!("Virtual Tapcash " + order).equals(tempCard.getCardName())) {
                        cardName = "Virtual Tapcash " + order;
                        break;
                    }

                    order++;
                }

                if (cardName == null) {
                    cardName = "Virtual Tapcash " + order;
                }

                tapcashCardJpaRepository.updateTapcashCardStatusAndName(request.getCardId(), cardName, "Active");

                String message = "Card Successfully Registered";

                return ResponseEntity.status(HttpStatus.CREATED).body("Card successfully registered.");

            }

            TapcashCard newCard = new TapcashCard();

            if (tapcashCardJpaRepository.isThereCardSetToDefault()) {
                newCard.setIsDefault(false);
            } else {
                newCard.setIsDefault(true);
            }

            Optional<ExternalSystemCard> relatedCard = externalSystemCardService.getCardById(request.getCardId());

            Optional<MBankingAccount> user = userJpaRepository.findMBankingAccountByVirtualTapCashId(request.getVirtualTapcashId());

            List<TapcashCard> cardList = tapcashCardService.getAllCard();

            cardList.sort(Comparator.comparing(TapcashCard::getCardName));

            String cardName = null;

            int order = 1;

            for (int i = 0; i < cardList.size(); i++) {
                TapcashCard tempCard = cardList.get(i);


                if (tempCard.getCardName() == null || tempCard.getCardName().isEmpty()) {
                    continue;
                }

                if (!("Virtual Tapcash " + order).equals(tempCard.getCardName())) {
                    cardName = "Virtual Tapcash " + order;
                    break;
                }

                order++;
            }

            if (cardName == null) {
                cardName = "Virtual Tapcash " + order;
            }

            newCard.setCardId(request.getCardId());
            newCard.setRegisteredAt(new Date());
            newCard.setUpdatedAt(new Date());
            newCard.setRfid(relatedCard.get().getRfid());
            newCard.setTapCashBalance(relatedCard.get().getTapCashBalance());
            newCard.setStatus("Active");
            newCard.setUser(user.get());
            newCard.setCardName(cardName);
            tapcashCardService.registerCard(newCard);

            return ResponseEntity.status(HttpStatus.CREATED).body(newCard);
        } catch (Exception e) {
            String errorMessage = "An error occurred while processing the request.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }

    }

    @PatchMapping
    public ResponseEntity<?> removeCards(@PathVariable String rfid) {
        try {
            Optional <TapcashCard> card = tapcashCardJpaRepository.findTapcashCardsByRfid(rfid);
            TapcashCard updatedCard = card.get();

            updatedCard.setStatus("Inactive");
            updatedCard.setCardName("");
            updatedCard.setIsDefault(false);

            tapcashCardService.updateCard(updatedCard);

            List<TapcashCard> cardList = tapcashCardService.getAllCard();
            cardList.sort(Comparator.comparing(TapcashCard::getCardName));
            for (int i = 0; i < cardList.size(); i++) {
                TapcashCard tempCard = cardList.get(i);
                if (!tempCard.getStatus().equals("Inactive")) {
                    tempCard.setIsDefault(true);
                    tapcashCardService.updateCard(tempCard);
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body("Card Successfully Removed!");

        } catch (Exception e) {
            String errorMessage = "An error occurred while processing the request.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }


    }






}
