package org.example.virtualtapcash.services;




import org.example.virtualtapcash.entities.TransactionResult;
import org.example.virtualtapcash.exceptions.ErrorTransaction;
import org.example.virtualtapcash.models.TapcashCard;
import org.example.virtualtapcash.models.Transaction;
import org.example.virtualtapcash.models.MBankingAccount;
import org.example.virtualtapcash.repository.TapcashCardJpaRepository;
import org.example.virtualtapcash.repository.TransactionJpaRepository;
import org.example.virtualtapcash.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.virtualtapcash.exceptions.CardNotFoundException;
import org.example.virtualtapcash.exceptions.InsufficientFundsException;

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
    private UserJpaRepository userJpaRepository;


    public Transaction createTransaction(Transaction transaction) { return transactionJpaRepository.save(transaction);
    }

    public List<Transaction> getAllTransaction() { return transactionJpaRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long transactionId) { return transactionJpaRepository.findById(transactionId);
    }

    public void deleteTransaction(Long transactionId) {
        transactionJpaRepository.deleteById(transactionId);
    }

    public Transaction updateTransaction (Transaction transaction) { return transactionJpaRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByRfid(String rfid) throws CardNotFoundException {
        List<Transaction> transactions = transactionJpaRepository.findTransactionsByRfid(rfid);
        if (transactions.isEmpty()) {
            throw new CardNotFoundException("No Transactions Data Found for RFID: " + rfid);
        }
        return transactions;
    }

    @Transactional
    public TransactionResult processPayment(String rfid, BigDecimal nominal) throws CardNotFoundException, InsufficientFundsException {
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
        transactionJpaRepository.save(transaction);

        return new TransactionResult(true, "Payment successful");
    }

    @Transactional
    public TransactionResult handleTopUpWithdrawal(String rfid, BigDecimal nominal, String type, String virtualTapcashId) throws CardNotFoundException, ErrorTransaction {
        TapcashCard card = tapcashCardJpaRepository.findTapcashCardsByRfid(rfid)
                .orElseThrow(() -> new CardNotFoundException("Card not found with RFID: " + rfid));

        MBankingAccount mBankingAccount = userJpaRepository.getUserByVirtualTapcashId(String.valueOf(virtualTapcashId))
                .orElseThrow(() -> new CardNotFoundException("Card not found with Virtual Tapcash Id: " + virtualTapcashId));

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
        userJpaRepository.save(mBankingAccount);
        tapcashCardJpaRepository.save(card);

        Transaction transaction = new Transaction();
        transaction.setCard(card);
        transaction.setNominal(nominal);
        transaction.setCreatedAt(new Date());
        transaction.setUser(card.getUser());
        transactionJpaRepository.save(transaction);

        return new TransactionResult(true, type + " successful");
    }



}