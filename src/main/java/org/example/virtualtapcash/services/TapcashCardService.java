package org.example.virtualtapcash.services;

import org.example.virtualtapcash.models.TapcashCard;
import org.example.virtualtapcash.repository.TapcashCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TapcashCardService {

    @Autowired
    private TapcashCardRepository tapcashCardRepository;

    public TapcashCard registerCard(TapcashCard card) {
        return tapcashCardRepository.save(card);
    }

    public List<TapcashCard> getAllCard() {
        return tapcashCardRepository.findAll();
    }

    public Optional<TapcashCard> getCardByRfid(String rfid) {
        return tapcashCardRepository.findById(rfid);
    }

    public void deleteCard(String rfid) {
        tapcashCardRepository.deleteById(rfid);
    }

    public TapcashCard updateCard(TapcashCard card) {
        return  tapcashCardRepository.save(card);
    }

}
