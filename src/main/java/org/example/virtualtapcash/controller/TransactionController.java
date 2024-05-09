package org.example.virtualtapcash.controller;

import org.example.virtualtapcash.dto.general.response.ApiResponseDto;
import org.example.virtualtapcash.dto.transaction.request.PaymentDto;
import org.example.virtualtapcash.dto.transaction.request.TransactionDto;
import org.example.virtualtapcash.exception.transaction.ErrorTransaction;
import org.example.virtualtapcash.service.QrService;
import org.example.virtualtapcash.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.virtualtapcash.exception.card.CardNotFoundException;
import org.example.virtualtapcash.exception.transaction.InsufficientFundsException;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    @Autowired
    private  TransactionService transactionService;

    @Autowired
    private QrService qrService;

    @GetMapping("/get-transaction-data")
    public ResponseEntity<ApiResponseDto> getTransactionData(@PathVariable String virtualTapcashId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionByVirtualTapcashId(virtualTapcashId));
        } catch (CardNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto("error", null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto("error", null, e.getMessage()));
        }
    }

    @PostMapping("/payment")
    public ResponseEntity<ApiResponseDto> createPayment(@RequestBody PaymentDto paymentDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.processPayment(paymentDto.getCardId(), paymentDto.getNominal()));
        } catch (InsufficientFundsException | CardNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto("error", null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto("error", null, e.getMessage()));
        }
    }

    @PostMapping("/qrpayment")
    public ResponseEntity<ApiResponseDto> qrPayment(@RequestBody PaymentDto paymentDto) {
        try {
            // Use cardId from the PaymentDto to check for an active QR code
            String cardId = paymentDto.getCardId();
            boolean isActive = qrService.checkIsActiveByCardId(cardId);

            if (!isActive) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto("error", null, "No active QR Code associated with this card or QR Code is not found."));
            }

            // Deactivate the QR code immediately after a successful transaction
            qrService.deactivateQrCodeByCardId(cardId);

            return ResponseEntity.status(HttpStatus.OK).body(transactionService.processPayment(cardId, paymentDto.getNominal()));
        } catch (InsufficientFundsException | CardNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto("error", null, e.getMessage()));
        } catch (ErrorTransaction e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto("error", null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto("error", null, e.getMessage()));
        }
    }


    @PostMapping("/top-up-n-withdraw")
    public ResponseEntity<ApiResponseDto> createTransaction(@RequestBody TransactionDto transactionDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.handleTopUpWithdrawal(transactionDto.getCardId(), transactionDto.getNominal(), transactionDto.getType(), transactionDto.getVirtual_tapcash_id(), transactionDto.getPin()));
        } catch (ErrorTransaction | CardNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("error", null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto("error", null, e.getMessage()));
        }
    }

}