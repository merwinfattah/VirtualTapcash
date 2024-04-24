package org.example.virtualtapcash.services;

import org.example.virtualtapcash.models.TapcashCard;
import org.example.virtualtapcash.repository.TapcashCardJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TapcashCardService {

    @Autowired
    private TapcashCardJpaRepository tapcashCardJpaRepository;

    public TapcashCard registerCard(TapcashCard card) {
        return tapcashCardJpaRepository.save(card);
    }

    public List<TapcashCard> getAllCard() {
        return tapcashCardJpaRepository.findAll();
    }

    public Optional<TapcashCard> getCardByRfid(String rfid) {
        return tapcashCardJpaRepository.findById(rfid);
    }

    public void deleteCard(String rfid) {
        tapcashCardJpaRepository.deleteById(rfid);
    }

    public TapcashCard updateCard(TapcashCard card) {
        return  tapcashCardJpaRepository.save(card);
    }

}
