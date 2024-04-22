package org.example.virtualtapcash.services;

import org.example.virtualtapcash.models.ExternalSystemCard;
import org.example.virtualtapcash.repository.ExternalSystemCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExternalSystemCardService {
    @Autowired
    private ExternalSystemCardRepository externalSystemCardRepository;

    public ExternalSystemCard registerCardToSystem(ExternalSystemCard externalSystemCard) {
        return externalSystemCardRepository.save(externalSystemCard);
    }

    public List<ExternalSystemCard> getAllExternalSystemCard() {
        return externalSystemCardRepository.findAll();
    }

    public Optional<ExternalSystemCard> getCardByRfid(String rfid) {
        return externalSystemCardRepository.findById(rfid);
    }

    public  void deleteExternalSystemCard(String rfid) {
        externalSystemCardRepository.deleteById(rfid);
    }

    public ExternalSystemCard updateExternalSystemCard(ExternalSystemCard externalSystemCard) {
        return externalSystemCardRepository.save(externalSystemCard);
    }
}
