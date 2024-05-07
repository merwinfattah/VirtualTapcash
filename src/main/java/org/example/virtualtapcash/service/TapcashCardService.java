package org.example.virtualtapcash.service;

import org.example.virtualtapcash.dto.general.response.ApiResponseDto;
import org.example.virtualtapcash.exception.account.AccountNotFoundException;
import org.example.virtualtapcash.exception.account.BadCredentialException;
import org.example.virtualtapcash.exception.card.CardNotFoundException;
import org.example.virtualtapcash.exception.card.CardRegisteredException;
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

    public ApiResponseDto registerCard(String cardId, String virtualTapcashId) throws CardRegisteredException, CardNotFoundException {

        if (tapcashCardJpaRepository.isCardAlreadyRegistered(cardId) && tapcashCardJpaRepository.isCardActive(cardId)) {

            throw new CardRegisteredException("Card Already Registered!");

        }

        if (tapcashCardJpaRepository.isCardAlreadyRegistered(cardId) && !tapcashCardJpaRepository.isCardActive(cardId)) {

            String cardName = generateVirtualTapcashName(virtualTapcashId);

            tapcashCardJpaRepository.updateTapcashCardStatusAndName("Active", cardName, cardId, false, virtualTapcashId);

            String message = "Card Successfully Registered";

            return new ApiResponseDto("success", null, message);

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

            String message = "Card Successfully Registered";

            return new ApiResponseDto("success", newCard, message);
        } else {
            throw new CardNotFoundException("Card Have Not  Registered Yet On BNI System");
        }


    }

    public ApiResponseDto registerCardV2(String rfid, String virtualTapcashId) throws CardRegisteredException, CardNotFoundException  {
        if (tapcashCardJpaRepository.isCardAlreadyRegisteredByRfid(rfid) && tapcashCardJpaRepository.isCardActiveByRfid(rfid)) {

            throw new CardRegisteredException("Card Already Registered!");

        }

        if (tapcashCardJpaRepository.isCardAlreadyRegisteredByRfid(rfid) && !tapcashCardJpaRepository.isCardActiveByRfid(rfid)) {
            String cardName = generateVirtualTapcashName(virtualTapcashId);
            tapcashCardJpaRepository.updateTapcashCardStatusAndNameByRfid("Active", cardName, rfid, false, virtualTapcashId);
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

            tapcashCardJpaRepository.save(newCard);

            String message = "Card Successfully Registered";

            return new ApiResponseDto("success", newCard, message);
        } else {
            throw new CardNotFoundException("Card Have Not  Registered Yet On BNI System");
        }
    }



    public ApiResponseDto getAllCard(String virtualTapCashId) throws CardNotFoundException {

        List<TapcashCard> cardsData = tapcashCardJpaRepository.findTapcashCardsByVirtualTapcashId(virtualTapCashId, "Active");
        if (!cardsData.isEmpty()) {
            String message = "cards data retrieved successfully";
            return new ApiResponseDto("success", cardsData, message);
        } else {
            throw new CardNotFoundException("No Cards Found for Virtual Tapcash ID: " + virtualTapCashId);
        }

    }

    public ApiResponseDto getOneCard(String cardId) throws CardNotFoundException {
        Optional <TapcashCard> card = tapcashCardJpaRepository.findTapcashCardsByCardId(cardId);
        if (card.isPresent()) {
            String message = "card data retrieved successfully";
            return new ApiResponseDto("success", card, message);
        } else {
            throw new CardNotFoundException("No Cards Found for  Tapcash ID: " + cardId);
        }
    }


    public ApiResponseDto updateCard(Long userId, String cardId, String pin) throws BadCredentialException, AccountNotFoundException {
        Optional<MBankingAccount> account = accountJpaRepository.findById(userId);

        if (account.isPresent()) {

            if (encoder.matches(pin, account.get().getPin())) {
                Optional <TapcashCard> card = tapcashCardJpaRepository.findTapcashCardsByCardId(cardId);
                TapcashCard updatedCard = card.get();

                updatedCard.setStatus("Inactive");
                updatedCard.setCardName("");
                updatedCard.setIsDefault(false);
                updatedCard.setUser(null);

                tapcashCardJpaRepository.save(updatedCard);

                if (card.get().getIsDefault().equals(true)) {
                    List<TapcashCard> cardList = tapcashCardJpaRepository.findTapcashCardsByVirtualTapcashIdOrderByCardNameAsc(account.get().getVirtualTapCashId());

                    for (TapcashCard tempCard : cardList) {
                        if (tempCard.getIsDefault().equals(false) && !tempCard.getCardName().equals(card.get().getCardName())) {
                            tempCard.setIsDefault(true);
                            tapcashCardJpaRepository.save(tempCard);
                            break;
                        }

                    }
                }

                String message = "Card Removed Successfully!";

                return new ApiResponseDto("success", updatedCard, message);
            } else {
                throw  new BadCredentialException("Wrong pin number!");
            }
        } else {
            throw new AccountNotFoundException("Account not found");
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