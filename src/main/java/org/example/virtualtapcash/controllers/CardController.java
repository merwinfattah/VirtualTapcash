package org.example.virtualtapcash.controllers;

import org.example.virtualtapcash.entities.AddCardRequest;
import org.example.virtualtapcash.models.ExternalSystemCard;
import org.example.virtualtapcash.models.MBankingAccount;
import org.example.virtualtapcash.models.TapcashCard;
import org.example.virtualtapcash.repository.ExternalSystemCardJpaRepository;
import org.example.virtualtapcash.repository.TapcashCardJpaRepository;
import org.example.virtualtapcash.repository.UserJpaRepository;
import org.example.virtualtapcash.services.ExternalSystemCardService;
import org.example.virtualtapcash.services.MBankingService;
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
    public ResponseEntity<List<TapcashCard>> getCardsData(@PathVariable String virtualTapCashId) {
        List<TapcashCard> cardsData = tapcashCardJpaRepository.findTapcashCardsByVirtualTapcashId(virtualTapCashId);
        if (!cardsData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(cardsData);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping ("/add-card")
    public ResponseEntity<?> addCard(@RequestBody AddCardRequest request) {
        if (request.getCardId().isBlank()) {
            String message = "Card ID can not empty";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if (request.getVirtualTapcashId().isBlank()) {
            String message = "Virtual Tapcash ID can not empty";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }

        if (tapcashCardJpaRepository.isCardAlreadyRegistered(request.getCardId())) {
            String message = "Card with ID " + request.getCardId() + " is already registered.";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }

        Optional<ExternalSystemCard> card = externalSystemCardService.getCardById(request.getCardId());

        Optional<MBankingAccount> user = userJpaRepository.findMBankingAccountByVirtualTapCashId(request.getVirtualTapcashId());

        List<TapcashCard> cardList = tapcashCardService.getAllCard();

        cardList.sort(Comparator.comparing(TapcashCard::getCardId));

        String cardName;

        for (int i =0; i < cardList.size() ; i++) {
            TapcashCard tempCard = cardList.get(i);
            if (tempCard.)
        }

        TapcashCard newCard = new TapcashCard();


        newCard.setCardId(request.getCardId());
        if (tapcashCardJpaRepository.isThereCardSetToDefault()) {
            newCard.setIsDefault(false);
        } else {
            newCard.setIsDefault(true);
        }
        newCard.setRegisteredAt(new Date());
        newCard.setUpdatedAt(new Date());
        newCard.setRfid(card.get().getRfid());
        newCard.setTapCashBalance(card.get().getTapCashBalance());
        newCard.setStatus("Active");
        newCard.setUser(user.get());




    }


}
