package org.example.virtualtapcash.service;




import org.example.virtualtapcash.dto.general.response.ApiResponseDto;
import org.example.virtualtapcash.dto.transaction.response.TransactionResultDto;
import org.example.virtualtapcash.exception.transaction.ErrorTransaction;
import org.example.virtualtapcash.model.ExternalSystemCard;
import org.example.virtualtapcash.model.TapcashCard;
import org.example.virtualtapcash.model.Transaction;
import org.example.virtualtapcash.model.MBankingAccount;
import org.example.virtualtapcash.repository.ExternalSystemCardJpaRepository;
import org.example.virtualtapcash.repository.TapcashCardJpaRepository;
import org.example.virtualtapcash.repository.TransactionJpaRepository;
import org.example.virtualtapcash.repository.AccountJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.virtualtapcash.exception.card.CardNotFoundException;
import org.example.virtualtapcash.exception.transaction.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionJpaRepository transactionJpaRepository;

    @Autowired
    private TapcashCardJpaRepository tapcashCardJpaRepository;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Autowired
    private ExternalSystemCardJpaRepository externalSystemCardJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public ApiResponseDto getTransactionByVirtualTapcashId(String virtualTapcashId) throws CardNotFoundException {
        List<TapcashCard> cards = tapcashCardJpaRepository.findTapcashCardsByVirtualTapcashId(virtualTapcashId, "Active");
        TapcashCard card = cards.get(0);
        List<Transaction> transactions = transactionJpaRepository.findTransactionsByCardIdFilteredByVirtualTapcashId(card.getUser().getVirtualTapCashId());
        if (transactions.isEmpty()) {
            throw new CardNotFoundException("No Transactions Data Found for Card ID: " + card.getCardId());

        }
        return new ApiResponseDto("success", transactions, "Transactions Retrieved Successfully");
    }

    @Transactional
    public ApiResponseDto processPayment(String cardId, BigDecimal nominal) throws CardNotFoundException, InsufficientFundsException {
        TapcashCard card = tapcashCardJpaRepository.findTapcashCardsByCardId(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found with Card ID: " + cardId));

        ExternalSystemCard external = externalSystemCardJpaRepository.findTapcashCardsByCardId(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found with Card ID: " + cardId));

        BigDecimal minRequiredBalance = nominal.add(new BigDecimal("4000"));
        if (card.getTapCashBalance().compareTo(minRequiredBalance) <= 0) {
            throw new InsufficientFundsException("Insufficient funds available. A minimum balance of 4000 over the transaction amount is required.");
        }

        card.setTapCashBalance(card.getTapCashBalance().subtract(nominal));
        card.setUpdatedAt(new Date());
        external.setTapCashBalance(external.getTapCashBalance().subtract(nominal));
        tapcashCardJpaRepository.save(card);
        externalSystemCardJpaRepository.save(external);

        Transaction transaction = new Transaction();
        transaction.setCard(card);
        transaction.setNominal(nominal);
        transaction.setCreatedAt(new Date());
        transaction.setUser(card.getUser());
        transaction.setType("PAYMENT");
        transactionJpaRepository.save(transaction);

        return new ApiResponseDto("success", transaction, "Payment successful");
    }

    @Transactional
    public ApiResponseDto handleTopUpWithdrawal(String cardId, BigDecimal nominal, String type, String virtualTapcashId, String pin) throws CardNotFoundException, ErrorTransaction {
        if (nominal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ErrorTransaction("Nominal Transaksi Tidak Boleh Negatif (-).");
        }

        TapcashCard card = tapcashCardJpaRepository.findTapcashCardsByCardId(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found with Card ID: " + cardId));

        MBankingAccount mBankingAccount = accountJpaRepository.getUserByVirtualTapcashId(String.valueOf(virtualTapcashId))
                .orElseThrow(() -> new CardNotFoundException("Card not found with Virtual Tapcash Id: " + virtualTapcashId));

        ExternalSystemCard external = externalSystemCardJpaRepository.findTapcashCardsByCardId(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found with Card ID: " + cardId));

        String savedPin = mBankingAccount.getPin();
        String message = "";
        if (!passwordEncoder.matches(pin, savedPin)){
            throw new ErrorTransaction("Invalid PIN");
        }

        if ("TOPUP".equals(type)) {
            BigDecimal bankAccountBalanceAfterTopUp = mBankingAccount.getBankAccountBalance().subtract(nominal);
            if (bankAccountBalanceAfterTopUp.compareTo(BigDecimal.ZERO) < 0) {
                throw new ErrorTransaction("Bank account balance is insufficient.");
            }

            BigDecimal totalBalanceAfterTopUp = card.getTapCashBalance().add(nominal);
            if (totalBalanceAfterTopUp.compareTo(BigDecimal.valueOf(2000000)) > 0) {
                throw new ErrorTransaction("Top-up amount exceeds maximum limit.");
            }

            mBankingAccount.setBankAccountBalance(mBankingAccount.getBankAccountBalance().subtract(nominal));
            card.setTapCashBalance(totalBalanceAfterTopUp);
            card.setUpdatedAt(new Date());
            external.setTapCashBalance(totalBalanceAfterTopUp);
            message = "TOPUP";
        } else if ("WITHDRAW".equals(type)) {
            BigDecimal totalWithdraw = card.getTapCashBalance().subtract(nominal);
            if (totalWithdraw.compareTo(BigDecimal.ZERO) < 0) {
                throw new ErrorTransaction("Withdrawal amount exceeds available balance.");
            }
            mBankingAccount.setBankAccountBalance(mBankingAccount.getBankAccountBalance().add(nominal));
            card.setTapCashBalance(totalWithdraw);
            card.setUpdatedAt(new Date());
            external.setTapCashBalance((totalWithdraw));
            message = "WITHDRAW";
        }
        accountJpaRepository.save(mBankingAccount);
        tapcashCardJpaRepository.save(card);
        externalSystemCardJpaRepository.save(external);

        Transaction transaction = new Transaction();
        transaction.setCard(card);
        transaction.setNominal(nominal);
        transaction.setCreatedAt(new Date());
        transaction.setUser(card.getUser());
        transaction.setType(type);
        transactionJpaRepository.save(transaction);

        return new ApiResponseDto("success", transaction, message);
    }
}