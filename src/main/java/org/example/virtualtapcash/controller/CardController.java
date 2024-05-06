package org.example.virtualtapcash.controller;

import org.example.virtualtapcash.dto.card.request.AddCardDto;
import org.example.virtualtapcash.dto.card.request.AddCardV2Dto;
import org.example.virtualtapcash.dto.card.request.ChangeCardDto;
import org.example.virtualtapcash.dto.card.request.RemoveCardDto;
import org.example.virtualtapcash.dto.general.response.ApiResponseDto;
import org.example.virtualtapcash.service.TapcashCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@Controller
@CrossOrigin
@RequestMapping("/api/v1/card")
public class CardController {

    @Autowired
    TapcashCardService tapcashCardService;

    @GetMapping ("/get-cards-data/{virtualTapCashId}")
    public ResponseEntity<ApiResponseDto> getCardsData(@PathVariable String virtualTapCashId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(tapcashCardService.getAllCard(virtualTapCashId));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto("error", null, errorMessage));
        }
    }

    @GetMapping("/get-card/{cardId}")
    public ResponseEntity<ApiResponseDto> getOneCard(@PathVariable String cardId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(tapcashCardService.getOneCard(cardId));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto("error", null, errorMessage));
        }
    }

    @PostMapping ("/add-card")
    public ResponseEntity<ApiResponseDto> addCard(@RequestBody AddCardDto request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(tapcashCardService.registerCard(request.getCardId(), request.getVirtualTapcashId()));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto("error", null, errorMessage));
        }
    }

    @PostMapping("/add-card2")
    public ResponseEntity<ApiResponseDto> addCardV2(@RequestBody AddCardV2Dto addCardV2Dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(tapcashCardService.registerCardV2(addCardV2Dto.getRfid(), addCardV2Dto.getVirtualTapcashId()));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto("error", null, errorMessage));
        }
    }

    @PatchMapping("/remove-card")
    public ResponseEntity<ApiResponseDto> removeCard(@RequestBody RemoveCardDto request) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(tapcashCardService.updateCard(request.getUserId(), request.getCardId(), request.getPin()));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto("error", null, errorMessage));
        }
    }

    @PatchMapping("/change-card")
    public ResponseEntity<ApiResponseDto> changeCard(@RequestBody ChangeCardDto request) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(tapcashCardService.changeCard(request.getCardId()));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto("error", null, errorMessage));
        }
    }





}