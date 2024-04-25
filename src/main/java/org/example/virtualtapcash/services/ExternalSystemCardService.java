package org.example.virtualtapcash.services;

import org.example.virtualtapcash.models.ExternalSystemCard;
import org.example.virtualtapcash.repository.ExternalSystemCardJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExternalSystemCardService {
    @Autowired
    private ExternalSystemCardJpaRepository externalSystemCardJpaRepository;

    public ExternalSystemCard registerCardToSystem(ExternalSystemCard externalSystemCard) {
        return externalSystemCardJpaRepository.save(externalSystemCard);
    }

    public List<ExternalSystemCard> getAllExternalSystemCard() {
        return externalSystemCardJpaRepository.findAll();
    }

    public Optional<ExternalSystemCard> getCardById(String cardId) {
        return externalSystemCardJpaRepository.findById(cardId);
    }

    public  void deleteExternalSystemCard(String rfid) {
        externalSystemCardJpaRepository.deleteById(rfid);
    }

    public ExternalSystemCard updateExternalSystemCard(ExternalSystemCard externalSystemCard) {
        return externalSystemCardJpaRepository.save(externalSystemCard);
    }
}
