package org.example.virtualtapcash.service;

import org.example.virtualtapcash.dto.general.response.ApiResponseDto;
import org.example.virtualtapcash.model.ExternalSystemCard;
import org.example.virtualtapcash.model.MBankingAccount;
import org.example.virtualtapcash.model.TapcashCard;
import org.example.virtualtapcash.repository.TapcashCardJpaRepository;
import org.example.virtualtapcash.repository.AccountJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder encoder;

    public ApiResponseDto registerCard(String cardId, String virtualTapcashId) {

        if (tapcashCardJpaRepository.isCardAlreadyRegistered(cardId) && tapcashCardJpaRepository.isCardActive(cardId)) {

            String errorMessage = "Card Already Registered!";

            return new ApiResponseDto("error", null, errorMessage);
        }

        if (tapcashCardJpaRepository.isCardAlreadyRegistered(cardId) && !tapcashCardJpaRepository.isCardActive(cardId)) {

            String cardName = generateVirtualTapcashName(virtualTapcashId);

            tapcashCardJpaRepository.updateIsDefaultToFalse(virtualTapcashId);

            tapcashCardJpaRepository.updateTapcashCardStatusAndName("Active", cardName, cardId, true, virtualTapcashId);

            String message = "Card Successfully Registered";

            return new ApiResponseDto("success", null, message);

        }

        Optional<TapcashCard> card = tapcashCardJpaRepository.findTapcashCardsByCardId(cardId);
        TapcashCard updatedCard = card.get();

        List<TapcashCard> changeDefault = tapcashCardJpaRepository.changeIsDefault(updatedCard.getUser().getVirtualTapCashId());
        if (!changeDefault.isEmpty()) {
            for (TapcashCard tapcashCard : changeDefault) {
                tapcashCard.setIsDefault(false);
            }
            TapcashCard firstCard = changeDefault.get(1);
            firstCard.setIsDefault(true);
            tapcashCardJpaRepository.saveAll(changeDefault);
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
            newCard.setIsDefault(true);

            tapcashCardJpaRepository.save(newCard);

            String message = "Card Successfully Registered";

            return new ApiResponseDto("success", newCard, message);
        } else {
            String errorMessage = "Card Have Not  Registered Yet On BNI System";
            return new ApiResponseDto("error", null, errorMessage);
        }


    }

    public ApiResponseDto registerCardV2(String rfid, String virtualTapcashId) {
        if (tapcashCardJpaRepository.isCardAlreadyRegisteredByRfid(rfid) && tapcashCardJpaRepository.isCardActiveByRfid(rfid)) {

            String errorMessage = "Card Already Registered";

            return new ApiResponseDto("error", null, errorMessage);
        }

        if (tapcashCardJpaRepository.isCardAlreadyRegisteredByRfid(rfid) && !tapcashCardJpaRepository.isCardActiveByRfid(rfid)) {
            String cardName = generateVirtualTapcashName(virtualTapcashId);
            tapcashCardJpaRepository.updateIsDefaultToFalse(virtualTapcashId);
            tapcashCardJpaRepository.updateTapcashCardStatusAndNameByRfid("Active", cardName, rfid, true, virtualTapcashId);
            String message = "Card Successfully Registered Again";

            return new ApiResponseDto("success", null, message);
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
            newCard.setIsDefault(true);
            tapcashCardJpaRepository.save(newCard);

            String message = "Card Successfully Registered";

            return new ApiResponseDto("success", newCard, message);
        } else {
            String errorMessage = "Card Have Not  Registered Yet On BNI System";
            return new ApiResponseDto("error", null, errorMessage);
        }
    }



    public ApiResponseDto getAllCard(String virtualTapCashId) {

        List<TapcashCard> cardsData = tapcashCardJpaRepository.findTapcashCardsByVirtualTapcashId(virtualTapCashId, "Active");
        if (!cardsData.isEmpty()) {
            String message = "cards data retrieved successfully";
            return new ApiResponseDto("success", cardsData, message);
        } else {
            String errorMessage = "No Cards Found for Virtual Tapcash ID: " + virtualTapCashId;
            return new ApiResponseDto("error", null, errorMessage);
        }

    }

    public ApiResponseDto getOneCard(String cardId) {
        Optional <TapcashCard> card = tapcashCardJpaRepository.findTapcashCardsByCardId(cardId);
        if (card.isPresent()) {
            String message = "card data retrieved successfully";
            return new ApiResponseDto("success", card, message);
        } else {
            String errorMessage = "No Cards Found for  Tapcash ID: " + cardId;
            return new ApiResponseDto("success", null, errorMessage);
        }
    }


    public ApiResponseDto updateCard(Long userId, String cardId, String pin) {
        Optional<MBankingAccount> account = accountJpaRepository.findById(userId);

        if (account.isPresent()) {
            String hashedPin = encoder.encode(pin);

            if (account.get().getPin().equals(hashedPin)) {
                Optional <TapcashCard> card = tapcashCardJpaRepository.findTapcashCardsByCardId(cardId);
                TapcashCard updatedCard = card.get();

                updatedCard.setStatus("Inactive");
                updatedCard.setCardName("");
                updatedCard.setIsDefault(false);
                updatedCard.setUser(null);

                tapcashCardJpaRepository.save(updatedCard);

                List<TapcashCard> changeDefault = tapcashCardJpaRepository.changeIsDefault(account.get().getVirtualTapCashId());
                if (!changeDefault.isEmpty()) {
                    TapcashCard firstCard = changeDefault.get(0);
                    firstCard.setIsDefault(true);
                    tapcashCardJpaRepository.save(firstCard);
                }

                String message = "Card Removed Successfully!";

                return new ApiResponseDto("success", null, message);
            } else {
                return new ApiResponseDto("error", null, "Wrong pin number");
            }
        } else {
            return new ApiResponseDto("error", null, "Account not found");
        }
    }

    public ApiResponseDto changeCard(String cardId) {
        Optional <TapcashCard> card = tapcashCardJpaRepository.findTapcashCardsByCardId(cardId);
        TapcashCard changedCard = card.get();

        List<TapcashCard> removeDefault = tapcashCardJpaRepository.changeDefaultCard(changedCard.getUser().getVirtualTapCashId(), cardId);
        if (!removeDefault.isEmpty()) {
            TapcashCard firstCard = removeDefault.get(0);
            firstCard.setIsDefault(false);
            tapcashCardJpaRepository.save(firstCard);
        }

        changedCard.setIsDefault(true);
        tapcashCardJpaRepository.save(changedCard);
        String message = "Card Default Successfully Changed!";

        return new ApiResponseDto("success", null, message);
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