package org.example.virtualtapcash.service;

import org.example.virtualtapcash.model.ExternalSystemCard;
import org.example.virtualtapcash.model.MBankingAccount;
import org.example.virtualtapcash.model.TapcashCard;
import org.example.virtualtapcash.repository.TapcashCardJpaRepository;
import org.example.virtualtapcash.repository.AccountJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TapcashCardService {

    @Autowired
    private TapcashCardJpaRepository tapcashCardJpaRepository;

    @Autowired
    ExternalSystemCardService externalSystemCardService;

    @Autowired
    AccountJpaRepository accountJpaRepository;

    public ResponseEntity<?> registerCard(String cardId, String virtualTapcashId) {

        if (tapcashCardJpaRepository.isCardAlreadyRegistered(cardId) && tapcashCardJpaRepository.isCardActive(cardId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Card with ID " + cardId + " is already registered.");
        }

        if (tapcashCardJpaRepository.isCardAlreadyRegistered(cardId) && !tapcashCardJpaRepository.isCardActive(cardId)) {

            String cardName = generateVirtualTapcashName(virtualTapcashId);

            tapcashCardJpaRepository.updateTapcashCardStatusAndName("Active", cardName, cardId);

            String message = "Card Successfully Registered";

            return ResponseEntity.status(HttpStatus.CREATED).body(message);

        }

        Optional<ExternalSystemCard> relatedCard = externalSystemCardService.getCardById(cardId);

        if (relatedCard.isPresent()) {

            TapcashCard newCard = new TapcashCard();

            if (tapcashCardJpaRepository.isThereCardSetToDefaultByVirtualTapcashId(virtualTapcashId)) {
                newCard.setIsDefault(false);
            } else {
                newCard.setIsDefault(true);
            }

            Optional<MBankingAccount> user = accountJpaRepository.findMBankingAccountByVirtualTapCashId(virtualTapcashId);

            String cardName = generateVirtualTapcashName(virtualTapcashId);

            newCard.setCardId(cardId);
            newCard.setRegisteredAt(new Date());
            newCard.setUpdatedAt(new Date());
            newCard.setRfid(relatedCard.get().getRfid());
            newCard.setTapCashBalance(relatedCard.get().getTapCashBalance());
            newCard.setStatus("Active");
            newCard.setUser(user.get());
            newCard.setCardName(cardName);
            tapcashCardJpaRepository.save(newCard);

            return ResponseEntity.status(HttpStatus.CREATED).body(newCard);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card Have Not  Registered Yet On BNI System");
        }


    }

    public ResponseEntity<?> registerCardV2(String rfid, String virtualTapcashId) {
        if (tapcashCardJpaRepository.isCardAlreadyRegisteredByRfid(rfid) && tapcashCardJpaRepository.isCardActiveByRfid(rfid)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Card with RFID " + rfid + " is already registered and active.");
        }

        if (tapcashCardJpaRepository.isCardAlreadyRegisteredByRfid(rfid) && !tapcashCardJpaRepository.isCardActiveByRfid(rfid)) {
            String cardName = generateVirtualTapcashName(virtualTapcashId);
            tapcashCardJpaRepository.updateTapcashCardStatusAndNameByRfid("Active", cardName, rfid);
            return ResponseEntity.status(HttpStatus.CREATED).body("Card Successfully Reactivated and Registered.");
        }

        Optional<ExternalSystemCard> relatedCard = externalSystemCardService.getCardByRfid(rfid);

        if (relatedCard.isPresent()) {

            TapcashCard newCard = new TapcashCard();

            if (tapcashCardJpaRepository.isThereCardSetToDefaultByVirtualTapcashId(virtualTapcashId)) {
                newCard.setIsDefault(false);
            } else {
                newCard.setIsDefault(true);
            }

            Optional<MBankingAccount> user = accountJpaRepository.findMBankingAccountByVirtualTapCashId(virtualTapcashId);

            String cardName = generateVirtualTapcashName(virtualTapcashId);

            newCard.setCardId(relatedCard.get().getCardId());
            newCard.setRegisteredAt(new Date());
            newCard.setUpdatedAt(new Date());
            newCard.setRfid(rfid);
            newCard.setTapCashBalance(relatedCard.get().getTapCashBalance());
            newCard.setStatus("Active");
            newCard.setUser(user.get());
            newCard.setCardName(cardName);
            tapcashCardJpaRepository.save(newCard);

            return ResponseEntity.status(HttpStatus.CREATED).body(newCard);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card Have Not  Registered Yet On BNI System");
        }
    }



    public ResponseEntity<?> getAllCard(String virtualTapCashId) {

        List<TapcashCard> cardsData = tapcashCardJpaRepository.findTapcashCardsByVirtualTapcashId(virtualTapCashId, "Active");
        if (!cardsData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(cardsData);
        } else {
            String stringMessage = "No Cards Found for Virtual Tapcash ID: " + virtualTapCashId;
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(stringMessage);
        }

    }

    public ResponseEntity<?> getOneCard(String cardId) {
        Optional <TapcashCard> card = tapcashCardJpaRepository.findTapcashCardsByCardId(cardId);
        if (card.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(card);
        } else {
            String stringMessage = "No Cards Found for  Tapcash ID: " + cardId;
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(stringMessage);
        }
    }


    public ResponseEntity<?> updateCard(String cardId) {
        Optional <TapcashCard> card = tapcashCardJpaRepository.findTapcashCardsByCardId(cardId);
        TapcashCard updatedCard = card.get();

        updatedCard.setStatus("Inactive");
        updatedCard.setCardName("");
        updatedCard.setIsDefault(false);

        tapcashCardJpaRepository.save(updatedCard);

        List<TapcashCard> cardList = tapcashCardJpaRepository.findAllByOrderByCardNameAsc();
        if (!cardList.isEmpty()) {
            for (TapcashCard tempCard : cardList) {
                if (tempCard.getStatus().equals("Active")) {
                    tempCard.setIsDefault(true);
                    tapcashCardJpaRepository.save(tempCard);
                    break;
                }
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body("Card Successfully Removed!");
    }

    private String generateVirtualTapcashName(String virtualTapcashId) {
        List<TapcashCard> cardList = tapcashCardJpaRepository.findTapcashCardsByVirtualTapcashIdOrderByCardNameAsc(virtualTapcashId);

        String cardName = "";

        int order = 1;

        for (TapcashCard tempCard : cardList) {

            if (!tempCard.getCardName().isBlank()) {
                if (!("Virtual Tapcash " + order).equals(tempCard.getCardName())) {
                    cardName = "Virtual Tapcash " + order;
                    break;
                }
                order++;
            }

        }

        if (cardName.equals("")) {
            cardName = "Virtual Tapcash " + order;
        }

        return cardName;
    }
}