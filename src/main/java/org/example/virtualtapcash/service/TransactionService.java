package org.example.virtualtapcash.service;




import org.example.virtualtapcash.dto.transaction.response.TransactionResultDto;
import org.example.virtualtapcash.exception.transaction.ErrorTransaction;
import org.example.virtualtapcash.model.TapcashCard;
import org.example.virtualtapcash.model.Transaction;
import org.example.virtualtapcash.model.MBankingAccount;
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

@Service
public class TransactionService {
    @Autowired
    private TransactionJpaRepository transactionJpaRepository;

    @Autowired
    private TapcashCardJpaRepository tapcashCardJpaRepository;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<Transaction> getTransactionsByRfid(String rfid) throws CardNotFoundException {
        List<Transaction> transactions = transactionJpaRepository.findTransactionsByRfid(rfid);
        if (transactions.isEmpty()) {
            throw new CardNotFoundException("No Transactions Data Found for RFID: " + rfid);
        }
        return transactions;
    }

    @Transactional
    public TransactionResultDto processPayment(String rfid, BigDecimal nominal) throws CardNotFoundException, InsufficientFundsException {
        TapcashCard card = tapcashCardJpaRepository.findTapcashCardsByRfid(rfid)
                .orElseThrow(() -> new CardNotFoundException("Card not found with RFID: " + rfid));

        BigDecimal minRequiredBalance = nominal.add(new BigDecimal("4000"));
        if (card.getTapCashBalance().compareTo(minRequiredBalance) <= 0) {
            throw new InsufficientFundsException("Insufficient funds available. A minimum balance of 4000 over the transaction amount is required.");
        }

        card.setTapCashBalance(card.getTapCashBalance().subtract(nominal));
        tapcashCardJpaRepository.save(card);

        Transaction transaction = new Transaction();
        transaction.setCard(card);
        transaction.setNominal(nominal);
        transaction.setCreatedAt(new Date());
        transaction.setUser(card.getUser());
        transaction.setType("PAYMENT");
        transactionJpaRepository.save(transaction);

        return new TransactionResultDto(true, "Payment successful");
    }

    @Transactional
    public TransactionResultDto handleTopUpWithdrawal(String rfid, BigDecimal nominal, String type, String virtualTapcashId, String pin) throws CardNotFoundException, ErrorTransaction {
        TapcashCard card = tapcashCardJpaRepository.findTapcashCardsByRfid(rfid)
                .orElseThrow(() -> new CardNotFoundException("Card not found with RFID: " + rfid));

        MBankingAccount mBankingAccount = accountJpaRepository.getUserByVirtualTapcashId(String.valueOf(virtualTapcashId))
                .orElseThrow(() -> new CardNotFoundException("Card not found with Virtual Tapcash Id: " + virtualTapcashId));

        String savedPin = mBankingAccount.getPin();
        if (!passwordEncoder.matches(pin, savedPin)){
            throw new ErrorTransaction("Invalid PIN");
        }

        if ("TOPUP".equals(type)) {
            BigDecimal totalBalanceAfterTopUp = card.getTapCashBalance().add(nominal);
            if (totalBalanceAfterTopUp.compareTo(BigDecimal.valueOf(2000000)) > 0) {
                throw new ErrorTransaction("Top-up amount exceeds maximum limit.");
            }
            mBankingAccount.setBankAccountBalance(mBankingAccount.getBankAccountBalance().subtract(nominal));
            card.setTapCashBalance(totalBalanceAfterTopUp);
        } else if ("WITHDRAW".equals(type)) {
            BigDecimal totalWithdraw = card.getTapCashBalance().subtract(nominal);
            if (totalWithdraw.compareTo(BigDecimal.ZERO) < 0) {
                throw new ErrorTransaction("Withdrawal amount exceeds available balance.");
            }
            mBankingAccount.setBankAccountBalance(mBankingAccount.getBankAccountBalance().add(nominal));
            card.setTapCashBalance(totalWithdraw);
        }
        accountJpaRepository.save(mBankingAccount);
        tapcashCardJpaRepository.save(card);

        Transaction transaction = new Transaction();
        transaction.setCard(card);
        transaction.setNominal(nominal);
        transaction.setCreatedAt(new Date());
        transaction.setUser(card.getUser());
        transaction.setType(type);
        transactionJpaRepository.save(transaction);

        return new TransactionResultDto(true, type + " successful");
    }



}