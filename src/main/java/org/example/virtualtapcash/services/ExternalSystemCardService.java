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


    public Optional<ExternalSystemCard> getCardById(String cardId) {
        return externalSystemCardJpaRepository.findById(cardId);
    }

}
