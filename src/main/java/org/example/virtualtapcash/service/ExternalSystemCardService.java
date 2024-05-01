package org.example.virtualtapcash.service;

import org.example.virtualtapcash.model.ExternalSystemCard;
import org.example.virtualtapcash.repository.ExternalSystemCardJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExternalSystemCardService {
    @Autowired
    private ExternalSystemCardJpaRepository externalSystemCardJpaRepository;

    public Optional<ExternalSystemCard> getCardById(String cardId) {
        return externalSystemCardJpaRepository.findById(cardId);
    }

    public Optional<ExternalSystemCard> getCardByRfid(String rfid) {
        return externalSystemCardJpaRepository.findCardIdByRfid(rfid);
    }
}
