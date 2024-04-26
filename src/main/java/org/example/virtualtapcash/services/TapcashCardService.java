package org.example.virtualtapcash.services;

import org.example.virtualtapcash.models.ExternalSystemCard;
import org.example.virtualtapcash.models.MBankingAccount;
import org.example.virtualtapcash.models.TapcashCard;
import org.example.virtualtapcash.repository.TapcashCardJpaRepository;
import org.example.virtualtapcash.repository.UserJpaRepository;
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
    UserJpaRepository userJpaRepository;

    public ResponseEntity<?> registerCard(String cardId, String virtualTapcashId) {

            if (tapcashCardJpaRepository.isCardAlreadyRegistered(cardId) && tapcashCardJpaRepository.isCardActive(cardId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Card with ID " + cardId + " is already registered.");
            }

            if (tapcashCardJpaRepository.isCardAlreadyRegistered(cardId) && !tapcashCardJpaRepository.isCardActive(cardId)) {

                List<TapcashCard> cardList = tapcashCardJpaRepository.findAllByOrderByCardNameAsc();

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

                tapcashCardJpaRepository.updateTapcashCardStatusAndName("Active", cardName, cardId);

                String message = "Card Successfully Registered";

                return ResponseEntity.status(HttpStatus.CREATED).body(message);

            }


            Optional<ExternalSystemCard> relatedCard = externalSystemCardService.getCardById(cardId);

            if (relatedCard.isPresent()) {

                TapcashCard newCard = new TapcashCard();

                if (tapcashCardJpaRepository.isThereCardSetToDefault()) {
                    newCard.setIsDefault(false);
                } else {
                    newCard.setIsDefault(true);
                }

                Optional<MBankingAccount> user = userJpaRepository.findMBankingAccountByVirtualTapCashId(virtualTapcashId);

                List<TapcashCard> cardList = tapcashCardJpaRepository.findAllByOrderByCardNameAsc();

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



    public ResponseEntity<?> getAllCard(String virtualTapCashId) {

        List<TapcashCard> cardsData = tapcashCardJpaRepository.findTapcashCardsByVirtualTapcashId(virtualTapCashId, "Active");
        if (!cardsData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(cardsData);
        } else {
            String stringMessage = "No Cards Found for Virtual Tapcash ID: " + virtualTapCashId;
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(stringMessage);
        }

    }


    public ResponseEntity<?> updateCard(String rfid) {
        Optional <TapcashCard> card = tapcashCardJpaRepository.findTapcashCardsByRfid(rfid);
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

}
